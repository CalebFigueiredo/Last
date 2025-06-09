package com.hotel.app.view;

import com.hotel.app.model.Role;
import com.hotel.app.model.User;
import com.hotel.app.service.BookingService; // Pode não ser necessário aqui, mas manter por consistência
import com.hotel.app.service.RoomService;   // Pode não ser necessário aqui, mas manter por consistência
import com.hotel.app.service.UserService;
import com.hotel.app.util.Utilities;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List; // Adicionar para listar

public class UserManagementMenu {

    // Remover 'static' desses atributos para injeção de dependência via construtor,
    // a menos que você tenha uma boa razão para que eles sejam estáticos e compartilhados globalmente.
    // No contexto de um menu de instância, eles geralmente não deveriam ser estáticos.
    private UserService userService;
    private RoomService roomService;
    private BookingService bookingService;
    private EntityManager sharedEntityManager;


    public UserManagementMenu(UserService uService, RoomService rService, BookingService bService, EntityManager em) {
        // Usar 'this.' para atribuir aos campos da instância
        this.userService = uService;
        this.roomService = rService;
        this.bookingService = bService;
        this.sharedEntityManager = em;
    }

    public void startUserManagementMenu() {
        boolean managing = true;
        while (managing) {
            Utilities.cls();
            System.out.println("\n--- GERENCIAMENTO DE USUÁRIOS ---");
            System.out.println("1. Listar Todos os Usuários");
            System.out.println("2. Buscar Usuário por Email");
            System.out.println("3. Adicionar Novo Usuário");
            System.out.println("4. Atualizar Dados do Usuário");
            System.out.println("5. Remover Usuário");
            System.out.println("8. Voltar ao Painel do Administrador");
            System.out.print("Sua Escolha: ");

            int option = Utilities.readIntInput("");

            switch (option) {
                case 1:
                    listAllUsers();
                    break;
                case 2:
                    searchUserByEmail();
                    break;
                case 3:
                    addNewUser();
                    break;
                case 4:
                    updateUserData();
                    break;
                case 5:
                    deleteUser();
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


    private void listAllUsers() {
        Utilities.cls();
        System.out.println("\n--- LISTAR TODOS OS USUÁRIOS ---");
        List<User> users = userService.getAllUsers(); // Supondo que você terá um método getAllUsers() no UserService

        if (users.isEmpty()) {
            System.out.println("Nenhum usuário encontrado.");
        } else {
            System.out.println("--------------------------------------------------------------------------------------------------");
            System.out.printf("%-5s %-25s %-25s %-15s %-15s\n", "ID", "Nome Completo", "Email", "Telefone", "Cargo");
            System.out.println("--------------------------------------------------------------------------------------------------");
            for (User user : users) {
                System.out.printf("%-5d %-25s %-25s %-15s %-15s\n",
                        user.getUserId(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getRole());
            }
            System.out.println("--------------------------------------------------------------------------------------------------");
        }
        Utilities.readNonEmptyString("Pressione Enter para continuar...");
    }

    private void searchUserByEmail() {
        Utilities.cls();
        System.out.println("\n--- BUSCAR USUÁRIO POR EMAIL ---");
        String email = Utilities.readNonEmptyString("Digite o email do usuário: ");
        User user = userService.getUserByEmail(email); // Já havia corrigido no UserService antes para retornar User ou null

        if (user != null) {
            System.out.println("\nUsuário Encontrado:");
            System.out.println("ID: " + user.getUserId());
            System.out.println("Nome: " + user.getFullName());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Telefone: " + user.getPhone());
            System.out.println("Aniversário: " + user.getBirthday().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("Cargo: " + user.getRole());
        } else {
            System.out.println("Usuário com o email '" + email + "' não encontrado.");
        }
        Utilities.readNonEmptyString("Pressione Enter para continuar...");
    }

    private void addNewUser() {
        Utilities.cls();
        System.out.println("\n--- ADICIONAR NOVO USUÁRIO ---");
        System.out.println("Por favor, preencha os dados do novo usuário.");

        String fullName = Utilities.readPersonName("Nome completo: ");
        String email = Utilities.readEmail("Email: ");
        String phone = Utilities.readPhoneNumber("Telefone (ex: 9XXXXXXXX): ");

        // Temporário: Usar Utilities.readBirthDate quando estiver pronto
        // Lembre-se que 'readBirthDate' do Utilities ainda precisa ser implementado para funcionar
        String birthdayStr = "13/02/2003";
        LocalDate birthday = LocalDate.parse(birthdayStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String password = Utilities.readPassword("Crie a senha: ");
        String confirmPassword = Utilities.readPassword("Confirme a senha: ");

        while (!password.equals(confirmPassword)) {
            System.out.println("\nAs senhas não coincidem. Vamos tentar novamente.");
            password = Utilities.readPassword("Crie a senha: ");
            confirmPassword = Utilities.readPassword("Confirme a senha: ");
        }

        Role userRole = selectUserRole();

        try {
            // Passa a variável 'userRole' já preenchida
            User newUser = userService.registerUser(fullName, email, phone, birthday, password, userRole);
            if (newUser != null) {
                System.out.println("\nUsuário " + newUser.getFullName() + " (" + newUser.getRole() + ") cadastrado com sucesso!");
            } else {
                System.out.println("\nNão foi possível cadastrar o usuário. O email ou telefone podem já estar em uso.");
            }
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado durante o cadastro: " + e.getMessage());
        }
        Utilities.readNonEmptyString("Pressione Enter para continuar...");
    }

    private Role selectUserRole() {
        while (true) {
            System.out.println("\nSelecione o Cargo do Usuário:");
            System.out.println("1. Cliente (GUEST)");
            System.out.println("2. Funcionário (EMPLOYEE)");
            System.out.println("3. Administrador (ADMINISTRATOR)");
            System.out.print("Sua escolha: ");

            int choice = Utilities.readIntInput("");
            switch (choice) {
                case 1: return Role.GUEST;
                case 2: return Role.EMPLOYEE;
                case 3: return Role.ADMINISTRATOR;
                default:
                    System.out.println("Opção inválida. Por favor, digite 1, 2 ou 3.");
            }
        }
    }

    private void updateUserData() {
        Utilities.cls();
        System.out.println("\n--- ATUALIZAR DADOS DO USUÁRIO ---");
        System.out.println("Funcionalidade em desenvolvimento!");
        Utilities.readNonEmptyString("Pressione Enter para continuar...");
    }

    private void deleteUser() {
        Utilities.cls();
        System.out.println("\n--- REMOVER USUÁRIO ---");
        System.out.println("Funcionalidade em desenvolvimento!");
        Utilities.readNonEmptyString("Pressione Enter para continuar...");
    }
}