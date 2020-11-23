package com.company.controller;

import com.company.Player;
import com.company.RequestJSON;
import com.company.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.*;


/**
 * Spring REST controller. Maps all available requests and passes all data to @Service
 *
 * Methods:
 *      getPlayerByUsername(String username) - Gets player by username from db
 *      addNewPlayer(RequestJSON requestJSON) - Creates and addes a new player to the db
 *
 NB!! server.port=8070
 */

// jdbcTemplate
// "SELECT FOR UPDATE SET value = :value WHERE id = :id
// "UPDATE SET balance = :balance WHERE id = :id AND balance_version = :balance_version
@RestController
public class GameController {
    @Autowired
    private GameService gameService;
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    /* ======= GET ======= */
    /**
     * This method is used for getting a previously created player from db by
     * unique username and starting a manual game session in system log via java.util.Scanner.
     * The request is on hold, as long the player/user has finished the session after which the
     * latest version of player is returned from the db. Periodical background update is running
     * while the session is on. The request is passed on to @Service
     *
     * @param username - A RequestParam for username
     * @param random - when true, creates a session with random transactions, otherwise manual session
     * @return Player - Returns latest player object from db at the end of the session
     */
    // GET PLAYER BY USERNAME AND START GAME SESSION
    /* localhost:8070/start?username=player1?random=true */
    @GetMapping("/start")
    public Player getPlayerByUsername(@RequestParam("username") String username,
                                      @RequestParam("random") boolean random){
        return gameService.getPlayerByUsername(username, random);
    }


    /* ======= POST ======= */
    /**
     * This method is used to create a new player either by providing a json object or a request parameter.
     * The request is passed on to @Service
     *
     *
     * @param requestJSON - A json RequestBody for username { "data": "player123" }
     * @param username - ALTERNATIVE RequestParam for browser URL tab. This way requestJSON is ignored
     * @return Player - Returns the new player
     */
    // ADD NEW PLAYER
    @PostMapping("/addnewplayer")
    public Player addNewPlayer(@RequestBody RequestJSON requestJSON,
                               @RequestParam(name="username", required = false) String username){
        if(username == null){
            return gameService.addNewPlayer(requestJSON.data);
        } else {
            return gameService.addNewPlayer(username);
        }
    }

}
