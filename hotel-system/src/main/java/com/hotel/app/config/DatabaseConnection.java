package com.hotel.app.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DatabaseConnection {

    // Detalhes da conexão com o banco de dados PostgreSQL no Docker
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/hoteldb";
    private static final String DB_USER = "caleb"; // Seu usuário do PostgreSQL
    private static final String DB_PASSWORD = "caleb28"; // Sua senha do PostgreSQL (ajuste se mudou)

    public static void main(String[] args) {
        System.out.println("Attempting to connect to the database...");

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Connection to PostgreSQL successful!");

            // Exemplo: Consultar as tabelas para confirmar que existem
            System.out.println("\nListing tables from 'hoteldb':");
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'");
                while (resultSet.next()) {
                    System.out.println(" - " + resultSet.getString("table_name"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error connecting to the database or executing query:");
            e.printStackTrace();

        }
    }

    // Método para obter uma conexão (vamos usar isso mais tarde)
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}