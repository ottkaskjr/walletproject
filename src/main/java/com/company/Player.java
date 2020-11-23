package com.company;

import java.math.BigDecimal;

/**
 * Class for creating Player objects.
 *
 * values:
 *      id - Unique id of Player
 *      username - Unique username of Player
 *      balanceVersion - Latest balance change version
 *      balance - Player's balance
 */
public class Player {
    private BigDecimal id;
    private String username;
    private BigDecimal balanceVersion;
    private BigDecimal balance;

    public Player(){

    }

    public Player(String username) {
        this.username = username;
        this.balanceVersion = new BigDecimal("1");
    }
    public Player(BigDecimal id,String username, BigDecimal balanceVersion, BigDecimal balance){
        this.id = id;
        this.username = username;
        this.balanceVersion = balanceVersion;
        this.balance = balance;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getBalanceVersion() {
        return balanceVersion;
    }

    public void setBalanceVersion(BigDecimal balanceVersion) {
        this.balanceVersion = balanceVersion;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
