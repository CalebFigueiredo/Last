package com.hotel.app.dao;

import com.hotel.app.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.EntityTransaction; // Importe EntityTransaction se for gerenciar transações aqui

import java.util.List;

public class UserDAO {

    private EntityManager entityManager;

    public UserDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Adiciona um novo usuário ao banco de dados.
     * O ID do usuário será gerado pelo banco de dados e atualizado no objeto User após a persistência.
     *
     * @param user O objeto User a ser adicionado.
     * @return O objeto User persistido, com o ID gerado pelo banco de dados. Retorna null em caso de erro.
     */
    public User addUser(User user) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            entityManager.persist(user);
            entityManager.flush();

            transaction.commit();
            System.out.println("Usuário adicionado com sucesso! ID: " + user.getUserId());
            return user;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Erro ao adicionar usuário ao banco de dados: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
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
        // Para operações de leitura, geralmente não é necessário uma transação explícita
        // a menos que você precise de um nível específico de isolamento ou controle transacional.
        // Se o EntityManager estiver gerenciado no contexto da sessão, pode funcionar sem begin/commit.
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    /**
     * Atualiza as informações de um utilizador existente no banco de dados.
     *
     * @param user O objeto {@link User} com as informações atualizadas.
     * @return O objeto User atualizado, ou null em caso de falha.
     */
    public User updateUser(User user) { // ALTERADO: Retorna User
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            User mergedUser = entityManager.merge(user); // O método merge retorna a entidade gerenciada
            transaction.commit();
            return mergedUser; // Retorna o objeto User atualizado
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Erro ao atualizar usuário no banco de dados: " + e.getMessage());
            e.printStackTrace();
            return null; // Retorna null em caso de falha
        }
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
     *
     * @param userId O ID do usuário a ser deletado.
     * @return true se o usuário foi deletado, false caso contrário.
     */
    public boolean deleteUser(Integer userId) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            User user = entityManager.find(User.class, userId);
            if (user != null) {
                entityManager.remove(user);
                transaction.commit();
                return true;
            } else {
                transaction.rollback(); // Nao encontrou, nao faz sentido commitar
                return false;
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Erro ao deletar usuário do banco de dados: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}