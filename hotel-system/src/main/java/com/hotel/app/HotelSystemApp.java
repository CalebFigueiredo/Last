package com.hotel.app;

import com.hotel.app.service.BookingService;
import com.hotel.app.service.RoomService;
import com.hotel.app.service.UserService;
import com.hotel.app.view.MainMenu;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class HotelSystemApp {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("hotel_management_unit");
            entityManager = entityManagerFactory.createEntityManager();

            System.out.println("EntityManagerFactory inicializado com sucesso.");

            UserService userService = new UserService(entityManager);
            RoomService roomService = new RoomService(entityManager);
            BookingService bookingService = new BookingService(entityManager);


            System.out.println("Aplicação HotelSystemApp iniciada.");

            MainMenu.start(userService, roomService, bookingService, entityManager);

        } catch (Exception e) {
            System.out.println("Erro fatal na inicialização da aplicação: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
                System.out.println("EntityManager fechado.");
            }
            if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
                entityManagerFactory.close();
                System.out.println("EntityManagerFactory fechado.");
            }
            System.out.println("Aplicação HotelSystemApp encerrada e recursos JPA liberados.");
        }
    }
}