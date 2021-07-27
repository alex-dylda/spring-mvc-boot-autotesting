package com.acme.dbo.domain;

import java.math.BigDecimal;

public class Account {
    private int id;
    private BigDecimal amount;

    public Account(int id, BigDecimal amount) {
        this.id = id;
        this.amount = amount;
    }

    public Account(final Account account) {
        this.id = account.getId();
        this.amount = account.getAmount();
    }

    public int getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Account " + getId() + " : " + getAmount();
    }
}
