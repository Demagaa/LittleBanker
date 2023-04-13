package com.banker.services;

import com.banker.models.Account;
import com.banker.models.Transfer;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Aleksei Chursin
 */
public class TransactionServiceTest {
    @Test
    public void testTransferCredits() {
        TransactionService transactionService = new TransactionService();
        AccountService dao = new AccountService();
        Transfer transfer = new Transfer();
        transfer.setAmount(BigDecimal.valueOf(1));
        transfer.setDebtorIban("CZ7208000000192000145393");
        transfer.setCreditorIban("DE02430609674015142043SFTPOJH");
        transfer.setMessage("Hello");
        BigDecimal remain;
        try {
            remain = transactionService.transferCredits(transfer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Account debtorAccount = dao.getSummary(transfer.getDebtorIban());

        assertEquals(debtorAccount.getBalance(), remain);
    }

    @Test
    public void testViewHistory() {
        TransactionService transactionService = new TransactionService();
        assertNotNull(transactionService.viewHistory("CZ7208000000192000145393", 0, 1));
    }

    @Test
    public void testSearchByAmount() {
        TransactionService transactionService = new TransactionService();
        assertNotNull(transactionService.search(BigDecimal.valueOf(1), 0, 1));
    }

}
