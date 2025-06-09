package com.hotel.app.view;

import com.hotel.app.service.UserService;

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
    private EntityManager sharedEntityManager;

    // Construtor para injetar os serviços e o EntityManager
    public AdminMenu(UserService userService, RoomService roomService, BookingService bookingService, EntityManager sharedEntityManager) {
        this.userService = userService;
        this.roomService = roomService;
        this.bookingService = bookingService;
        this.sharedEntityManager = sharedEntityManager; // Inicializa o EntityManager

        // Inicializa a nova instância do UserManagementMenu aqui,
        // passando todas as dependências que ele precisa.
        // As dependências 'roomService' e 'bookingService' são passadas mesmo que 'UserManagementMenu'
        // não as utilize diretamente, para manter a consistência na injeção de dependências
        // e flexibilidade para o futuro.
        this.userManagementMenu = new UserManagementMenu(userService, roomService, bookingService, sharedEntityManager);
    }
    // Método para iniciar o menu do administrador (removido 'static')
    public void startAdminMenu(User adminUser) {
        int choice;
        do {
            Utilities.cls(); // Limpa a tela
            System.out.println("\n======= PAINEL DE CONTROLE DO ADMINISTRADOR =======");
            System.out.println("        Bem-vindo(a), " + adminUser.getFullName() + "!");
            System.out.println("--------------------------------------------------");
            System.out.println("Como podemos ajudá-lo(a) hoje?");
            System.out.println("1. Gerenciar Quartos");
            System.out.println("2. Gerenciar Usuários");
            System.out.println("3. Gerenciar Reservas");
            // System.out.println("4. Gerenciar Funcionários");
            // System.out.println("5. Relatórios e Análises");
            System.out.println("8. Voltar ao Menu Principal");

            choice = Utilities.readIntInput("Sua Escolha: "); // Usa o readIntInput do Utilities

            switch (choice) {
                case 1:
                    // Lógica para Gerenciar Quartos - Em breve, chamaremos um RoomManagementMenu aqui
                    System.out.println("\nGerenciar Quartos: Funcionalidade em desenvolvimento!");
                    Utilities.readNonEmptyString("Pressione Enter para continuar...");
                    break;
                case 2:
                    userManagementMenu.startUserManagementMenu();
                    break;
                case 3:
                    // Lógica para Gerenciar Reservas - Em breve, chamaremos um BookingManagementMenu aqui
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


    private void manageUsers() {
        boolean managing = true;
        while (managing) {
            Utilities.cls();
            System.out.println("\n--- GERENCIAMENTO DE USUÁRIOS ---");
            System.out.println("1. Listar Todos os Clientes");
            System.out.println("2. Buscar Cliente por Email");
            System.out.println("3. Atualizar Dados do Cliente");
            System.out.println("4. Remover Cliente");
            System.out.println("5. Adicionar Novo Usuário"); // NOVA OPÇÃO AQUI
            System.out.println("8. Voltar ao Painel do Administrador");
            System.out.print("Sua Escolha: ");

            int option = Utilities.readIntInput(""); // Usando o readIntInput que já criamos!

            switch (option) {
                case 1:
                    //listAllClients();
                    break;
                case 2:
                    //searchClientByEmail();
                    break;
                case 3:
                    //updateClientData();
                    break;
                case 4:
                    //deleteClient();
                    break;
                case 5:
                    //addNewUser();
                    break;
                case 8:
                    managing = false;
                    System.out.println("Voltando ao Painel do Administrador...");
                    break;
                default:
                    System.out.println("Opção inválida. Por favor, digite um número entre 1 e 8.");
                    Utilities.readNonEmptyString("Pressione Enter para continuar...");
            }
        }
    }


}
