package com.hotel.app.dao;

import com.hotel.app.model.User;
import com.hotel.app.util.JpaUtil; // Importe o JpaUtil

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * Objeto de Acesso a Dados (DAO) para a entidade {@link User}.
 * Responsável por todas as operações de persistência relacionadas aos usuários no banco de dados,
 * utilizando JPA (Jakarta Persistence API) através do Hibernate.
 */
public class UserDAO {
    public static final EntityManager entityManager = JpaUtil.getEntityManager();
    /**
     * Adiciona um novo usuário ao banco de dados.
     * O ID do usuário será gerado pelo banco de dados e atualizado no objeto User após a persistência.
     *
     * @param user O objeto {@link User} a ser adicionado.
     * @throws Exception Se ocorrer um erro durante a persistência (ex: violação de unique constraint).
     */
    public void addUser(User user) throws Exception {
        try {
            entityManager.getTransaction().begin(); // Inicia uma transação
            entityManager.persist(user); // Persiste o objeto (insere no DB)
            entityManager.getTransaction().commit(); // Confirma a transação
            System.out.println("Usuário adicionado com sucesso! ID: " + user.getUserId());
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("Erro ao adicionar usuário: " + e.getMessage());
            throw e; // Relança a exceção
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    /**
     * Busca um usuário no banco de dados pelo seu ID único.
     *
     * @param userId O ID do usuário a ser buscado.
     * @return O objeto {@link User} correspondente ao ID, ou null se não for encontrado.
     */
    public User getUserById(Integer userId) {
        User user = null;
        try {
            // find é um método simples para buscar por chave primária
            user = entityManager.find(User.class, userId);
        } catch (Exception e) {
            System.err.println("Erro ao buscar usuário por ID: " + e.getMessage());
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
        return user;
    }

    /**
     * Busca um usuário no banco de dados pelo seu endereço de e-mail.
     *
     * @param email O endereço de e-mail do usuário a ser buscado.
     * @return O objeto {@link User} correspondente ao e-mail, ou null se não for encontrado.
     */
    public User getUserByEmail(String email) {
        User user = null;
        try {
            // JPQL (Jakarta Persistence Query Language) é usada para consultar entidades
            TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            user = query.getSingleResult(); // Retorna um único resultado, ou exceção se não houver
        } catch (NoResultException e) {
            // Nenhuma exceção para quando o email não for encontrado, retorna null
            user = null;
        } catch (Exception e) {
            System.err.println("Erro ao buscar usuário por e-mail: " + e.getMessage());
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
        return user;
    }

    /**
     * Retorna uma lista de todos os usuários cadastrados no banco de dados.
     *
     * @return Uma {@link List} de objetos {@link User}. Pode ser vazia se não houver usuários.
     */
    public List<User> getAllUsers() {
        EntityManager em = JpaUtil.getEntityManager();
        List<User> users = null;
        try {
            // JPQL para selecionar todas as entidades User
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
            users = query.getResultList(); // Retorna uma lista de resultados
        } catch (Exception e) {
            System.err.println("Erro ao listar todos os usuários: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return users;
    }

    /**
     * Atualiza as informações de um usuário existente no banco de dados.
     *
     * @param user O objeto {@link User} com as informações atualizadas.
     * @return true se o usuário foi atualizado, false caso contrário.
     * @throws Exception Se ocorrer um erro durante a atualização.
     */
    public boolean updateUser(User user) throws Exception {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(user); // 'merge' é usado para atualizar entidades
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Deleta um usuário do banco de dados pelo seu ID.
     *
     * @param userId O ID do usuário a ser deletado.
     * @return true se o usuário foi deletado, false caso contrário.
     * @throws Exception Se ocorrer um erro durante a deleção.
     */
    public boolean deleteUser(Integer userId) throws Exception {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, userId); // Primeiro, encontre a entidade
            if (user != null) {
                em.remove(user); // Em seguida, remova
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().commit(); // Se user for null, ainda comita a transação vazia
            return false; // Não encontrou o usuário para deletar
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Erro ao deletar usuário: " + e.getMessage());
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}