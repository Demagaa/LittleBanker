package com.banker.controllers;

import com.banker.dao.AccountService;
import com.banker.models.Account;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aleksei Chursin
 */

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService; // Data access object //

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE) // Spring REST annotation //
    public ResponseEntity createAccount(@RequestBody Account newAccount) {
        accountService.save(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{iban}") // Spring REST annotation //
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity deleteAccount(@PathVariable String iban) {
        if (!accountService.delete(iban))
            throw new ErrorResponseException(HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/summary/{iban}",
            produces = MediaType.APPLICATION_JSON_VALUE) // Spring REST annotation //
    public ResponseEntity getSummary(@PathVariable String iban) {
        if (accountService.getSummary(iban) == null)
            throw new ErrorResponseException(HttpStatus.NOT_FOUND);
        return new ResponseEntity(accountService.getSummary(iban), HttpStatus.OK);
    }

    @PostMapping(value = "/update/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE) // Spring REST annotation //
    public ResponseEntity updateAccount(@RequestBody Account account) {
        accountService.update(account);
        return new ResponseEntity(HttpStatus.OK);
    }
}

