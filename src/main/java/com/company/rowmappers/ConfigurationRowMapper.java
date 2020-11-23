package com.company.rowmappers;

import com.company.Configuration;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RowMapper for creating Configuration objects while retrieving data from db
 */
public class ConfigurationRowMapper implements RowMapper<Configuration> {
    @Override
    public Configuration mapRow(ResultSet resultSet, int i) throws SQLException {
        Configuration configuration = new Configuration();
        configuration.setBalanceChangeLimit(resultSet.getBigDecimal("balance_change_limit"));
        return configuration;
    }
}