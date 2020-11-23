package com.company;

import java.math.BigDecimal;
/**
 * Class currently not in use.
 */
public class Transaction {
    private BigDecimal id;
    private BigDecimal balance_change;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getBalance_change() {
        return balance_change;
    }

    public void setBalance_change(BigDecimal balance_change) {
        this.balance_change = balance_change;
    }
}
