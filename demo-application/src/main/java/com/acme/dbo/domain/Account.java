package com.acme.dbo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {
    private int id;
    private BigDecimal amount;

    @JsonCreator
    public Account(@JsonProperty("id") int id, @JsonProperty("amount") BigDecimal amount) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id && Objects.equals(amount, account.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount);
    }
}
