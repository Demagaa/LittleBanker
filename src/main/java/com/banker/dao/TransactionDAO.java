package com.banker.dao;

import com.banker.models.Account;
import com.banker.models.Transfer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Aleksei Chursin
 */
@Component
public class TransactionDAO {

    static DbUtil util = new DbUtil();
    private static final String URL = util.getConnectionUrl();
    private static final String USERNAME = util.getUserName();
    private static final String PASSWORD = util.getPassword();

    private static Connection connection;

    // Connecting to database //
    static {
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


    // Methods for working with database data //
    public BigDecimal transferCredits(String debtorIBAN, String creditorIBAN, BigDecimal amount, String message) {
        double modifier = 1;
        AccountDAO dao = new AccountDAO();
        Account debtorAccount = dao.getSummary(debtorIBAN);
        Account creditorAccount = dao.getSummary(creditorIBAN);
        if (debtorAccount.getBalance().compareTo(amount) <= -1) {
            throw new RuntimeException("Insufficient balance");
        }

        if (debtorAccount.getCurrency() != creditorAccount.getCurrency()) {
            if (!debtorAccount.getCurrency().equals("eur")) {
                modifier = 0.04;
            } else modifier = 23.5;
        }

        debtorAccount.setBalance(debtorAccount.getBalance().subtract(amount));
        BigDecimal converted = amount.multiply(BigDecimal.valueOf(modifier));
        creditorAccount.setBalance(creditorAccount.getBalance().add(converted));
        dao.setBalance(debtorAccount.getBalance(), debtorIBAN);
        dao.setBalance(creditorAccount.getBalance(), creditorIBAN);

        Transfer transfer = new Transfer(debtorIBAN, creditorIBAN, amount, message);
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO transfer " +
                            "(debtoriban, creditoriban, amount, message) VALUES(?, ?, ?, ?)");

            preparedStatement.setString(1, transfer.getDebtorIban());
            preparedStatement.setString(2, transfer.getCreditorIban());
            preparedStatement.setBigDecimal(3, transfer.getAmount());
            preparedStatement.setString(4, transfer.getMessage());

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return BigDecimal.valueOf(-1);
        }
        return debtorAccount.getBalance();
    }

    public List<Transfer> viewHistory(String debtorIBAN) {
        List<Transfer> list = new ArrayList<>();
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM transfer WHERE debtoriban=?");

            preparedStatement.setString(1, debtorIBAN);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new Transfer(debtorIBAN, resultSet.getString(3), resultSet.getBigDecimal(4), resultSet.getString(5)));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return list;
    }

    public List<Transfer> search(BigDecimal amount) {
        List<Transfer> list = new ArrayList<>();
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM transfer WHERE amount=?");

            preparedStatement.setBigDecimal(1, amount);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new Transfer(resultSet.getString(2), resultSet.getString(3), amount, resultSet.getString(5)));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    public List<Transfer> search(String message) {
        List<Transfer> list = new ArrayList<>();
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM transfer WHERE message=?");

            preparedStatement.setString(1, message);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new Transfer(resultSet.getString(2), resultSet.getString(3), resultSet.getBigDecimal(4), message));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    // Assistant function for parsing //
    public Properties parsePropertiesString(String s) {
        final Properties p = new Properties();
        try {
            p.load(new StringReader(s));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return p;
    }


}
