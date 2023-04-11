package com.banker.models;


import java.math.BigDecimal;

/**
 * @author Aleksei Chursin
 */

// POJO class for Account //

public class Account {

    private String iban;

    private int customerId;

    private String currency;

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
