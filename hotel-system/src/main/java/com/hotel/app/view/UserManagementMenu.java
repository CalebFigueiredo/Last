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

    private UserService userService;
    private RoomService roomService;
    private BookingService bookingService;
    private EntityManager sharedEntityManager;


    public UserManagementMenu(UserService uService, RoomService rService, BookingService bService, EntityManager em) {
        this.userService = uService;
        this.roomService = rService;
        this.bookingService = bService;
        this.sharedEntityManager = em;
    }

    public void startUserManagementMenu() {
        boolean managing = true;
        while (managing) {
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
                    updateUserData(); // Chamando o método atualizado
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
        System.out.println("\n--- LISTAR TODOS OS USUÁRIOS ---");
        List<User> users = userService.getAllUsers();

        if (users.isEmpty()) {
            System.out.println("Nenhum usuário encontrado.");
        } else {
            System.out.println("--------------------------------------------------------------------------------------------------");
            System.out.printf("%-5s %-25s %-25s %-15s %-15s %-12s\n", "ID", "Nome Completo", "Email", "Telefone", "Cargo", "Aniversário");
            System.out.println("--------------------------------------------------------------------------------------------------");
            for (User user : users) {
                System.out.printf("%-5d %-25s %-25s %-15s %-15s %-12s\n",
                        user.getUserId(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getRole(),
                        user.getBirthday().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
            System.out.println("--------------------------------------------------------------------------------------------------");
        }
        Utilities.readNonEmptyString("Pressione Enter para continuar...");
    }

    private void searchUserByEmail() {
        System.out.println("\n--- BUSCAR USUÁRIO POR EMAIL ---");
        String email = Utilities.readNonEmptyString("Digite o email do usuário: ");
        User user = userService.getUserByEmail(email);

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
        System.out.println("\n--- ADICIONAR NOVO USUÁRIO ---");
        System.out.println("Por favor, preencha os dados do novo usuário.");

        String fullName = Utilities.readPersonName("Nome completo: ");
        String email = Utilities.readEmail("Email: ");
        String phone = Utilities.readPhoneNumber("Telefone (ex: 9XXXXXXXX): ");
        LocalDate birthday = Utilities.readBirthDate("Data de Nascimento");

        String password = Utilities.readPassword("Crie a senha: ");
        String confirmPassword = Utilities.readPassword("Confirme a senha: ");

        while (!password.equals(confirmPassword)) {
            System.out.println("\nAs senhas não coincidem. Vamos tentar novamente.");
            password = Utilities.readPassword("Crie a senha: ");
            confirmPassword = Utilities.readPassword("Confirme a senha: ");
        }

        Role userRole = selectUserRole();

        try {
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

    // --- NOVO MÉTODO updateUserData() com Comparativo e Confirmação ---
    private void updateUserData() {
        System.out.println("\n--- ATUALIZAR DADOS DO USUÁRIO ---");
        String emailToUpdate = Utilities.readEmail("Digite o email do usuário que deseja atualizar: ");

        User currentUser = userService.getUserByEmail(emailToUpdate);

        if (currentUser == null) {
            System.out.println("Usuário com o email '" + emailToUpdate + "' não encontrado.");
            Utilities.readNonEmptyString("Pressione Enter para continuar...");
            return;
        }

        System.out.println("\nUsuário encontrado. Por favor, insira os NOVOS dados para este usuário.");
        System.out.println("As validações serão aplicadas ao final. Se não quiser alterar a senha, insira a senha atual.");

        String newFullName = Utilities.readPersonName("Novo Nome completo: ");
        String newEmail = Utilities.readEmail("Novo Email: ");
        String newPhone = Utilities.readPhoneNumber("Novo Telefone (ex: 9XXXXXXXX): ");
        LocalDate newBirthday = Utilities.readBirthDate("Nova Data de Nascimento");

        String newPassword = Utilities.readPassword("Nova Senha (deve ser forte): ");
        String confirmNewPassword = Utilities.readPassword("Confirme a nova senha: ");

        while (!newPassword.equals(confirmNewPassword)) {
            System.out.println("\nAs senhas não coincidem. Vamos tentar novamente.");
            newPassword = Utilities.readPassword("Nova Senha (deve ser forte): ");
            confirmNewPassword = Utilities.readPassword("Confirme a nova senha: ");
        }

        Role newRole = selectUserRole(); // Permite selecionar um novo cargo

        System.out.println("\n--- REVISÃO DOS DADOS DO USUÁRIO ---");
        System.out.println("------------------------------------------------------------------");
        System.out.printf("%-20s %-30s %-30s\n", "Campo", "Valor Atual", "Novo Valor Proposto");
        System.out.println("------------------------------------------------------------------");
        System.out.printf("%-20s %-30s %-30s\n", "Nome Completo:", currentUser.getFullName(), newFullName);
        System.out.printf("%-20s %-30s %-30s\n", "Email:", currentUser.getEmail(), newEmail);
        System.out.printf("%-20s %-30s %-30s\n", "Telefone:", currentUser.getPhone(), newPhone);
        System.out.printf("%-20s %-30s %-30s\n", "Data Nasc.:",
                currentUser.getBirthday().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                newBirthday.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.printf("%-20s %-30s %-30s\n", "Senha:", "********", "********"); // Não exibir senhas
        System.out.printf("%-20s %-30s %-30s\n", "Cargo:", currentUser.getRole(), newRole);
        System.out.println("------------------------------------------------------------------");

        String confirmation = Utilities.readNonEmptyString("\nConfirma a atualização com os novos dados? (SIM/NAO): ").trim().toUpperCase();

        if (confirmation.equals("SIM")) {
            try {
                // Criar um novo objeto User com os novos dados para passar ao serviço
                // ou atualizar o objeto currentUser diretamente com os novos valores.
                // Vou optar por atualizar o currentUser para manter a referência original.
                currentUser.setFullName(newFullName);
                currentUser.setEmail(newEmail);
                currentUser.setPhone(newPhone);
                currentUser.setBirthday(newBirthday);
                currentUser.setPassword(newPassword); // A senha já foi validada e confirmada
                currentUser.setRole(newRole);

                userService.updateUser(currentUser); // Assumindo que updateUser persiste as mudanças
                System.out.println("\nDados do usuário " + currentUser.getFullName() + " atualizados com sucesso!");
            } catch (Exception e) {
                System.err.println("Erro ao atualizar usuário: " + e.getMessage());
            }
        } else {
            System.out.println("\nAtualização cancelada.");
        }
        Utilities.readNonEmptyString("Pressione Enter para continuar...");
    }


    private void deleteUser() {
        System.out.println("\n--- REMOVER USUÁRIO ---");
        String emailToDelete = Utilities.readEmail("Digite o email do usuário que deseja remover: ");

        User userToDelete = userService.getUserByEmail(emailToDelete);

        if (userToDelete == null) {
            System.out.println("Usuário com o email '" + emailToDelete + "' não encontrado.");
            Utilities.readNonEmptyString("Pressione Enter para continuar...");
            return;
        }

        // Confirmação para evitar exclusões acidentais
        System.out.println("\nVocê realmente deseja remover o usuário: " + userToDelete.getFullName() + " (" + userToDelete.getEmail() + ")?");
        String confirmation = Utilities.readNonEmptyString("Digite 'SIM' para confirmar ou qualquer outra coisa para cancelar: ").trim().toUpperCase();

        if (confirmation.equals("SIM")) {
            try {

                userService.deleteUser(userToDelete.getUserId());
                System.out.println("\nUsuário " + userToDelete.getFullName() + " removido com sucesso!");
            } catch (Exception e) {
                System.err.println("Erro ao remover usuário: " + e.getMessage());
            }
        } else {
            System.out.println("\nRemoção cancelada.");
        }
        Utilities.readNonEmptyString("Pressione Enter para continuar...");
    }
}