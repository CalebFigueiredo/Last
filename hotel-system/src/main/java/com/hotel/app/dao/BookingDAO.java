package com.hotel.app.dao;

import com.hotel.app.model.Booking;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class BookingDAO {
    private EntityManager entityManager;

    public BookingDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Booking booking) {
        entityManager.persist(booking);
    }

    public Booking findById(Long id) {
        return entityManager.find(Booking.class, id);
    }

    public void update(Booking booking) {
        entityManager.merge(booking);
    }

    public void delete(Booking booking) {
        entityManager.remove(booking);
    }

    public List<Booking> findAll() {
        return entityManager.createQuery("SELECT b FROM Booking b", Booking.class).getResultList();
    }

    public List<Booking> findBookingsByUserId(Long userId) {
        TypedQuery<Booking> query = entityManager.createQuery(
                "SELECT b FROM Booking b WHERE b.user.userId = :userId", Booking.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    // Você pode adicionar outros métodos de busca conforme a necessidade, por exemplo:
    // public List<Booking> findBookingsByRoomId(Long roomId) { ... }
    // public List<Booking> findBookingsByDateRange(LocalDate checkIn, LocalDate checkOut) { ... }
}