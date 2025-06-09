package com.hotel.app.service;

import com.hotel.app.model.Booking;
import com.hotel.app.model.BookingStatus;
import com.hotel.app.model.Room;
import com.hotel.app.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional; // Importe Optional

public class BookingService {

    private EntityManager entityManager;

    public BookingService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Booking createBooking(Booking booking) {
        try {
            entityManager.getTransaction().begin();
            // Verifica se o quarto está disponível para o período selecionado
            if (!isRoomAvailable(booking.getRoom().getRoomId(), booking.getCheckInDate(), booking.getCheckOutDate())) {
                throw new RuntimeException("O quarto " + booking.getRoom().getRoomNumber() + " não está disponível para as datas selecionadas.");
            }

            // Define o quarto como indisponível (se a reserva for confirmada imediatamente)
            // Ou você pode ter um status BOOKING_CONFIRMED que muda a disponibilidade
            // Por enquanto, vamos marcar o quarto como indisponível ao criar a reserva PENDING
            // booking.getRoom().setAvailable(false); // Esta linha pode ser removida se você gerenciar disponibilidade apenas por reservas
            // O método isRoomAvailable já faz essa verificação.

            entityManager.persist(booking);
            entityManager.getTransaction().commit();
            return booking;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao criar a reserva: " + e.getMessage(), e);
        }
    }

    public Optional<Booking> findBookingById(Integer bookingId) {
        try {
            return Optional.ofNullable(entityManager.find(Booking.class, bookingId));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<Booking> getAllBookings() {
        return entityManager.createQuery("SELECT b FROM Booking b", Booking.class).getResultList();
    }

    public void updateBooking(Booking booking) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(booking);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao atualizar reserva: " + e.getMessage(), e);
        }
    }

    public void deleteBooking(Integer bookingId) {
        try {
            entityManager.getTransaction().begin();
            Booking booking = entityManager.find(Booking.class, bookingId);
            if (booking != null) {
                entityManager.remove(booking);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao deletar reserva: " + e.getMessage(), e);
        }
    }

    /**
     * Verifica se um quarto está disponível para um determinado período.
     * Um quarto está disponível se não houver reservas CONCLUIDAS ou PENDENTES
     * que se sobreponham ao período desejado.
     *
     * @param roomId O ID do quarto a ser verificado.
     * @param desiredCheckIn A data de check-in desejada.
     * @param desiredCheckOut A data de check-out desejada.
     * @return true se o quarto estiver disponível, false caso contrário.
     */
    public boolean isRoomAvailable(Integer roomId, LocalDate desiredCheckIn, LocalDate desiredCheckOut) {
        // Ajuste para garantir que desiredCheckOut não seja inclusivo no cálculo de sobreposição,
        // pois a saída de um hóspede no dia X libera o quarto para entrada no mesmo dia X.
        // No entanto, para evitar confusão, é comum que a "disponibilidade" para o check-out
        // signifique que o quarto está livre APÓS a data de check-out.

        // Uma reserva se sobrepõe se:
        // (start1 < end2) AND (end1 > start2)
        // Onde:
        // start1 = desiredCheckIn, end1 = desiredCheckOut
        // start2 = existingBooking.checkInDate, end2 = existingBooking.checkOutDate

        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(b) FROM Booking b " +
                        "WHERE b.room.roomId = :roomId " +
                        "AND (b.status = 'PENDING' OR b.status = 'CONFIRMED' OR b.status = 'CHECKED_IN') " +
                        "AND (b.checkInDate < :desiredCheckOut AND b.checkOutDate > :desiredCheckIn)",
                Long.class);

        query.setParameter("roomId", roomId);
        query.setParameter("desiredCheckIn", desiredCheckIn);
        query.setParameter("desiredCheckOut", desiredCheckOut);

        Long count = query.getSingleResult();
        return count == 0; // Se o count for 0, não há sobreposição, então o quarto está disponível
    }

    // NOVO MÉTODO: Listar histórico de reservas de um cliente específico
    public List<Booking> listClientBookings(User user) {
        try {
            // Certifique-se de que o objeto 'user' esteja em um estado gerenciado, se vier de fora do contexto do EM
            // Ou você pode buscar o user novamente pelo ID, se houver problemas de estado detached
            // User managedUser = entityManager.find(User.class, user.getUserId());
            // if (managedUser == null) {
            //    return new java.util.ArrayList<>(); // Usuário não encontrado no contexto do EM
            // }

            // Query para buscar todas as reservas associadas a um usuário
            return entityManager.createQuery(
                            "SELECT b FROM Booking b WHERE b.user = :user ORDER BY b.checkInDate DESC", Booking.class)
                    .setParameter("user", user)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao listar reservas do cliente " + user.getFullName() + ": " + e.getMessage());
            // e.printStackTrace(); // Para depuração
            return new java.util.ArrayList<>(); // Retorna uma lista vazia em caso de erro
        }
    }
}