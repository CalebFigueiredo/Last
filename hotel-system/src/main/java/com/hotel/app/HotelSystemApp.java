package com.hotel.app;

import com.hotel.app.dao.RoomDAO;
import com.hotel.app.dao.UserDAO;
import com.hotel.app.service.UserService;
import com.hotel.app.util.JpaUtil;
import com.hotel.app.view.MainMenu;

import jakarta.persistence.EntityManager;

public class HotelSystemApp {

    public static void main(String[] args) {
        JpaUtil.init(); // Inicializa o EntityManagerFactory
        System.out.println("Aplicação HotelSystemApp iniciada.");

        EntityManager entityManager = null;

        try {
            entityManager = JpaUtil.getEntityManager();

            UserDAO userDAO = new UserDAO(entityManager);
            RoomDAO roomDAO = new RoomDAO(entityManager);

            UserService userService = new UserService(userDAO);
            // Futuramente, você terá: BookingService bookingService = new BookingService(bookingDAO, userDAO, roomDAO);

            MainMenu.start(userService, roomDAO, entityManager);


        } catch (Exception e) {
            // Em um Main, o rollback aqui pode ser para um caso excepcional não tratado nos menus.
            // A maioria das transações será gerenciada DENTRO dos métodos do MainMenu ou Services.
            if (entityManager != null && entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("Ocorreu um erro inesperado na aplicação: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
            JpaUtil.close(); // Fecha o EntityManagerFactory
            System.out.println("Aplicação HotelSystemApp encerrada e recursos JPA liberados.");
        }
    }
}