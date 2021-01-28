package org.sayner.student.jdbcdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.sql.*;
import java.util.*;

@SpringBootApplication
public class JdbcDemoApplication {
    private static final Logger logger = LoggerFactory.getLogger(JdbcDemoApplication.class);
    private static final String DB_URL = "DB_URL";
    private static final String DB_USER = "DB_USER";
    private static final String DB_PWD = "DB_PWD";

    public static void main(String[] args) {
        logger.info("Starting application");
        final Map<String, String> properties = readPropertiesFile();
        createDatabase(properties.get(DB_URL), properties.get(DB_USER), properties.get(DB_PWD));
        SpringApplication.run(JdbcDemoApplication.class, args);
    }

    private static Map<String, String> readPropertiesFile() {
        final ClassPathResource resource = new ClassPathResource("application.properties");
        final Properties properties = new Properties();
        try {
            logger.info("Trying to read properties file");
            properties.load(resource.getInputStream());
        } catch (IOException e) {
            logger.error("application.properties could not be open: {}", e.getMessage());
            e.printStackTrace();
        }
        Map<String, String> credentials = new HashMap<>();
        credentials.put(DB_URL, properties.getProperty(DB_URL));
        credentials.put(DB_USER, properties.getProperty(DB_USER));
        credentials.put(DB_PWD, properties.getProperty(DB_PWD));
        return credentials;
    }

    protected static void createDatabase(String DB_URL, String DB_USER, String DB_PWD) {
        logger.info("Testing connection to PostgreSQL JDBC");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }

        logger.info("PostgreSQL JDBC Driver successfully connected");
        Connection connection;

        try {
            connection = DriverManager
                    .getConnection(DB_URL, DB_USER, DB_PWD);

        } catch (SQLException e) {
            logger.error("Connection Failed");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            logger.info("You successfully connected to database now");
            logger.info("Execution of create database statement");
            executeQuery("CREATE DATABASE student", connection);
        } else {
            logger.warn("Failed to make connection to database");
        }
        logger.info("Successfully created database");
    }

    protected static void executeQuery(String query, Connection connection) {
        if (query == null || query.equals("")) {
            logger.error("Empty query execution attempt");
            return;
        }
        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error("Failed to prepare or execute query statement\nQuery: {}\nError: {}", query, e.getMessage());
            e.printStackTrace();
        }
    }
}
