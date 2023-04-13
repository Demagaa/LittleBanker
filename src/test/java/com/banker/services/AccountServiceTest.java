package com.banker.services;

import com.banker.models.Account;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Aleksei Chursin
 */


public class AccountServiceTest {

    @Test
    public void testDelete() {
        AccountService accountService = new AccountService();
        assertEquals(true, accountService.delete("CZ2345678901234567890124"));
    }

    @Test
    public void testSaveAndGetSummary() {
        AccountService accountService = new AccountService();
        Account account = new Account();
        account.setIban("CZ2345678901234567890121");
        account.setBalance(BigDecimal.valueOf(123.00));
        account.setCurrency("eur");
        account.setCustomerId(1);
        accountService.save(account);
        Account summary = accountService.getSummary("123");
        assertEquals("123", summary.getIban());
        assertEquals(123, summary.getBalance().intValue());
        assertEquals("eur", summary.getCurrency());
        assertEquals(1, summary.getCustomerId());
    }

    @Test
    public void testUpdate() {
        AccountService accountService = new AccountService();
        Account account = new Account();
        account.setIban("CZ2345678901234567890124");
        account.setBalance(BigDecimal.valueOf(123.00));
        account.setCurrency("eur");
        account.setCustomerId(2);
        assertTrue(accountService.update(account));
    }

}
