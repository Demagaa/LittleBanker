package com.banker.controllers;

import com.banker.services.CustomerService;
import com.banker.models.Customer;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aleksei Chursin
 */

@RestController
@RequestMapping("/customers")
@Validated
public class CustomerController {

    private final CustomerService customerService; // Data access object //

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE) // Spring REST annotation //
    public ResponseEntity createCustomer(@RequestBody @Valid Customer newCustomer) {
        int id;
        try {
            customerService.save(newCustomer);
            id = customerService.getId(newCustomer);
        } catch (Exception e) {
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Customer created under unique identifier: " + id, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}") // Spring REST annotation //
    public ResponseEntity deleteCustomer(@PathVariable @Positive int id) {
        if (!customerService.delete(id))
            throw new ErrorResponseException(HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/update/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE) // Spring REST annotation //
    public ResponseEntity updateCustomer(@RequestBody @Valid Customer customer, @PathVariable @Positive int id) {
        if (!customerService.update(customer, id))
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/summary/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE) // Spring REST annotation //
    public ResponseEntity getSummary(@PathVariable @Positive int id) {
        if (customerService.getSummary(id) == null)
            throw new ErrorResponseException(HttpStatus.NOT_FOUND);
        return new ResponseEntity(customerService.getSummary(id), HttpStatus.OK);
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
