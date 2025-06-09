package com.hotel.app.view;

import com.hotel.app.model.Role;
import com.hotel.app.model.User;
import com.hotel.app.service.BookingService;
import com.hotel.app.service.RoomService;
import com.hotel.app.service.UserService;
import com.hotel.app.util.Utilities;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter; // Esta importação pode ser removida se não for mais usada no MainMenu
import java.util.Scanner;

public class MainMenu {

    private static Scanner scanner = new Scanner(System.in);
    private static UserService userService;
    private static RoomService roomService;
    private static BookingService bookingService;
    private static EntityManager sharedEntityManager;

    private static ClientMenu clientMenu; // Instância para o menu do cliente
    private static AdminMenu adminMenu;   // Instância para o menu do administrador

    public static void start(UserService uService, RoomService rService, BookingService bService, EntityManager entityManager) {
        userService = uService;
        roomService = rService;
        bookingService = bService;
        sharedEntityManager = entityManager;

        // Inicializa as instâncias dos menus específicos, passando as dependências
        clientMenu = new ClientMenu(userService, roomService, bookingService, sharedEntityManager);
        MainMenu.adminMenu = new AdminMenu(userService, roomService, bookingService, entityManager);

        boolean running = true;
        while (running) {

            // Mensagens mais amigáveis e claras
            System.out.println("\n========== BEM-VINDO AO HOTEL A-DE-5-ESTRELAS ==========");
            System.out.println("   Conforto e Excelência à sua Espera!");
            System.out.println("--------------------------------------------------");
            System.out.println("Por favor, selecione uma opção para continuar:");
            System.out.println("1. Novo por aqui? Faça seu Cadastro de Cliente!");
            System.out.println("2. Já é cliente? Acesse sua conta!");
            System.out.println("3. Acesso Restrito (Administrador)");
            System.out.println("4. Sair do Aplicativo");
            System.out.print("Sua escolha: ");

            try {
                int option = Integer.parseInt(scanner.nextLine());

                switch (option) {
                    case 1:
                        registerCustomer();
                        break;
                    case 2:
                        customerLogin();
                        break;
                    case 3:
                        adminLogin();
                        break;
                    case 4:
                        running = false;
                        System.out.println("Agradecemos a sua visita. Volte sempre!");
                        break;
                    default:
                        System.out.println("\nOpção inválida. Por favor, digite um número entre 1 e 4.");
                        Utilities.readNonEmptyString("Pressione Enter para tentar novamente...");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nEntrada inválida. Por favor, digite um número.");
                Utilities.readNonEmptyString("Pressione Enter para tentar novamente...");
            }
        }
        scanner.close();
    }

    // --- MÉTODOS DE CADASTRO E LOGIN ---

    private static void registerCustomer() {
        System.out.println("\n--- Junte-se à Família A-DE-5-ESTRELAS! ---");
        System.out.println("Por favor, preencha seus dados para criar sua conta.");

        String fullName = Utilities.readPersonName("Nome completo: ");

        String email = Utilities.readEmail("Email: ");

        String phone = Utilities.readPhoneNumber("Telefone (ex: 9XXXXXXXX): ");

        LocalDate birthday = Utilities.readBirthDate("Data de Nascimento");

        String password = Utilities.readPassword("Crie sua senha: ");
        String confirmPassword = Utilities.readPassword("Confirme sua senha: ");

        while (!password.equals(confirmPassword)) {
            System.out.println("\nAs senhas não coincidem. Vamos tentar novamente.");
            password = Utilities.readPassword("Crie sua senha: ");
            confirmPassword = Utilities.readPassword("Confirme sua senha: ");
        }


        try {
            User registeredUser = userService.registerUser(fullName, email, phone, birthday, password, Role.GUEST);

            if (registeredUser != null) {
                System.out.println("\nParabéns, " + registeredUser.getFullName() + "! Seu cadastro foi um sucesso!");
                System.out.println("Agora você pode aproveitar todos os nossos serviços.");
                Utilities.readNonEmptyString("Pressione Enter para voltar ao Menu Principal...");
            } else {
                System.out.println("\nNão foi possível completar o cadastro. O email pode já estar em uso.");
                Utilities.readNonEmptyString("Pressione Enter para voltar ao Menu Principal...");
            }
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado durante o cadastro: " + e.getMessage());
            e.printStackTrace(); // Manter para depuração durante o desenvolvimento
            Utilities.readNonEmptyString("Pressione Enter para voltar ao Menu Principal...");
        }
    }

    // ... (restante do código do MainMenu, incluindo customerLogin e adminLogin)
    private static void customerLogin() {
        System.out.println("\n--- Acesso Cliente A-DE-5-ESTRELAS ---");
        System.out.println("Por favor, insira suas credenciais.");

        String email = Utilities.readEmail("Email: ");
        String password = Utilities.readPassword("Senha: ");

        User loggedInUser = userService.authenticateUser(email, password);

        if (loggedInUser != null) {
            if (loggedInUser.getRole() == Role.GUEST) {
                System.out.println("\nBem-vindo(a) de volta, " + loggedInUser.getFullName() + "!");
                Utilities.readNonEmptyString("Pressione Enter para acessar seu menu exclusivo...");
                clientMenu.startClientMenu(loggedInUser);
            } else {
                System.out.println("\nSuas credenciais são válidas, mas seu perfil não é de Cliente.");
                System.out.println("Por favor, utilize a opção de login apropriada.");
                Utilities.readNonEmptyString("Pressione Enter para voltar ao Menu Principal...");
            }
        } else {
            System.out.println("\nEmail ou senha incorretos. Por favor, verifique e tente novamente.");
            Utilities.readNonEmptyString("Pressione Enter para voltar ao Menu Principal...");
        }
    }


    private static void adminLogin() {
        System.out.println("\n--- Acesso Restrito: Painel Administrativo ---");
        System.out.println("Por favor, insira suas credenciais de administrador.");

        String email = Utilities.readNonEmptyString("Email: "); // Lê qualquer string não vazia
        String password = Utilities.readNonEmptyString("Senha: "); // Lê qualquer string não vazia

        User loggedInUser = userService.authenticateUser(email, password);

        if (loggedInUser != null && loggedInUser.getRole() == Role.ADMINISTRATOR) {
            System.out.println("\nAcesso concedido, Administrador(a) " + loggedInUser.getFullName() + "!");
            Utilities.readNonEmptyString("Pressione Enter para acessar o Painel de Controle...");
            adminMenu.startAdminMenu(loggedInUser);
        } else {
            System.out.println("\nCredenciais inválidas ou você não possui permissões de administrador.");
            System.out.println("Acesso negado.");
            Utilities.readNonEmptyString("Pressione Enter para voltar ao Menu Principal...");
        }
    }
}