package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final String DB_URL = "jdbc:sqlite:dataBase.db";

    public void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            
            String createTableQuery = "CREATE TABLE IF NOT EXISTS USER" +
                                      "(ID INTEGER PRIMARY KEY      NOT NULL," +
                                      "USERACCOUNT         TEXT     NOT NULL," +
                                      "PASSWORD            TEXT     NOT NULL," +
                                      "DEFAULTPASSWORD     TEXT     NOT NULL)" ;
            statement.executeUpdate(createTableQuery);//创建USER表

            createTableQuery = "CREATE TABLE IF NOT EXISTS ADMIN" + 
                               "(ID INTEGER PRIMARY KEY      NOT NULL," +
                               "ADMINACCOUNT        TEXT     NOT NULL," +
                               "PASSWORD            TEXT     NOT NULL)" ;
            statement.executeUpdate(createTableQuery);//创建ADMIN表

            createTableQuery = "CREATE TABLE IF NOT EXISTS GOODS" +
                               "(ID INTEGER PRIMARY KEY      NOT NULL," +
                               "NAME                TEXT     NOT NULL," +
                               "PRICE               REAL     NOT NULL," +
                               "QUANTITY            INTEGER  NOT NULL)" ;
            statement.executeUpdate(createTableQuery);//创建GOODS表

            createTableQuery = "CREATE TABLE IF NOT EXISTS SHOPPINGCART"+
                               "(USERACCOUNT        TEXT KEY NOT NULL," +
                               "ID                  INTEGER  NOT NULL," +
                               "GOODSNAME           TEXT     NOT NULL," +
                               "PRICE               REAL     NOT NULL," +
                               "QUANTITY            INTEGER  NOT NULL)" ;
            statement.executeUpdate(createTableQuery);//创建SHOPPINGCART表

            createTableQuery = "CREATE TABLE IF NOT EXISTS SHOPHISTORY" +
                               "(TIME               TEXT KEY NOT NULL," +
                               "USERACCOUNT         TEXT KEY NOT NULL," +
                               "ID                  INTEGER  NOT NULL," +
                               "GOODSNAME           TEXT     NOT NULL," +
                               "PRICE               REAL     NOT NULL," +
                               "QUANTITY            INTEGER  NOT NULL)" ;
            statement.executeUpdate(createTableQuery);//创建SHOPHISTORY表

           // System.out.println("Database initialized successfully!");
        } catch (SQLException e) {
            System.out.println("Failed to initialize database: " + e.getMessage());
        }
    }
}
