package com.hotel.app.service;

import com.hotel.app.dao.UserDAO;
import com.hotel.app.model.User;
import com.hotel.app.model.Role;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;

/**
 * Camada de serviço para operações relacionadas a usuários (clientes, funcionários, administradores).
 * Esta classe contém a lógica de negócios e gerência as operações de persistência via UserDAO.
 */
public class UserService {

    private UserDAO userDAO;


    public UserService(UserDAO userDAO) {this.userDAO = userDAO;}

    /**
     * Realiza o cadastro de um novo usuário.
     *
     * @param fullName O nome completo do usuário.
     * @param email O email do usuário (será o username).
     * @param phone O telefone do usuário.
     * @param birthday A data de nascimento do usuário.
     * @param password A senha do usuário.
     * @return O objeto User persistido, ou null se o cadastro falhar.
     */
    public User registerUser(String fullName, String email, String phone, LocalDate birthday, String password) {


        if (userDAO.getUserByEmail(email) != null) {
            System.out.println("Erro: Já existe um usuário cadastrado com este e-mail.");
            return null;
        }
        if (userDAO.getUserByPhone(phone) != null) { // Assumindo que você adicionou um getUserByPhone no UserDAO
            System.out.println("Erro: Já existe um usuário cadastrado com este telefone.");
            return null;
        }

        User newUser = new User(fullName, email, phone, birthday, Role.GUEST, password);
        try {
            userDAO.addUser(newUser);
            return newUser;
        } catch (Exception e) {
            System.err.println("Erro ao registrar usuário: " + e.getMessage());
            return null;
        }
    }

    /**
     * Realiza a autenticação de um usuário.
     * @param email O email do usuário.
     * @param password A senha do usuário.
     * @return O objeto User se a autenticação for bem-sucedida, ou null caso contrário.
     */
    public User authenticateUser(String email, String password) {
        User user = userDAO.getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }


    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    // Adicione outros métodos conforme necessário, por exemplo:
    // public boolean updateUserProfile(User user) { ... }
    // public List<User> getAllGuests() { ... }
}