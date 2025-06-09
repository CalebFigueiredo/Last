package com.hotel.app.service;

import com.hotel.app.dao.UserDAO; // Certifique-se de ter essa classe no pacote 'dao'
import com.hotel.app.model.User;
import com.hotel.app.model.Role;
import jakarta.persistence.EntityManager; // Importe EntityManager

import java.time.LocalDate;
import java.util.List;

public class UserService {

    private UserDAO userDAO;

    public UserService(EntityManager entityManager) {
        this.userDAO = new UserDAO(entityManager);
    }

    /**
     * Realiza o cadastro de um novo usuário com um cargo específico.
     *
     * @param fullName O nome completo do usuário.
     * @param email O email do usuário (será o username).
     * @param phone O telefone do usuário.
     * @param birthday A data de nascimento do usuário.
     * @param password A senha do usuário.
     * @param role O cargo/hierarquia do usuário (GUEST, EMPLOYEE, ADMINISTRATOR).
     * @return O objeto User persistido (com o ID gerado), ou null se o cadastro falhar.
     */
    public User registerUser(String fullName, String email, String phone, LocalDate birthday, String password, Role role) {
        // Verifica se já existe um usuário com o mesmo e-mail ou telefone
        if (userDAO.getUserByEmail(email) != null) {
            System.out.println("Erro: Já existe um usuário cadastrado com este e-mail.");
            return null;
        }
        //  adicionar uma verificação de telefone aqui também
         //if (userDAO.getUserByPhone(phone) != null) { ... }

        User newUser = new User(fullName, email, phone, birthday, role, password);

        try {

            User persistedUser = userDAO.addUser(newUser);
            if (persistedUser != null) {
                System.out.println("Usuário " + persistedUser.getFullName() + " cadastrado com sucesso! ID: " + persistedUser.getUserId());
            } else {
                System.out.println("Falha ao cadastrar usuário. O DAO retornou nulo após a tentativa de adição.");
            }
            return persistedUser;
        } catch (Exception e) {
            System.err.println("Erro ao registrar usuário: " + e.getMessage());
            // Logar a stack trace para depuração detalhada
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Realiza o cadastro de um novo cliente (Role.GUEST) por padrão.
     * Este é um método sobrecarregado para facilitar o registro de clientes.
     *
     * @param fullName O nome completo do cliente.
     * @param email O email do cliente.
     * @param phone O telefone do cliente.
     * @param birthday A data de nascimento do cliente.
     * @param password A senha do cliente.
     * @return O objeto User persistido, ou null se o cadastro falhar.
     */
    public User registerUser(String fullName, String email, String phone, LocalDate birthday, String password) {
        return registerUser(fullName, email, phone, birthday, password, Role.GUEST);
    }


    /**
     * Realiza a autenticação de um usuário.
     * @param email O email do usuário.
     * @param password A senha do usuário.
     * @return O objeto User se a autenticação for bem-sucedida, ou null caso contrário.
     */
    public User authenticateUser(String email, String password) {
        User user = userDAO.getUserByEmail(email);
        // ATENÇÃO: Em um sistema real, a senha NUNCA deve ser armazenada como texto puro.
        // Use uma função de hash (ex: BCrypt) para comparar senhas.
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    /**
     * Busca um usuário pelo email.
     * @param email O email do usuário a ser buscado.
     * @return O objeto User encontrado, ou null se não for encontrado.
     */
    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    /**
     * Atualiza os dados de um usuário existente.
     * @param user O objeto User com os dados atualizados.
     * @return O objeto User atualizado, ou null em caso de falha.
    */
    public User updateUser(User user) {
        try {
            return userDAO.updateUser(user);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
            return null;
        }
    }

    /**
     * Deleta um usuário pelo seu ID.
     * @param userId O ID do usuário a ser deletado.
     */
    public void deleteUser(Integer userId) {
        try {
            userDAO.deleteUser(userId);
            System.out.println("Usuário com ID " + userId + " deletado com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao deletar usuário: " + e.getMessage());
            e.printStackTrace(); // Para depuração
        }
    }

    /**
     * Obtém uma lista de todos os usuários cadastrados.
     * @return Uma lista de objetos User.
     */
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
}