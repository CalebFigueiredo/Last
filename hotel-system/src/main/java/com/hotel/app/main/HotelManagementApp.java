 /*
 package com.hotel.app.main;

import com.hotel.app.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class HotelManagementApp {

    public static void main(String[] args) {
        System.out.println("Iniciando o Sistema de Gestão Hoteleira...");

        // Testando a conexão inicial (opcional, já testamos antes)
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Conexão com o banco de dados estabelecida com sucesso!");

            } else {
                System.out.println("Falha ao estabelecer conexão com o banco de dados.");
            }
        } catch (SQLException e) {
            System.err.println("Erro durante a conexão ou operação com o banco de dados:");
            e.printStackTrace();
        }

        System.out.println("Sistema encerrado.");
    }
}

 * */