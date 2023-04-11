package com.banker.dao;

import com.banker.models.Customer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;

/**
 * @author Aleksei Chursin
 */
@Component
@Transactional
public class CustomerService {
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

    public Customer save(Customer customer) {
        try {
            setConnection();

            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO customer (name,surname) VALUES( ?, ?)");

            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getSurname());

            preparedStatement.executeUpdate();

            closeConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return customer;
    }
    public boolean delete(int id) {
        PreparedStatement preparedStatement;
        AccountService dao = new AccountService();
        dao.deleteByCustomerId(id);
        try {
            setConnection();

            preparedStatement = connection.prepareStatement("DELETE FROM customer WHERE id=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();

            closeConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean update (Customer updatedCustomer, int id) {
        try {
            setConnection();

            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE customer SET name=?, surname=? WHERE id=?");

            preparedStatement.setString(1, updatedCustomer.getName());
            preparedStatement.setString(2, updatedCustomer.getSurname());
            preparedStatement.setInt(3, id);

            preparedStatement.executeUpdate();

            closeConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public int getId(Customer customer){
        try {
            setConnection();

            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT id FROM customer WHERE name=? AND surname=? ORDER BY id DESC LIMIT 1");

            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getSurname());

            ResultSet resultSet = preparedStatement.executeQuery();

            closeConnection();
            if (resultSet.next())
                return resultSet.getInt(1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return -1;
        }
        return -1;
    }

    public Customer getSummary(int id) {
        Customer customer = null;
        try {
            setConnection();

            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT id, name, surname FROM customer WHERE id=?");

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            customer = new Customer();
            customer.setId(resultSet.getInt(1));
            customer.setName(resultSet.getString(2));
            customer.setSurname(resultSet.getString(3));

            closeConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }

        return customer;
    }
}
