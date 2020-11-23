package com.company;

import java.math.BigDecimal;

/**
 * Class for creating Configuration objects.
 *
 * values:
 *      balanceChangeLimit - the limit for balance changes
 *      currentChange - current threshold of balance change
 */
public class Configuration {
    private BigDecimal balanceChangeLimit;
    private BigDecimal currentChange;

    public Configuration() {
        //this.balanceChangeLimit = new BigDecimal("1000");
        this.currentChange = new BigDecimal("0");
    }

    public BigDecimal getCurrentChange() {
        return currentChange;
    }

    public void setCurrentChange(BigDecimal currentChange) {
        this.currentChange = currentChange;
    }

    public void setBalanceChangeLimit(BigDecimal balanceChangeLimit) {
        this.balanceChangeLimit = balanceChangeLimit;
    }

    public BigDecimal getBalanceChangeLimit() {
        return balanceChangeLimit;
    }
}
