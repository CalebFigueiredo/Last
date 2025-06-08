package com.hotel.app.view;

import com.hotel.app.dao.RoomDAO; // Manter, pois pode ser usado para exibir quartos
import com.hotel.app.dao.UserDAO; // Manter, mas o MainMenu vai chamar o UserService
import com.hotel.app.model.User;
import com.hotel.app.model.Role; // Para definir o papel do usuário
import com.hotel.app.service.UserService; // Importa o UserService
import jakarta.persistence.EntityManager; // Para gerenciar transações no menu

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException; // Para tratar entrada de int
import java.util.List;
import java.util.Scanner;

public class MainMenu {

    private static Scanner scanner = new Scanner(System.in);
    private static UserService userService;
    private static RoomDAO roomDAO; // Vamos manter o RoomDAO por enquanto para exibir quartos

    // O método start agora recebe as dependências
    public static void start(UserService service, RoomDAO rDAO, EntityManager em) {
        // Atribui as dependências às variáveis estáticas ou de instância (neste caso, estáticas para simplicidade do main)
        userService = service;
        roomDAO = rDAO; // Pode ser útil para listar quartos disponíveis
        // O EntityManager 'em' é recebido, mas as transações para operações de escrita serão gerenciadas
        // por métodos específicos no MainMenu, ou transferidas para os Services/DAOs.

        boolean running = true;
        while (running) {
            System.out.println("\n==========MENU PRINCIPAL DO HOTEL A-DE-5-ESTRELAS ============");
            System.out.println("1- Fazer Cadastro de Cliente"); // Renomeado para clareza
            System.out.println("2- Acessar Menu do Cliente (Login)"); // Nova opção para clientes logarem
            System.out.println("3- Acessar Menu do Administrador"); // Opção para admins
            System.out.println("4- Sair");
            System.out.print("Escolha uma opção: ");

            try {
                int option = Integer.parseInt(scanner.nextLine());

                switch (option) {
                    case 1:
                        registerCustomer(em); // Passa o EntityManager para gerenciar a transação de cadastro
                        break;
                    case 2:
                        customerLogin(em); // Passa o EntityManager para o login e acesso ao menu do cliente
                        break;
                    case 3:
                        adminLogin(em); // Para o menu do administrador
                        break;
                    case 4:
                        running = false;
                        System.out.println("Saindo da aplicação. Até mais!");
                        break;
                    default:
                        System.out.println("Opção inválida. Por favor, tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
            }
        }
        scanner.close(); // Fecha o scanner quando a aplicação principal encerra o menu
    }

    // --- MÉTODOS DE CADASTRO E LOGIN ---

    private static void registerCustomer(EntityManager entityManager) {
        System.out.println("\n--- NOVO CADASTRO DE CLIENTE ---");
        String fullName;
        String email; // Adicionado para o email (será o username)
        String phone;
        String password;
        String confirmPassword; // Para confirmar a senha
        LocalDate birthday = null; // Data de nascimento

        // Validar Nome Completo
        while (true) {
            System.out.print("Digite seu nome completo: ");
            fullName = scanner.nextLine();
            if (fullName.matches("^[\\p{L} .'-]+$") && fullName.trim().length() > 2) { // Aceita letras, espaços, pontos, hífens, apóstrofos
                break;
            } else {
                System.out.println("Nome inválido. O nome deve conter apenas letras e ter no mínimo 3 caracteres.");
            }
        }

        // Validar Email
        while (true) {
            System.out.print("Digite seu email: ");
            email = scanner.nextLine();
            if (email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                // Verificar se o email já existe usando o UserService (que usa o DAO)
                // Esta verificação precisa de uma transação, mas o UserService já lida com o DAO que usa o EM injetado.
                // O check dentro do registerUser já cobre isso.
                break;
            } else {
                System.out.println("Email inválido. Por favor, digite um email válido.");
            }
        }

        // Validar Telefone
        while (true) {
            System.out.print("Digite o número de telefone (apenas números): ");
            phone = scanner.nextLine();
            if (phone.matches("^\\d{9,15}$")) { // Exemplo: 9 a 15 dígitos
                // Verificar se o telefone já existe
                // A verificação será feita dentro do UserService.registerUser
                break;
            } else {
                System.out.println("Telefone inválido. Use apenas números e um formato válido (ex: 9 digitos).");
            }
        }

        // Validar Data de Nascimento
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        while (true) {
            System.out.print("Digite sua data de nascimento (DD/MM/AAAA): ");
            String birthdayStr = scanner.nextLine();
            try {
                birthday = LocalDate.parse(birthdayStr, dateFormatter);
                if (birthday.isAfter(LocalDate.now().minusYears(18))) { // Usuário deve ter no mínimo 18 anos
                    System.out.println("Você deve ter pelo menos 18 anos para se cadastrar.");
                    continue;
                }
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido. Use DD/MM/AAAA.");
            }
        }

        // Validar Senha
        while (true) {
            System.out.print("Digite sua senha: ");
            password = scanner.nextLine();
            if (password.length() >= 6) { // Senha deve ter pelo menos 6 caracteres
                System.out.print("Confirme sua senha: ");
                confirmPassword = scanner.nextLine();
                if (password.equals(confirmPassword)) {
                    break;
                } else {
                    System.out.println("As senhas não coincidem. Tente novamente.");
                }
            } else {
                System.out.println("A senha deve ter pelo menos 6 caracteres.");
            }
        }

        // Agora, chame o UserService para registrar o usuário no banco de dados
        // O UserService.registerUser já vai iniciar e commitar/rollback a transação,
        // ou assumir que uma transação já está ativa (neste caso, a do main).
        // Para simplicidade inicial, vamos gerenciar a transação para esta operação AQUI.
        // No futuro, isso poderia ser movido para uma camada de "transação de serviço".
        try {
            // Iniciar transação antes de chamar o serviço/DAO
            entityManager.getTransaction().begin();
            User registeredUser = userService.registerUser(fullName, email, phone, birthday, password);
            if (registeredUser != null) {
                entityManager.getTransaction().commit(); // Commit se o cadastro foi bem-sucedido
                System.out.println("\nCadastro realizado com sucesso!");
                System.out.println("Nome: " + registeredUser.getFullName());
                System.out.println("Email: " + registeredUser.getEmail());
                System.out.println("Telefone: " + registeredUser.getPhone());
                // Não exiba a senha real aqui!
            } else {
                // A mensagem de erro já foi impressa pelo userService.registerUser
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback(); // Rollback se houve erro no cadastro
                }
            }
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback(); // Rollback em caso de exceção
            }
            System.err.println("Erro inesperado durante o cadastro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void customerLogin(EntityManager entityManager) {
        System.out.println("\n--- LOGIN DO CLIENTE ---");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String password = scanner.nextLine();

        User loggedInUser = null;
        try {
            entityManager.getTransaction().begin(); // Inicia uma transação para a busca
            loggedInUser = userService.authenticateUser(email, password);
            entityManager.getTransaction().commit(); // Commita a transação de leitura
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("Erro durante o login: " + e.getMessage());
            e.printStackTrace();
        }


        if (loggedInUser != null) {
            System.out.println("\nLogin bem-sucedido! Bem-vindo(a), " + loggedInUser.getFullName() + "!");
            // Se o papel do usuário é GUEST, abre o menu do cliente.
            if (loggedInUser.getRole() == Role.GUEST) {
                customerMenu(loggedInUser, entityManager);
            } else {
                System.out.println("Seu papel não é de GUEST. Por favor, use o menu apropriado.");
            }
        } else {
            System.out.println("Email ou senha inválidos.");
        }
    }

    private static void adminLogin(EntityManager entityManager) {
        System.out.println("\n--- LOGIN DO ADMINISTRADOR ---");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String password = scanner.nextLine();

        User loggedInUser = null;
        try {
            entityManager.getTransaction().begin(); // Inicia uma transação para a busca
            loggedInUser = userService.authenticateUser(email, password);
            entityManager.getTransaction().commit(); // Commita a transação de leitura
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("Erro durante o login de administrador: " + e.getMessage());
            e.printStackTrace();
        }

        if (loggedInUser != null && loggedInUser.getRole() == Role.ADMINISTRATOR) {
            System.out.println("\nLogin de Administrador bem-sucedido! Bem-vindo(a), " + loggedInUser.getFullName() + "!");
            adminMenu(entityManager); // Chama o menu do administrador
        } else {
            System.out.println("Acesso negado. Credenciais inválidas ou você não é um administrador.");
        }
    }

    // --- MENUS DE NAVEGAÇÃO ---

    private static void customerMenu(User currentUser, EntityManager entityManager) {
        boolean customerRunning = true;
        while (customerRunning) {
            System.out.println("\n=======Menu do Cliente (" + currentUser.getFullName() + ")======");
            System.out.println("1. Realizar Reserva");
            System.out.println("2. Ver Perfil");
            System.out.println("3. Fazer Pagamento");
            System.out.println("4. Check-in/Check-out Online");
            System.out.println("5. Comunicação e Suporte");
            System.out.println("6. Avaliação e Feedback");
            System.out.println("7. Histórico e Cancelamentos");
            System.out.println("8. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        makeReservation(currentUser, entityManager);
                        break;
                    case 2:
                        viewProfile(currentUser);
                        break;
                    case 8:
                        customerRunning = false;
                        System.out.println("Saindo do menu do cliente.");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
            }
        }
    }

    private static void adminMenu(EntityManager entityManager) {
        // Este menu será implementado quando tivermos um AdminService
        boolean adminRunning = true;
        while (adminRunning) {
            System.out.println("\n=======Menu do Administrador======");
            System.out.println("1. Gerenciar Usuários");
            System.out.println("2. Gerenciar Quartos");
            System.out.println("3. Ver Reservas");
            System.out.println("4. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        // Chamar método para gerenciar usuários (requer AdminService ou UserService com métodos admin)
                        System.out.println("Funcionalidade de gerenciar usuários em desenvolvimento.");
                        break;
                    case 2:
                        // Chamar método para gerenciar quartos (requer RoomService com métodos admin)
                        System.out.println("Funcionalidade de gerenciar quartos em desenvolvimento.");
                        break;
                    case 3:
                        // Chamar método para ver reservas (requer BookingService)
                        System.out.println("Funcionalidade de ver reservas em desenvolvimento.");
                        break;
                    case 4:
                        adminRunning = false;
                        System.out.println("Saindo do menu do administrador.");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
            }
        }
    }

    // --- MÉTODOS DE OPERAÇÕES DO CLIENTE (Muitos ainda a serem implementados) ---

    private static void makeReservation(User currentUser, EntityManager entityManager) {
        System.out.println("\n====Realizar Reserva====");
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataMinima = LocalDate.now(); // Data mínima para reserva (hoje ou no futuro)
        LocalDate dateIn = null;
        LocalDate dateOut = null;

        // Escolher datas de entrada e saída
        while (true) {
            System.out.print("Por favor, digite a data de entrada (dd/MM/aaaa): ");
            String entradaS = scanner.nextLine();
            try {
                dateIn = LocalDate.parse(entradaS, formato);
                if (!dateIn.isBefore(dataMinima)) {
                    break;
                } else {
                    System.out.println("Data de entrada inválida! Escolha uma data de hoje ou futura.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido! Tente novamente (DD/MM/AAAA).");
            }
        }

        while (true) {
            System.out.print("Por favor, digite a data de saída (dd/MM/aaaa): ");
            String saidaS = scanner.nextLine();
            try {
                dateOut = LocalDate.parse(saidaS, formato);
                if (dateOut.isAfter(dateIn)) {
                    System.out.println("Período de reserva de " + dateIn.format(formato) + " até " + dateOut.format(formato) + " confirmado!");
                    break;
                } else {
                    System.out.println("Data de saída inválida! Não pode ser antes ou igual à data de entrada.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido! Tente novamente (DD/MM/AAAA).");
            }
        }

        // TODO: Integrar com RoomService para listar quartos disponíveis
        // TODO: Integrar com BookingService para criar a reserva no banco de dados

        System.out.println("Funcionalidade de Realizar Reserva em desenvolvimento (seleção de quarto, hóspedes, serviços e persistência).");
    }

    private static void viewProfile(User user) {
        System.out.println("\n--- PERFIL DO CLIENTE ---");
        System.out.println("ID do Usuário: " + user.getUserId());
        System.out.println("Nome Completo: " + user.getFullName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Telefone: " + user.getPhone());
        System.out.println("Data de Nascimento: " + user.getBirthday().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Papel: " + user.getRole());
        // Não exiba a senha!
    }
    // Outros métodos para pagamento, check-in, etc. virão aqui e chamarão os respectivos Services.
}