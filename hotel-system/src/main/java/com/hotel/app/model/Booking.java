package com.hotel.app.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
public class Booking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Coluna de chave estrangeira para User
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false) // Coluna de chave estrangeira para Room
    private Room room;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @Enumerated(EnumType.STRING) // Armazena o enum como String (CONFIRMED, PENDING, etc.)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    // Construtor padrão.
    public Booking() {}

    // Construtor com parâmetros
    public Booking(User user, Room room, LocalDate checkInDate, LocalDate checkOutDate, LocalDate bookingDate, BookingStatus status, Double totalPrice) {
        this.user = user;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingDate = bookingDate;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    // Getters e Setters
    public Long getBookingId() {return bookingId;}
    public void setBookingId(Long bookingId) {this.bookingId = bookingId;}

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}

    public Room getRoom() {return room;}
    public void setRoom(Room room) {this.room = room;}

    public LocalDate getCheckInDate() {return checkInDate;}
    public void setCheckInDate(LocalDate checkInDate) {this.checkInDate = checkInDate;}

    public LocalDate getCheckOutDate() {return checkOutDate;}
    public void setCheckOutDate(LocalDate checkOutDate) {this.checkOutDate = checkOutDate;}

    public LocalDate getBookingDate() {return bookingDate;}
    public void setBookingDate(LocalDate bookingDate) {this.bookingDate = bookingDate;}

    public BookingStatus getStatus() {return status;}
    public void setStatus(BookingStatus status) {this.status = status;}

    public Double getTotalPrice() {return totalPrice;}

    public void setTotalPrice(Double totalPrice) {this.totalPrice = totalPrice;}

    @Override
    public String toString() {
        return "Booking{" +
                "Id da reserva=" + bookingId +
                ", Hóspede=" + (user != null ? user.getFullName() : "N/A") +
                ", Quarto=" + (room != null ? room.getRoomNumber() : "N/A") +
                ", Data de check-in=" + checkInDate +
                ", Data de check-out=" + checkOutDate +
                ", Data da reserva=" + bookingDate +
                ", Estado da reserva=" + status +
                ", Preço total =" + totalPrice +
                '}';
    }
}