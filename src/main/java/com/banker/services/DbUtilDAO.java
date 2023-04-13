package com.banker.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Aleksei Chursin
 */

// POJO class for database connection //
public class DbUtilDAO {
    public String getDbDriver() {
        return dbDriver;
    }

    public static String getConnectionUrl() {
        return connectionUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    private static String dbDriver;
    private static String connectionUrl;
    private static String userName;
    private static String password;

    public DbUtilDAO() {
        InputStream inputStream = DbUtilDAO.class.getClassLoader()
                .getResourceAsStream("application.properties");
        Properties properties = new Properties();
        if (properties != null) {
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            this.dbDriver = properties.getProperty("dbDriver");
            this.connectionUrl = properties.getProperty("connectionUrl");
            this.userName = properties.getProperty("userName");
            this.password = properties.getProperty("password");
        }
    }
}
