package com.banker.controllers;

import com.banker.dao.TransactionService;
import com.banker.models.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Aleksei Chursin
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService; // Data access object //

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE) // Spring REST annotation //
    public ResponseEntity transferCredits(@RequestBody Transfer newTransfer) {
        BigDecimal remainingBalance  = transactionService.transferCredits(newTransfer);
        return new ResponseEntity("Remaining ballance: " + remainingBalance, HttpStatus.OK);
    }

    @GetMapping("/history/{iban}") // Spring REST annotation //
    public ResponseEntity viewHistoryByIban(@PathVariable String iban) {
        List list = transactionService.viewHistory(iban);
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/search/message/") // Spring REST annotation //
    public ResponseEntity searchByMessage(@RequestParam String message) {
        List list = transactionService.search(message);
        return new ResponseEntity(list, HttpStatus.OK);
    }


    @GetMapping("/search/amount/") // Spring REST annotation //
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity searchByMessage(@RequestParam BigDecimal amount) {
        List list = transactionService.search(amount);
        return new ResponseEntity(list, HttpStatus.OK);
    }
}
