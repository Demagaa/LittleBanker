package com.banker.controllers;

import com.banker.services.TransactionService;
import com.banker.models.Transfer;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.util.List;

/**
 * @author Aleksei Chursin
 */
@RestController
@RequestMapping("/transactions")
@Validated
public class TransactionController {

    private final TransactionService transactionService; // Data access object //

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE) // Spring REST annotation //
    public ResponseEntity transferCredits(@RequestBody @Valid Transfer newTransfer) {
        BigDecimal remainingBalance  = null;
        try {
            remainingBalance = transactionService.transferCredits(newTransfer);
        } catch (Exception e) {
            return new ResponseEntity("Something went wrong: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Remaining ballance: " + remainingBalance, HttpStatus.OK);
    }

    @GetMapping("/history/{iban}") // Spring REST annotation //
    public ResponseEntity viewHistoryByIban(@PathVariable @Size(min = 5, max = 34) String iban,
                                            @PositiveOrZero @RequestParam(defaultValue = "0") int page,
                                            @Positive @RequestParam(defaultValue = "3") int size) {
        return new ResponseEntity(transactionService.viewHistory(iban, page, size), HttpStatus.OK);
    }

    @GetMapping("/search/message/") // Spring REST annotation //
    public ResponseEntity searchByMessage(@RequestParam String message,
                                          @PositiveOrZero @RequestParam(defaultValue = "0") int page,
                                          @Positive @RequestParam(defaultValue = "3") int size) {
        List list = transactionService.search(message, page, size);
        if (list.isEmpty())
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        return new ResponseEntity(list, HttpStatus.OK);
    }


    @GetMapping("/search/amount/") // Spring REST annotation //
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity searchByMessage(@RequestParam @Positive BigDecimal amount,
                                          @PositiveOrZero @RequestParam(defaultValue = "0") int page,
                                          @Positive @RequestParam(defaultValue = "3") int size) {
        List list = transactionService.search(amount, page, size);
        if (list.isEmpty())
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        return new ResponseEntity(list, HttpStatus.OK);
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
