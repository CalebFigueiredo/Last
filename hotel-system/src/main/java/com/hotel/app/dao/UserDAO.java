package com.hotel.app.dao;

import com.hotel.app.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class UserDAO {

    private EntityManager entityManager;

    // Construtor para injetar o EntityManager
    public UserDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Adiciona um novo usuário ao banco de dados.
     * O ID do usuário será gerado pelo banco de dados e atualizado no objeto User após a persistência.
     * A transação é gerenciada externamente.
     *
     * @param user O objeto User a ser adicionado.
     */
    public void addUser(User user) {
        entityManager.persist(user);
        System.out.println("Usuário adicionado com sucesso! ID: " + user.getUserId());
    }

    /**
     * Busca um usuário no banco de dados pelo seu email único.
     * A transação é gerenciada externamente.
     *
     * @param email O email do usuário a ser buscado.
     * @return O objeto User correspondente ao email, ou null se não for encontrado.
     */
    public User getUserByEmail(String email) {
        try {
            TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // Retorna null se nenhum usuário for encontrado com o email
        }
    }

    /**
     * Retorna uma lista de todos os usuários cadastrados no banco de dados.
     * A transação é gerenciada externamente.
     *
     * @return Uma {@link List} de objetos {@link User}. Pode ser vazia se não houver usuários.
     */
    public List<User> getAllUsers() {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    /**
     * Atualiza as informações de um utilizador existente no banco de dados.
     * A transação é gerenciada externamente.
     *
     * @param user O objeto {@link User} com as informações atualizadas.
     * @return true se o utilizador foi atualizado, false caso contrário.
     */
    public boolean updateUser(User user) {
        // O método merge retorna a entidade gerenciada, que pode ser uma cópia do que foi passado.
        // É importante usar o retorno para futuras operações nesta transação.
        entityManager.merge(user);
        return true; // Supondo que merge sempre retorna true se sem exceção
    }


    /**
     * Busca um utilizador pelo telefone.
     * @param phone O telefone do utilizador.
     * @return O User encontrado, ou null.
     */
    public User getUserByPhone(String phone) {
        try {
            TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.phone = :phone", User.class);
            query.setParameter("phone", phone);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    /**
     * Deleta um usuário do banco de dados pelo seu ID.
     * A transação é gerenciada externamente.
     *
     * @param userId O ID do usuário a ser deletado.
     * @return true se o usuário foi deletado, false caso contrário.
     */
    public boolean deleteUser(Integer userId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            entityManager.remove(user);
            return true;
        }
        return false;
    }
}