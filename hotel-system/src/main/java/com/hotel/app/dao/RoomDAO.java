package com.hotel.app.dao;

import com.hotel.app.model.Room;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * Objeto de Acesso a Dados (DAO) para a entidade {@link Room}.
 * Responsável por todas as operações de persistência relacionadas aos quartos no banco de dados.
 */
public class RoomDAO {

    private EntityManager entityManager; // Variável de instância para o EntityManager

    // Construtor para injetar o EntityManager
    public RoomDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Adiciona um novo quarto ao banco de dados.
     * O ID do quarto será gerado pelo banco de dados e atualizado no objeto Room após a persistência.
     * A transação é gerenciada externamente.
     *
     * @param room O objeto {@link Room} a ser adicionado.
     */
    public void addRoom(Room room) {
        entityManager.persist(room);
        System.out.println("Quarto adicionado com sucesso! ID: " + room.getRoomId());
    }

    /**
     * Busca um quarto no banco de dados pelo seu ID único.
     * A transação é gerenciada externamente.
     *
     * @param roomId O ID do quarto a ser buscado.
     * @return O objeto {@link Room} correspondente ao ID, ou null se não for encontrado.
     */
    public Room getRoomById(Integer roomId) {
        return entityManager.find(Room.class, roomId);
    }

    /**
     * Busca um quarto no banco de dados pelo seu número de quarto.
     * A transação é gerenciada externamente.
     *
     * @param roomNumber O número do quarto a ser buscado.
     * @return O objeto {@link Room} correspondente ao número, ou null se não for encontrado.
     */
    public Room getRoomByNumber(String roomNumber) {
        try {
            TypedQuery<Room> query = entityManager.createQuery("SELECT r FROM Room r WHERE r.roomNumber = :roomNumber", Room.class);
            query.setParameter("roomNumber", roomNumber);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // Retorna null se nenhum quarto for encontrado
        }
    }

    /**
     * Retorna uma lista de todos os quartos cadastrados no banco de dados.
     * A transação é gerenciada externamente.
     *
     * @return Uma {@link List} de objetos {@link Room}. Pode ser vazia se não houver quartos.
     */
    public List<Room> getAllRooms() {
        TypedQuery<Room> query = entityManager.createQuery("SELECT r FROM Room r", Room.class);
        return query.getResultList();
    }

    /**
     * Retorna uma lista de quartos disponíveis.
     * A transação é gerenciada externamente.
     *
     * @return Uma {@link List} de objetos {@link Room} que estão disponíveis.
     */
    public List<Room> getAvailableRooms() {
        TypedQuery<Room> query = entityManager.createQuery("SELECT r FROM Room r WHERE r.isAvailable = true", Room.class);
        return query.getResultList();
    }

    /**
     * Atualiza as informações de um quarto existente no banco de dados.
     * A transação é gerenciada externamente.
     *
     * @param room O objeto {@link Room} com as informações atualizadas.
     * @return true se o quarto foi atualizado, false caso contrário.
     */
    public boolean updateRoom(Room room) {
        entityManager.merge(room);
        return true; // Supondo que merge sempre retorna true se sem exceção
    }

    /**
     * Deleta um quarto do banco de dados pelo seu ID.
     * A transação é gerenciada externamente.
     *
     * @param roomId O ID do quarto a ser deletado.
     * @return true se o quarto foi deletado, false caso contrário.
     */
    public boolean deleteRoom(Integer roomId) {
        Room room = entityManager.find(Room.class, roomId);
        if (room != null) {
            entityManager.remove(room);
            return true;
        }
        return false;
    }
}