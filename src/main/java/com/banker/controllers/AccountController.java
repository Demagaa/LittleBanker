package com.banker.controllers;

import com.banker.services.AccountService;
import com.banker.models.Account;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import org.iban4j.Iban;
import org.iban4j.IbanFormatException;
import org.iban4j.IbanUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;


/**
 * @author Aleksei Chursin
 */

@RestController
@RequestMapping("/accounts")
@Validated
public class AccountController {
    private final AccountService accountService; // Data access object //

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE) // Spring REST annotation //
    public ResponseEntity createAccount( @RequestBody @Valid Account newAccount) {
        if (!accountService.save(newAccount))
            return new ResponseEntity("IBAN is already registered", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{iban}") // Spring REST annotation //
    public ResponseEntity deleteAccount(@PathVariable @Size(min = 5, max = 34) String iban) {
        if (!accountService.delete(iban))
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/summary/{iban}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE) // Spring REST annotation //
    public ResponseEntity getSummary(@PathVariable @Size(min = 5, max = 34) String iban) {
        if (accountService.getSummary(iban) == null)
            throw new ErrorResponseException(HttpStatus.NOT_FOUND);
        return new ResponseEntity(accountService.getSummary(iban), HttpStatus.OK);
    }

    @PostMapping(value = "/update/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE) // Spring REST annotation //
    public ResponseEntity updateAccount(@RequestBody @Valid Account account) {
        if (!accountService.update(account))
            throw new ErrorResponseException(HttpStatus.NOT_FOUND);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException( MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body("Validation failed: not valid properties" + exception.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleConstraintViolationException(ConstraintViolationException exception) {
        return ResponseEntity.badRequest().body("Validation failed: " + exception.getMessage());
    }

}

