package com.hotel.app.view;

import com.hotel.app.model.User;
import com.hotel.app.service.BookingService;
import com.hotel.app.service.RoomService;
import com.hotel.app.service.UserService;
import com.hotel.app.util.Utilities;
import jakarta.persistence.EntityManager;

import java.util.Scanner;


public class AdminMenu {
    private UserService userService;
    private RoomService roomService;
    private BookingService bookingService;
    private final Scanner scanner = new Scanner(System.in);
    private UserManagementMenu userManagementMenu;
    private RoomManagementMenu roomManagementMenu;
    private EntityManager sharedEntityManager;

    // Construtor para injetar os serviços e o EntityManager
    public AdminMenu(UserService userService, RoomService roomService, BookingService bookingService, EntityManager sharedEntityManager) {
        this.userService = userService;
        this.roomService = roomService;
        this.bookingService = bookingService;
        this.sharedEntityManager = sharedEntityManager;

        this.userManagementMenu = new UserManagementMenu(userService, roomService, bookingService, sharedEntityManager);
        this.roomManagementMenu = new RoomManagementMenu(roomService);
    }
    // Método para iniciar o menu do administrador (removido 'static')
    public void startAdminMenu(User adminUser) {
        int choice;
        do {
            System.out.println("\n======= PAINEL DE CONTROLE DO ADMINISTRADOR =======");
            System.out.println("        Bem-vindo(a), " + adminUser.getFullName() + "!");
            System.out.println("--------------------------------------------------");
            System.out.println("Como podemos ajudá-lo(a) hoje?");
            System.out.println("1. Gerenciar Quartos");
            System.out.println("2. Gerenciar Usuários");
            System.out.println("3. Gerenciar Reservas");
            System.out.println("8. Voltar ao Menu Principal");

            choice = Utilities.readIntInput("Sua Escolha: ");

            switch (choice) {
                case 1:
                    roomManagementMenu.startRoomManagementMenu();
                    break;
                case 2:
                    userManagementMenu.startUserManagementMenu();
                    break;
                case 3:
                    System.out.println("\nGerenciar Reservas: Funcionalidade em desenvolvimento!");
                    Utilities.readNonEmptyString("Pressione Enter para continuar...");
                    break;
                case 8:
                    System.out.println("Voltando ao menu principal. Até logo!");
                    break;
                default:
                    System.out.println("Opção inválida. Por favor, tente novamente.");
                    Utilities.readNonEmptyString("Pressione Enter para continuar...");
                    break;
            }
        } while (choice != 8);
    }
}