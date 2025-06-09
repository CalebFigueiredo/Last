package com.hotel.app.dao;

import com.hotel.app.model.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class RoomDAO {

    private EntityManager entityManager;

    public RoomDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Room addRoom(Room room) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(room);
            entityManager.getTransaction().commit();
            return room;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("Erro ao adicionar quarto no DAO: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Room getRoomById(Integer id) {
        return entityManager.find(Room.class, id);
    }

    public Room getRoomByNumber(String roomNumber) {
        try {
            TypedQuery<Room> query = entityManager.createQuery("SELECT r FROM Room r WHERE r.roomNumber = :roomNumber", Room.class);
            query.setParameter("roomNumber", roomNumber);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // Nenhum quarto encontrado com este número
        } catch (Exception e) {
            System.err.println("Erro ao buscar quarto por número no DAO: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<Room> getAllRooms() {
        TypedQuery<Room> query = entityManager.createQuery("SELECT r FROM Room r ORDER BY r.roomNumber", Room.class);
        return query.getResultList();
    }

    public Room updateRoom(Room room) {
        try {
            entityManager.getTransaction().begin();
            Room updatedRoom = entityManager.merge(room);
            entityManager.getTransaction().commit();
            return updatedRoom;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("Erro ao atualizar quarto no DAO: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void deleteRoom(Integer roomId) {
        try {
            entityManager.getTransaction().begin();
            Room room = entityManager.find(Room.class, roomId);
            if (room != null) {
                entityManager.remove(room);
                System.out.println("Quarto com ID " + roomId + " removido do banco de dados.");
            } else {
                System.out.println("Quarto com ID " + roomId + " não encontrado para remoção.");
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("Erro ao deletar quarto no DAO: " + e.getMessage());
            e.printStackTrace();
        }
    }
}