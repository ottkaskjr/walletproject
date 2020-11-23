package com.company.rowmappers;

import com.company.Player;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * RowMapper for creating Player objects while retrieving data from db
 */
public class PlayerRowMapper implements RowMapper<Player> {
    @Override
    public Player mapRow(ResultSet resultSet, int i) throws SQLException {
        Player player = new Player();
        player.setId(resultSet.getBigDecimal("id"));
        player.setUsername(resultSet.getString("username"));
        player.setBalance(resultSet.getBigDecimal("balance"));
        player.setBalanceVersion(resultSet.getBigDecimal("balance_version"));
        return player;
    }
}
