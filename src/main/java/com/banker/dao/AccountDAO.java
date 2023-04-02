package com.banker.dao;

import com.banker.models.Account;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Properties;

/**
 * @author Aleksei Chursin
 */
@Component
public class AccountDAO {
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
    public Account save(Account account) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO account VALUES(?, ?, ?, ?)");

            preparedStatement.setString(1, account.getIban());
            preparedStatement.setInt(2, account.getCustomerId());
            preparedStatement.setBigDecimal(3, account.getBalance());
            preparedStatement.setString(4, account.getCurrency());

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return account;
    }

    public boolean delete(String iban) {
        PreparedStatement preparedStatement;

        try {
            preparedStatement = connection.prepareStatement("DELETE FROM transfer WHERE debtoriban=? OR creditoriban=?");

            preparedStatement.setString(1, iban);
            preparedStatement.setString(2, iban);

            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("DELETE FROM account WHERE iban=?");

            preparedStatement.setString(1, iban);

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean update(Account updatedAccount) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE account SET customerid=?, balance=?, currency=? WHERE iban=?");

            preparedStatement.setInt(1, updatedAccount.getCustomerId());
            preparedStatement.setBigDecimal(2, updatedAccount.getBalance());
            preparedStatement.setString(3, updatedAccount.getCurrency());
            preparedStatement.setString(4, updatedAccount.getIban());

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public void setBalance(BigDecimal balance, String iban) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE account SET balance=? WHERE iban=?");

            preparedStatement.setBigDecimal(1, balance);
            preparedStatement.setString(2, iban);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Account getSummary(String iban) {
        Account account = null;
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT customerid, balance, currency FROM account WHERE iban=?");

            preparedStatement.setString(1, iban);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                account = new Account();
                account.setIban(iban);
                account.setCustomerId(resultSet.getInt(1));
                account.setBalance(resultSet.getBigDecimal(2));
                account.setCurrency(resultSet.getString(3));
            } else return null;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }

        return account;
    }

    // Assistant function for delete operation //
    public void deleteByCustomerId(int customerId) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("DELETE FROM account WHERE customerid=?");

            preparedStatement.setInt(1, customerId);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
