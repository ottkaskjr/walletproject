package com.company;

import java.math.BigDecimal;

/**
 * Blacklist class for creating Blacklist objects.
 *
 * values:
 *      id - id of Blacklist
 *      player_id - reference to Player.id
 */
public class Blacklist {
    private BigDecimal id;
    private BigDecimal player_id;

    public Blacklist() {
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(BigDecimal player_id) {
        this.player_id = player_id;
    }
}


