package com.hotel.app.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Room {
    private int id;
    private String roomNumber;
    private String type;
    private BigDecimal price;
    private boolean isAvailable;

    // Construtor completo
    public Room(int id, String roomNumber, String type, BigDecimal price, boolean isAvailable) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    // Construtor para criar um novo quarto
    public Room(String roomNumber, String type, BigDecimal price, boolean isAvailable) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    // Getters
    public int getId() {return id;}
    public String getRoomNumber() {return roomNumber;}
    public String getType() {return type;}
    public BigDecimal getPrice() {return price;}
    public boolean isAvailable() { return isAvailable;}

    // Setters
    public void setId(int id) {this.id = id;}
    public void setRoomNumber(String roomNumber) {this.roomNumber = roomNumber;}
    public void setType(String type) {this.type = type;}
    public void setPrice(BigDecimal price) {this.price = price;}
    public void setAvailable(boolean available) {isAvailable = available;}


    @Override
    public String toString() {
        return "Room{" +
                "\tid=" + id +
                ", \tNúmero do Quarto ='" + roomNumber + '\'' +
                ", \tTipo de Quarto ='" + type + '\'' +
                ", \tPreço =" + price +
                ", \tDisponivel =" + isAvailable +
                '}';
    }
}