package com.hotel.app.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Classe utilitária para gerenciar o ciclo de vida do EntityManagerFactory e EntityManager.
 * Segue o padrão Singleton para o EntityManagerFactory, que é um recurso pesado.
 */
public class JpaUtil {

    // O EntityManagerFactory é um recurso pesado e deve ser criado apenas uma vez
    private static EntityManagerFactory entityManagerFactory;

    /**
     * Inicializa o EntityManagerFactory.
     * Deve ser chamado uma única vez no início da aplicação.
     */
    public static void init() {
        if (entityManagerFactory == null) {
            try {
                entityManagerFactory = Persistence.createEntityManagerFactory("hotel_management_unit");
                System.out.println("EntityManagerFactory inicializado com sucesso.");
            } catch (Exception e) {
                System.err.println("Erro ao inicializar o EntityManagerFactory: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Falha na inicialização do JPA.", e);
            }
        }
    }

    /**
     * Retorna uma nova instância de EntityManager.
     * EntityManager é leve e deve ser obtido e fechado para cada operação de banco de dados ou transação.
     *
     * @return Uma instância de EntityManager.
     */
    public static EntityManager getEntityManager() {
        if (entityManagerFactory == null) {
            throw new IllegalStateException("EntityManagerFactory não foi inicializado. Chame JpaUtil.init() primeiro.");
        }
        return entityManagerFactory.createEntityManager();
    }

    /**
     * Fecha o EntityManagerFactory quando a aplicação é encerrada.
     * Libera todos os recursos.
     */
    public static void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
            System.out.println("EntityManagerFactory fechado.");
        }
    }
}