package com.hotel.app.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Representa um quarto no sistema de gestão hoteleira.
 * Anotado com JPA para mapeamento para a tabela 'rooms' no banco de dados.
 */
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Integer roomId;

    @Column(name = "room_number", unique = true, nullable = false)
    private String roomNumber;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;


    /*** Construtor padrão exigido pelo JPA. */
    public Room() {}

    /**
     * Construtor completo para criar um objeto Room com um ID existente.
     *
     * @param roomId      O ID único do quarto.
     * @param roomNumber  O Número único do Quarto.
     * @param price       O preço do Quarto.
     * @param type        O tipo do quarto. Ex: Luxo, Simples...
     * @param isAvailable Mostra o estado do quarto
     */
    public Room(Integer roomId, String roomNumber, String type, BigDecimal price, boolean isAvailable) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    /**
     * Construtor para criar um novo objeto Room sem um ID (o ID será gerado pelo DB).
     *
     * @param roomNumber  O Número único do Quarto.
     * @param price       O preço do Quarto.
     * @param type        O tipo do quarto. Ex: Luxo, Simples...
     * @param isAvailable Mostra o estado do quarto
     */
    public Room(String roomNumber, String type, BigDecimal price, boolean isAvailable) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    // Getters
    public Integer getRoomId() {return roomId;}
    public String getRoomNumber() {return roomNumber;}
    public String getType() {return type;}
    public BigDecimal getPrice() {return price;}
    public boolean isAvailable() {return isAvailable;}

    // Setters
    public void setRoomId(Integer roomId) {this.roomId = roomId;}
    public void setRoomNumber(String roomNumber) {this.roomNumber = roomNumber;}
    public void setType(String type) {this.type = type;}
    public void setPrice(BigDecimal price) {this.price = price;}
    public void setAvailable(boolean available) {isAvailable = available;}

    @Override
    public String toString() {
        return "Room{" +
                "\tid do quarto =" + roomId +
                ", \tNúmero do Quarto ='" + roomNumber + '\'' +
                ", \tTipo de Quarto ='" + type + '\'' +
                ", \tPreço =" + price +
                ", \tDisponivel =" + isAvailable +
                '}';
    }
}