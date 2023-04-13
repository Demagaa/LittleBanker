package com.banker.services;

import com.banker.models.Customer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Aleksei Chursin
 */
public class CustomerServiceTest {
    private int id;

    @Test
    public void testDelete() {
        CustomerService customerService = new CustomerService();
        assertEquals(true, customerService.delete(id));
    }

    @Test
    public void testSaveAndGetSummary() {
        CustomerService customerService = new CustomerService();
        Customer customer = new Customer();
        customer.setName("Ivan");
        customer.setSurname("Ivanov");
        try {
            customerService.save(customer);
            id = customerService.getId(customer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(customer.getName(), customerService.getSummary(id).getName());
        assertEquals(customer.getSurname(), customerService.getSummary(id).getSurname());
    }

    @Test
    public void testUpdate() {
        CustomerService customerService = new CustomerService();
        Customer customer = new Customer();
        customer.setName("Alex");
        customer.setSurname("Ivanov");
        try {
            assertTrue(customerService.update(customer, id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
