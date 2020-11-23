package com.company.repository;

import com.company.Blacklist;
import com.company.Configuration;
import com.company.Player;
import com.company.exceptions.ApplicationException;
import com.company.rowmappers.BlacklistRowMapper;
import com.company.rowmappers.ConfigurationRowMapper;
import com.company.rowmappers.PlayerRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring @Repository Class. All operations concerning sql requests are here. Returns data back to @Service
 *
 * Methods:
 *      getPlayerByUsername(String username) - Select player from db
 *      onBlacklist(BigDecimal playerID) - Checks if player is on blacklist
 *      getConfiguration() - Gets configuration from db
 *      addNewPlayer(RequestJSON requestJSON) - Adds new player to db
 *      updatePlayer(Player player) - Updates a given player in db either by periodical background process or at the
 *      end of session
 *
 */
@Repository
public class GameRepository {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    /* ======= GET ======= */
    /**
     * This method selects player from db and returns it to @Service
     *
     * @param username - A RequestParam for username
     * @return List<Player> - Returns player object contained in a List from db or empty List
     */
    // GET PLAYER BY USERNAME
    public List<Player> getPlayerByUsername(String username){
        String getPlayerSql = "SELECT * FROM player WHERE username = :username";
        Map <String, Object> getPlayerParamMap = new HashMap<>();
        getPlayerParamMap.put("username", username);
        return jdbcTemplate.query(getPlayerSql, getPlayerParamMap, new PlayerRowMapper());
    }

    /**
     * This method checks if player is in blacklist
     *
     * @param playerID - A player_id column
     * @return List<Player> - Returns player object contained in a List from db or empty List
     */
    // CHECK IF PLAYER IS ON BLACKLIST
    public List<Blacklist> onBlacklist(BigDecimal playerID){
        String sql = "SELECT * FROM blacklist WHERE player_id = :player_id";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("player_id", playerID);
        return jdbcTemplate.query(sql, paramMap, new BlacklistRowMapper());
    }
    /**
     * This method selects configuration from db. If it doesn't exist, creates one with default values.
     *
     * @return Configuration - Returns configuration object
     */
    // GET CONFIGURATION (BALANCE CHANGE LIMIT)
    public Configuration getConfiguration(){
        String sql = "SELECT * FROM configuration";
        List<Configuration> confList =  jdbcTemplate.query(sql, new HashMap<>(), new ConfigurationRowMapper());
        if (confList.size() > 0){
            return confList.get(0);
        }
        // if configuration does not exist in the db, create one
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("limit", new BigDecimal("1000"));
        jdbcTemplate.update("INSERT INTO configuration (balance_change_limit) VALUES (:limit)", paramMap);
        return jdbcTemplate.queryForObject("SELECT * FROM configuration", new HashMap<>(), new ConfigurationRowMapper());
    }

    /* ======= POST ======= */

    /**
     * This inserts new player to db. If a player by provided username already exists, throws exception.
     *
     * @param username - A username for the new player
     * @return Player - Returns newly created player object
     */
    // ADD NEW PLAYER
    public Player addNewPlayer(String username){

        // create new player
        String sql = "INSERT INTO player (username, balance, balance_version) VALUES (:username, :balance, :balance_version)";
        Map <String, Object> paramMap = new HashMap<>();
        paramMap.put("username", username);
        paramMap.put("balance", new BigDecimal("100"));
        paramMap.put("balance_version", new BigDecimal("1"));

        try {
            // get and return newly created player
            jdbcTemplate.update(sql, paramMap);
            return getPlayerByUsername(username).get(0);

        } catch(Exception e){   // if player by given username already exists return exception message
            throw new ApplicationException("Player by given username already exists");
        }
    }

    /* ======= PUT ======= */
    /**
     * This updates the player on session
     *
     * @param player - A player who is being updated
     */
    // UPDATE PLAYER BALANCE AND BALANCE_VERSION
    public void updatePlayer(Player player){
        //String transactionSQL = "INSERT INTO transaction";
        String updatePlayerSQL = "UPDATE player SET balance = :balance, balance_version = :balance_version WHERE username = :username";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("username", player.getUsername());
        paramMap.put("balance", player.getBalance());
        paramMap.put("balance_version", player.getBalanceVersion().add(new BigDecimal("1")));
        // IF UPDATE IS SUCCESSFUL
        if (jdbcTemplate.update(updatePlayerSQL, paramMap) == 1){
            player.setBalanceVersion(player.getBalanceVersion().add(new BigDecimal("1")));
        };
    }

}
