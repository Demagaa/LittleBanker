package com.banker.controllers;

import com.banker.config.SpringConfig;
import com.banker.models.Transfer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Aleksei Chursin
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class})
@WebAppConfiguration
public class TransactionControllerTest {

    @Test
    public void testTransferCredits() {
        Transfer transfer = new Transfer();
        transfer.setAmount(BigDecimal.valueOf(1));
        transfer.setDebtorIban("CZ7208000000192000145393");
        transfer.setCreditorIban("DE02430609674015142043SFTPOJH");
        transfer.setMessage("Hello");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Transfer> request = new HttpEntity<>(transfer, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/transactions/", HttpMethod.POST, request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testViewHistoryByIban() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:8080/transactions/history/CZ7208000000192000145393",
                List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
