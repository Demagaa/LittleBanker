package com.banker.controllers;

import com.banker.dao.TransactionDAO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

/**
 * @author Aleksei Chursin
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionDAO transactionDAO; // Data access object //

    public TransactionController(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @PostMapping(value = "/", consumes = {"text/plain"}, produces = {"text/plain"}) // Spring REST annotation //
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String transferCredits(@RequestBody String r) {
        Properties properties = transactionDAO.parsePropertiesString(r); // Parsing string data //

        return "Remaining balance: " + (transactionDAO.transferCredits(properties.getProperty("debtoriban"),
                properties.getProperty("creditoriban"),
                BigDecimal.valueOf(Double.parseDouble(properties.getProperty("amount"))),
                properties.getProperty("message")));
    }

    @GetMapping("/history/") // Spring REST annotation //
    @ResponseStatus(HttpStatus.OK)
    public String viewHistoryByIban(@RequestParam String iban) {
        List list = transactionDAO.viewHistory(iban);
        String response = "";
        for (int i = 0; i < list.size(); i++)
            response += list.get(i).toString() + "\n" + "\n";
        return response;


    }

    @GetMapping("/search/message/") // Spring REST annotation //
    @ResponseStatus(HttpStatus.OK)
    public String searchByMessage(@RequestParam String message) {
        List list = transactionDAO.search(message);
        String response = "";
        for (int i = 0; i < list.size(); i++)
            response += list.get(i).toString() + "\n" + "\n";
        return response;
    }


    @GetMapping("/search/amount/") // Spring REST annotation //
    @ResponseStatus(HttpStatus.OK)
    public String searchByMessage(@RequestParam BigDecimal amount) {
        List list = transactionDAO.search(amount);
        String response = "";
        for (int i = 0; i < list.size(); i++)
            response += list.get(i).toString() + "\n" + "\n";
        return response;
    }
}
