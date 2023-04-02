package com.banker.controllers;

import com.banker.dao.CustomerDAO;
import com.banker.models.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;

import java.util.Properties;

/**
 * @author Aleksei Chursin
 */

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerDAO customerDAO; // Data access object //

    public CustomerController(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @PostMapping(value = "/", consumes = {"text/plain"}, produces = {"text/plain"}) // Spring REST annotation //
    @ResponseStatus(HttpStatus.CREATED)
    public String createCustomer(@RequestBody String r) {
        Properties properties = customerDAO.parsePropertiesString(r); // Parsing string data //
        Customer newCustomer = new Customer();
        newCustomer.setName(properties.getProperty("name"));
        newCustomer.setSurname(properties.getProperty("surname"));
        if (customerDAO.save(newCustomer) == null || customerDAO.getId(newCustomer) == -1)
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST);
        return "Customer created under unique identifier: " + customerDAO.getId(newCustomer);
    }

    @DeleteMapping("/delete/") // Spring REST annotation //
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@RequestParam int id) {
        if (!customerDAO.delete(id))
            throw new ErrorResponseException(HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping(value = "/update/", consumes = {"text/plain"}, produces = {"text/plain"}) // Spring REST annotation //
    @ResponseStatus(HttpStatus.CREATED)
    public void updateCustomer(@RequestBody String r) {
        Properties properties = customerDAO.parsePropertiesString(r); // Parsing string data //
        Customer updatedCustomer = new Customer();
        updatedCustomer.setName(properties.getProperty("name"));
        updatedCustomer.setSurname(properties.getProperty("surname"));
        if (!customerDAO.update(updatedCustomer, Integer.parseInt(properties.getProperty("id"))))
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/summary/") // Spring REST annotation //
    @ResponseStatus(HttpStatus.OK)
    public String getSummary(@RequestParam int id) {
        if (customerDAO.getSummary(id) == null)
            throw new ErrorResponseException(HttpStatus.NOT_FOUND);
        return customerDAO.getSummary(id).toString();
    }
}
