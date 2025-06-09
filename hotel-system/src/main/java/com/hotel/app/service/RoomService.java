// Exemplo de como seu RoomService.java deve ser
package com.hotel.app.service;

import com.hotel.app.model.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class RoomService {

    private EntityManager entityManager;

    public RoomService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Room createRoom(Room room) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(room);
            entityManager.getTransaction().commit();
            return room;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("Erro ao criar quarto: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Falha ao criar quarto.", e);
        }
    }

    public Room getRoomById(Integer roomId) {
        try {
            return entityManager.find(Room.class, roomId);
        } catch (Exception e) {
            System.err.println("Erro ao buscar quarto por ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<Room> getAllAvailableRooms() {
        TypedQuery<Room> query = entityManager.createQuery(
                "SELECT r FROM Room r WHERE r.isAvailable = TRUE", Room.class);
        return query.getResultList();
    }

    public Room updateRoomAvailability(Integer roomId, boolean isAvailable) {
        try {
            entityManager.getTransaction().begin();
            Room room = entityManager.find(Room.class, roomId);
            if (room != null) {
                room.setAvailable(isAvailable);
                entityManager.merge(room);
            }
            entityManager.getTransaction().commit();
            return room;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("Erro ao atualizar disponibilidade do quarto: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Falha ao atualizar disponibilidade do quarto.", e);
        }
    }
}