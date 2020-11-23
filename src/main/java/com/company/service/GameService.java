package com.company.service;


import com.company.Configuration;
import com.company.Player;
import com.company.exceptions.ApplicationException;
import com.company.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Spring @Service Class. All operations concerning data are done here. Passes all sql requests to @Repository
 *
 * Methods:
 *      getPlayerByUsername(String username) - Gets player by username and starts a game session
 *      addNewPlayer(RequestJSON requestJSON) - Creates a new player
 *
 */

@Service
public class GameService {
    // COLORS FOR LOG
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;


    /**
     * This method takes gets player from @Repository and starts a game session
     *
     * @param username - A RequestParam for username
     * @param random - when true, creates a session with random transactions
     * @return Player - Returns latest player object from db at the end of the session
     */
    /* ======= GET ======= */
    // GET PLAYER BY USERNAME
    public Player getPlayerByUsername(String username, boolean random){
        List<Player> playerList = gameRepository.getPlayerByUsername(username);

        // if player by provided username is not found return exception message
        if(playerList.size() == 0){
            throw new ApplicationException("Player by given username does not exists");
        }



        // If no configuration, create one and get balance change limit
        Configuration configuration = gameRepository.getConfiguration();
        // else start game session using Scanner class

        Player player = playerList.get(0);
        // start game session
        startGame(player, configuration, random);

        // return player at the end of the session
        return player;
    }

    /* ======= POST ======= */
    /**
     * This method is used to create a new player either by providing a json object or a request parameter.
     * The request is passed on to @Service
     *
     * @param username - String username from @Controller
     * @return Player - Returns the new player
     */
    // ADD NEW PLAYER BY UNIQUE USERNAME
    public Player addNewPlayer(String username){

        // if no username is provided return error;
        if(username == null || username.isEmpty()){
            throw new ApplicationException("Please provide a username");
        }
        // else return player by provided username via @Repository
        System.out.println(ANSI_BLUE + "=== PLAYER " + username + " CREATED ===" + ANSI_RESET);
        return gameRepository.addNewPlayer(username);

    }


    /**
     * This method starts a game session in system log by using Scanner class.
     *
     * @param player - An object of the Player having the game session
     * @param configuration - An object containing the balance change limit and current total balance change per session
     *
     * @controls:
     *      "play [sum]" - spends 50 credits ("play 50")
     *      "add [sum]" increases wallet ("add 100")
     *      "exit" ends player session
     *
     */
    public void startGame(Player player, Configuration configuration, boolean random){
        // PLAYER IN LOG
        System.out.println(ANSI_BLUE + "=== PLAYER " + player.getUsername() + " IN ===" + ANSI_RESET);
        // SET PERIODICAL UPDATE
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameRepository.updatePlayer(player);
            }
        }, 1000, 5000);

        // if game is set to random transactions
        if(random){
            // CREATE SCANNER
            System.out.println("GAME SET TO RANDOM");
            gameUI(player.getUsername(), player.getBalance()); // log player ui

            String[] commands = {"add ", "play ", "exit"};

            for(String command : commands){
                int randomInt = (int) (Math.random() * 100.0);
                String Command = command + randomInt;
                if(command.equals("exit")){
                    Command = command;
                }
                System.out.println(Command);
                handleCommand(Command, player, configuration);
            }
        } else { // MANUAL SESSION
            // CREATE SCANNER
            Scanner scanner = new Scanner(System.in);
            gameInstructions(); // log game instructions
            gameUI(player.getUsername(), player.getBalance()); // log player ui

            String command = scanner.nextLine(); // wait user command

            while(!command.equals("exit")){
                handleCommand(command, player, configuration);
                gameUI(player.getUsername(), player.getBalance());
                command = scanner.nextLine();
            }
        }


        System.out.println("Session over");
        // CANCEL TIMER
        timer.cancel();
        System.out.println(ANSI_BLUE + "=== PLAYER " + player.getUsername() + " OUT ===" + ANSI_RESET);
        // WHEN SESSION OVER, UPDATE PLAYER IN DATABASE


    }

    /**
     * This method prints a simple UI that includes player username and balance.
     *
     * @param username
     * @param balance
     *
     */
    public void gameUI(String username, BigDecimal balance){

        System.out.println(ANSI_RESET + username + ", balance: " + ANSI_YELLOW + balance + "\n" +
                ANSI_PURPLE + "Command:");
    }

    /**
     * This method prints the info concering session commands.
     */
    public void gameInstructions(){
        System.out.println(ANSI_GREEN + "/**\n" +
                "         * @controls\n" +
                "         * \"play [sum]\" - spends 50 credits (\"play 50\") \n" +
                "         * \"add [sum]\" increases wallet (\"add 100\")\n" +
                "         * \"exit\" ends player session \n" +
                "         */");
    }

    /**
     * This method handles balance operations of the player in memory. Prevents transactions that go
     * above the balance_change_limit or below 0 balance.
     *
     * @param command - Scanner input ("add [sum]", "play [sum]", "exit")
     * @param player - player object in memory
     * @param conf - configuration object in memory containing balance change limit and current total balance change
     *
     */
    public void handleCommand(String command, Player player, Configuration conf){
        try {
            if(command.contains("play")){ // spend credits
                String balanceChangeStr = command.replace("play ", "");
                BigDecimal balanceChange = new BigDecimal(balanceChangeStr);
                // if limit is not exceeded
                if(conf.getBalanceChangeLimit().subtract(conf.getCurrentChange()).compareTo(balanceChange) > 0){
                    BigDecimal balance = player.getBalance();

                    // if balance > balanceChange
                    if (balance.compareTo(balanceChange) >= 0){
                        // overwrite player balance by balance - balanceChange
                        player.setBalance(balance.subtract(balanceChange));
                        conf.setCurrentChange(conf.getCurrentChange().add(balanceChange));
                        System.out.println("Spent " + balanceChangeStr + " credits");
                    } else {
                        System.out.println("Insufficient credits");
                    }
                } else {
                    System.out.println("Balance change limit is exceeded");
                }

            } else if(command.contains("add")){ // adds more credits
                // CHECK IF PLAYER IS NOT IN BLACKLIST
                if(gameRepository.onBlacklist(player.getId()).size() < 1){
                    String balanceChangeStr = command.replace("add ", "");
                    BigDecimal balanceChange = new BigDecimal(balanceChangeStr);

                    // if limit is not exceeded
                    if(conf.getBalanceChangeLimit().subtract(conf.getCurrentChange()).compareTo(balanceChange) > 0){
                        BigDecimal balance = player.getBalance();

                        player.setBalance(balance.add(balanceChange));
                        conf.setCurrentChange(conf.getCurrentChange().add(balanceChange));
                        System.out.println("Added " + balanceChangeStr + " credits");

                    } else {
                        System.out.println("Balance change limit is exceeded");
                    }
                } else {
                    // prevent adding new credits if player is in blacklist
                    System.out.println("Cannot add more credits. Player is in blacklist");
                }


            } else { // unknown command
                System.out.println("Unknown command");
            }

        } catch (Exception e){ // exception catch for illegal inputs
            System.out.println("Unknown command");
        }
    }
}
