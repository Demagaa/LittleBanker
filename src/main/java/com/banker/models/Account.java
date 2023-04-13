package com.banker.models;


import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * @author Aleksei Chursin
 */

// POJO class for Account //

public class Account {

    @Size(min = 5, max = 34)
    private String iban;

    @Positive
    private int customerId;


    @Size(min = 3, max = 3)
    private String currency;

    @PositiveOrZero(message = "Balance can't be negative")
    private BigDecimal balance;



    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    // Override toString() function to get account information //
    public String toString() {
        return "Iban: " + getIban() + "\nCustomer ID: " + customerId + "\nBalance: " + getBalance() + "\nCurrency: " + getCurrency();
    }

}
