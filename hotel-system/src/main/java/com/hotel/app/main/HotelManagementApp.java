package com.hotel.app.main;

import com.hotel.app.config.DatabaseConnection; // Importa a classe de conexão
//import com.hotel.app.dao.UserDAO;             // IMPORTANTE: Importa a classe UserDAO
//import com.hotel.app.model.User;             // IMPORTANTE: Importa a classe User

import java.sql.Connection;
import java.sql.SQLException;

public class HotelManagementApp {

    public static void main(String[] args) {
        System.out.println("Iniciando o Sistema de Gestão Hoteleira...");

        // Testando a conexão inicial (opcional, já testamos antes)
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Conexão com o banco de dados estabelecida com sucesso!");

                /*
                // --- INÍCIO DO CÓDIGO DE TESTE DA USERDAO ---

                UserDAO userDAO = new UserDAO();

                // 1. Adicionar um novo usuário
                System.out.println("\n--- Adicionando novo usuário ---");
                User newUser = new User("Joao Silva", "joao.silva@example.com", "senha123");
                userDAO.addUser(newUser); // O ID será setado no objeto newUser após a inserção

                // 2. Tentar encontrar o usuário pelo ID gerado
                System.out.println("\n--- Buscando usuário por ID ---");
                User foundUserById = userDAO.getUserById(newUser.getId());
                if (foundUserById != null) {
                    System.out.println("Usuário encontrado por ID: " + foundUserById);
                } else {
                    System.out.println("Usuário não encontrado por ID " + newUser.getId());
                }

                // 3. Tentar encontrar o usuário pelo email
                System.out.println("\n--- Buscando usuário por Email ---");
                User foundUserByEmail = userDAO.getUserByEmail("joao.silva@example.com");
                if (foundUserByEmail != null) {
                    System.out.println("Usuário encontrado por Email: " + foundUserByEmail);
                } else {
                    System.out.println("Usuário com email joao.silva@example.com não encontrado.");
                }

                // 4. Listar todos os usuários (deve incluir o novo)
                System.out.println("\n--- Listando todos os usuários ---");
                List<User> allUsers = userDAO.getAllUsers();
                if (allUsers.isEmpty()) {
                    System.out.println("Nenhum usuário no banco de dados.");
                } else {
                    for (User user : allUsers) {
                        System.out.println(user);
                    }
                }

                // --- FIM DO CÓDIGO DE TESTE DA USERDAO ---
                * */

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


/*Aplicar depois de usar o UserDAO*/
/*
package com.hotel.app.main;

import com.hotel.app.config.DatabaseConnection;
import com.hotel.app.dao.UserDAO;
import com.hotel.app.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List; // Certifique-se de que esta importação também está presente

public class HotelManagementApp {

    public static void main(String[] args) {
        System.out.println("Iniciando o Sistema de Gestão Hoteleira...");

        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Conexão com o banco de dados estabelecida com sucesso!");

                // --- INÍCIO DO CÓDIGO DE TESTE DA USERDAO ---

                UserDAO userDAO = new UserDAO();

                // 1. Adicionar um novo usuário
                System.out.println("\n--- Adicionando novo usuário ---");
                User newUser = new User("Joao Silva", "joao.silva@example.com", "senha123");
                userDAO.addUser(newUser);

                // 2. Tentar encontrar o usuário pelo ID gerado
                System.out.println("\n--- Buscando usuário por ID ---");
                User foundUserById = userDAO.getUserById(newUser.getId());
                if (foundUserById != null) {
                    System.out.println("Usuário encontrado por ID: " + foundUserById);
                } else {
                    System.out.println("Usuário não encontrado por ID " + newUser.getId());
                }

                // 3. Tentar encontrar o usuário pelo email
                System.out.println("\n--- Buscando usuário por Email ---");
                User foundUserByEmail = userDAO.getUserByEmail("joao.silva@example.com");
                if (foundUserByEmail != null) {
                    System.out.println("Usuário encontrado por Email: " + foundUserByEmail);
                } else {
                    System.out.println("Usuário com email joao.silva@example.com não encontrado.");
                }

                // 4. Listar todos os usuários (deve incluir o novo)
                System.out.println("\n--- Listando todos os usuários ---");
                List<User> allUsers = userDAO.getAllUsers();
                if (allUsers.isEmpty()) {
                    System.out.println("Nenhum usuário no banco de dados.");
                } else {
                    for (User user : allUsers) {
                        System.out.println(user);
                    }
                }

                // --- FIM DO CÓDIGO DE TESTE DA USERDAO ---

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