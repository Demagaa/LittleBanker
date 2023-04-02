package com.banker.controllers;

import com.banker.dao.AccountDAO;
import com.banker.models.Account;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Properties;

/**
 * @author Aleksei Chursin
 */

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountDAO accountDAO; // Data access object //

    public AccountController(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @PostMapping(value = "/", consumes = {"text/plain"}, produces = {"text/plain"}) // Spring REST annotation //
    @ResponseStatus(HttpStatus.CREATED)
    public String createAccount(@RequestBody String r) {
        Properties properties = accountDAO.parsePropertiesString(r); // Parsing string data //
        Account newAccount = new Account();
        newAccount.setIban(properties.getProperty("iban"));
        newAccount.setCustomerId(Integer.parseInt(properties.getProperty("customerid")));
        newAccount.setBalance(BigDecimal.valueOf(100.0));
        newAccount.setCurrency(properties.getProperty("currency"));
        accountDAO.save(newAccount);

        return "Created account with following properties: " + r;
    }

    @DeleteMapping("/delete/") // Spring REST annotation //
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccount(@RequestParam String iban) {
        if (!accountDAO.delete(iban))
            throw new ErrorResponseException(HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/summary/") // Spring REST annotation //
    @ResponseStatus(HttpStatus.OK)
    public String getSummary(@RequestParam String iban) {
        if (accountDAO.getSummary(iban) == null)
            throw new ErrorResponseException(HttpStatus.NOT_FOUND);
        return accountDAO.getSummary(iban).toString();
    }

    @PostMapping(value = "/update/", consumes = {"text/plain"}, produces = {"text/plain"}) // Spring REST annotation //
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateAccount(@RequestBody String r) {
        Properties properties = accountDAO.parsePropertiesString(r); // Parsing string data //
        Account newAccount = new Account();
        newAccount.setIban(properties.getProperty("iban"));
        newAccount.setCustomerId(Integer.parseInt(properties.getProperty("customerid")));
        newAccount.setBalance(BigDecimal.valueOf(Double.parseDouble(properties.getProperty("amount"))));
        newAccount.setCurrency(properties.getProperty("currency"));
        if (!accountDAO.update(newAccount))
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST);

    }
}

