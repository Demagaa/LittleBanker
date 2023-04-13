package com.banker.controllers;

import com.banker.config.SpringConfig;
import com.banker.models.Account;
import org.junit.After;;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * @author Aleksei Chursin
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class})
@WebAppConfiguration
public class AccountControllerTest {

    @Test
    public void testCreateAccount() {
        Account account = new Account();
        account.setIban("CZ123456789012345678901");
        account.setBalance(BigDecimal.valueOf(123.00));
        account.setCurrency("eur");
        account.setCustomerId(1);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Account> request = new HttpEntity<>(account, headers);

        ResponseEntity<Account> response = restTemplate.exchange("http://localhost:8080/accounts/", HttpMethod.POST, request, Account.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testGetSummary() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = "http://localhost:8080/accounts/summary/{id}";
        String id = "CZ123456789012345678901";

        ResponseEntity<Account> response = restTemplate.getForEntity(url,
                Account.class, id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdate() {
        Account account = new Account();
        account.setIban("CZ123456789012345678901");
        account.setBalance(BigDecimal.valueOf(500.00));
        account.setCurrency("czk");
        account.setCustomerId(2);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Account> request = new HttpEntity<>(account, headers);

        ResponseEntity<Account> response = restTemplate.exchange("http://localhost:8080/accounts/update/", HttpMethod.POST, request, Account.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @After
    public void testDelete() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = "http://localhost:8080/accounts/delete/CZ123456789012345678901";
        String id = "CZ123456789012345678901";

        ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }
}
