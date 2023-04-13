package com.banker.controllers;

import com.banker.config.SpringConfig;
import com.banker.models.Customer;
import com.banker.services.CustomerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

/**
 * @author Aleksei Chursin
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class})
@WebAppConfiguration
public class CustomerControllerTest {
    private int id;

    @Test
    @Before
    public void testCreateCustomer() {
        CustomerService customerService = new CustomerService();
        Customer customer = new Customer();
        customer.setName("Andrew");
        customer.setSurname("Block");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Customer> request = new HttpEntity<>(customer, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/customers/", HttpMethod.POST, request, String.class);
        try {
            this.id = customerService.getId(customer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


    @Test
    public void testUpdate() {
        Customer customer = new Customer();
        customer.setName("Andrew");
        customer.setSurname("Block");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Customer> request = new HttpEntity<>(customer, headers);
        ResponseEntity<Customer> response = restTemplate.exchange("http://localhost:8080/customers/update/{id}", HttpMethod.POST, request, Customer.class, id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @After
    public void testDelete() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = "http://localhost:8080/customers/delete/" + this.id;


        ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

}
