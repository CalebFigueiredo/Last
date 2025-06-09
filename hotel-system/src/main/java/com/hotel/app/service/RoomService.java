package com.hotel.app.service;

import com.hotel.app.dao.RoomDAO; // Certifique-se de ter essa classe no pacote 'dao'
import com.hotel.app.model.Room;
import com.hotel.app.model.RoomType;
import jakarta.persistence.EntityManager;

import java.util.List;

public class RoomService {

    private RoomDAO roomDAO;

    public RoomService(EntityManager entityManager) {
        this.roomDAO = new RoomDAO(entityManager);
    }

    /**
     * Adiciona um novo quarto ao sistema.
     * @param roomNumber O número do quarto.
     * @param roomType O tipo de quarto.
     * @param pricePerNight O preço por noite.
     * @param capacity A capacidade de pessoas.
     * @param floor O andar do quarto.
     * @param isAvailable Se o quarto está disponível.
     * @return O objeto Room persistido, ou null se já existir um quarto com o mesmo número.
     */
    public Room addRoom(String roomNumber, RoomType roomType, double pricePerNight, int capacity, int floor, boolean isAvailable) {
        // Opcional: Verifique se já existe um quarto com o mesmo número antes de adicionar
        if (roomDAO.getRoomByNumber(roomNumber) != null) {
            System.out.println("Erro: Já existe um quarto com o número " + roomNumber + ".");
            return null;
        }

        Room newRoom = new Room(roomNumber, roomType, pricePerNight, capacity, floor, isAvailable);
        try {
            return roomDAO.addRoom(newRoom);
        } catch (Exception e) {
            System.err.println("Erro ao adicionar quarto: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtém um quarto pelo seu ID.
     * @param id O ID do quarto.
     * @return O objeto Room, ou null se não encontrado.
     */
    public Room getRoomById(Integer id) {
        return roomDAO.getRoomById(id);
    }

    /**
     * Obtém um quarto pelo seu número.
     * @param roomNumber O número do quarto.
     * @return O objeto Room, ou null se não encontrado.
     */
    public Room getRoomByNumber(String roomNumber) {
        return roomDAO.getRoomByNumber(roomNumber);
    }


    /**
     * Obtém uma lista de todos os quartos.
     * @return Lista de todos os quartos.
     */
    public List<Room> getAllRooms() {
        return roomDAO.getAllRooms();
    }

    // Métodos para atualizar e deletar (serão implementados depois)
    public Room updateRoom(Room room) {
        // Implementação em breve
        return null;
    }

    public void deleteRoom(Integer roomId) {
        // Implementação em breve
    }
}