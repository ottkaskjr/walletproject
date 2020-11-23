package com.company.rowmappers;

import com.company.Blacklist;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RowMapper for creating Blacklist objects while retrieving data from db
 */
public class BlacklistRowMapper implements RowMapper<Blacklist> {
    @Override
    public Blacklist mapRow(ResultSet resultSet, int i) throws SQLException {
        Blacklist blacklist = new Blacklist();
        blacklist.setId(resultSet.getBigDecimal("id"));
        blacklist.setPlayer_id(resultSet.getBigDecimal("player_id"));
        return blacklist;
    }
}