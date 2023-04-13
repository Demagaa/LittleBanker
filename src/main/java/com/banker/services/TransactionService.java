package com.banker.services;

import com.banker.models.Account;
import com.banker.models.Transfer;
import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aleksei Chursin
 */
@Component
@Transactional
public class TransactionService {

    static DbUtilDAO util = new DbUtilDAO();
    private static final String URL = util.getConnectionUrl();
    private static final String USERNAME = util.getUserName();
    private static final String PASSWORD = util.getPassword();

    private static Connection connection;

    // Connecting to database //
    private void setConnection(){
        try {
            Class.forName(util.getDbDriver());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void closeConnection(){
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // Methods for working with database data //
    public BigDecimal transferCredits(Transfer transfer) throws Exception {
        double modifier = 1;
        AccountService dao = new AccountService();
        Account debtorAccount = dao.getSummary(transfer.getDebtorIban());
        Account creditorAccount = dao.getSummary(transfer.getCreditorIban());
        if (debtorAccount == null || creditorAccount == null)
            throw  new Exception("Wrong IBAN");

        if (debtorAccount.getBalance().compareTo(transfer.getAmount()) <= -1) {
            throw new RuntimeException("Insufficient balance");
        }

        if (debtorAccount.getCurrency().equals(creditorAccount.getCurrency())) {
            if (!debtorAccount.getCurrency().equals("eur")) {
                modifier = 0.04;
            } else modifier = 23.5;
        }

        debtorAccount.setBalance(debtorAccount.getBalance().subtract(transfer.getAmount()));
        BigDecimal converted = transfer.getAmount().multiply(BigDecimal.valueOf(modifier));
        creditorAccount.setBalance(creditorAccount.getBalance().add(converted));
        dao.setBalance(debtorAccount.getBalance(), transfer.getDebtorIban());
        dao.setBalance(creditorAccount.getBalance(), transfer.getCreditorIban());
;
        try {
            setConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO transfer " +
                            "(debtoriban, creditoriban, amount, message) VALUES(?, ?, ?, ?)");

            preparedStatement.setString(1, transfer.getDebtorIban());
            preparedStatement.setString(2, transfer.getCreditorIban());
            preparedStatement.setBigDecimal(3, transfer.getAmount());
            preparedStatement.setString(4, transfer.getMessage());
            preparedStatement.executeUpdate();
            closeConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw  new Exception("Internal error");
        }
        return debtorAccount.getBalance();
    }

    public List<Transfer> viewHistory(String debtorIBAN, int page, int size) {
        List<Transfer> list = new ArrayList<>();
        try {
            setConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM transfer WHERE debtoriban=?  LIMIT ?,? ");

            preparedStatement.setString(1, debtorIBAN);
            preparedStatement.setInt(2, page);
            preparedStatement.setInt(3, size);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new Transfer(debtorIBAN, resultSet.getString(3), resultSet.getBigDecimal(4), resultSet.getString(5)));
            }
            closeConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return list;
    }

    public List<Transfer> search(BigDecimal amount, int page, int size) {
        List<Transfer> list = new ArrayList<>();
        try {
            setConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM transfer WHERE amount=? LIMIT ?,? ");

            preparedStatement.setBigDecimal(1, amount);
            preparedStatement.setInt(2, page);
            preparedStatement.setInt(3, size);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new Transfer(resultSet.getString(2), resultSet.getString(3), amount, resultSet.getString(5)));
            }
            closeConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    public List<Transfer> search(String message, int page, int size) {
        List<Transfer> list = new ArrayList<>();
        try {
            setConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM transfer WHERE message=? LIMIT ?,? ");

            preparedStatement.setString(1, message);
            preparedStatement.setInt(2, page);
            preparedStatement.setInt(3, size);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new Transfer(resultSet.getString(2), resultSet.getString(3), resultSet.getBigDecimal(4), message));
            }
            closeConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }
}
