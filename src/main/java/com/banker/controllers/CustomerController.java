package com.banker.controllers;

import com.banker.dao.CustomerService;
import com.banker.models.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aleksei Chursin
 */

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService; // Data access object //

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE) // Spring REST annotation //
    public ResponseEntity createCustomer(@RequestBody Customer newCustomer) {
        if (customerService.save(newCustomer) == null || customerService.getId(newCustomer) == -1)
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST);
        return new ResponseEntity("Customer created under unique identifier: " + customerService.getId(newCustomer), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}") // Spring REST annotation //
    public ResponseEntity deleteCustomer(@PathVariable int id) {
        if (!customerService.delete(id))
            throw new ErrorResponseException(HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/update/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE) // Spring REST annotation //
    public ResponseEntity updateCustomer(@RequestBody Customer customer, @PathVariable int id) {
        if (!customerService.update(customer, id))
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/summary/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE) // Spring REST annotation //
    public ResponseEntity getSummary(@PathVariable int id) {
        if (customerService.getSummary(id) == null)
            throw new ErrorResponseException(HttpStatus.NOT_FOUND);
        return new ResponseEntity(customerService.getSummary(id), HttpStatus.OK);
    }
}
