package org.sayner.student.jdbcdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;

@SpringBootApplication
public class JdbcDemoApplication {
    private static final Logger logger = LoggerFactory.getLogger(JdbcDemoApplication.class);
    private static final String DB_URL = "jdbc:postgresql://25.33.103.29:6543/postgres";
    private static final String USER = "docker";
    private static final String PASS = "secret";

    public static void main(String[] args) {
        logger.info("Starting application");
        createDatabase();
        SpringApplication.run(JdbcDemoApplication.class, args);
    }

    protected static void createDatabase() {
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
                    .getConnection(DB_URL, USER, PASS);

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
