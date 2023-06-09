package com.banker.models;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * @author Aleksei Chursin
 */

// POJO class for Transfer //
public class Transfer {

    @Positive
    private BigDecimal amount;

    @Size(min = 5, max = 34)
    private String debtorIban;

    @Size(min = 5, max = 34)
    private String creditorIban;

    private String message;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDebtorIban() {
        return debtorIban;
    }

    public void setDebtorIban(String debtorIban) {
        this.debtorIban = debtorIban;
    }

    public String getCreditorIban() {
        return creditorIban;
    }

    public void setCreditorIban(String creditorIban) {
        this.creditorIban = creditorIban;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Transfer() { }

    public Transfer(String debtorIban, String creditorIban, BigDecimal amount, String message) {
        this.amount = amount;
        this.debtorIban = debtorIban;
        this.creditorIban = creditorIban;
        this.message = message;
    }

    public String toString() {
        return "Debtor IBAN: " + getDebtorIban() + "\nCreditor IBAN: " + getCreditorIban() +
                "\nAmount: " + getAmount() + "\nMesage: " + getMessage();
    }

}
