package com.hotel.app.view;

import com.hotel.app.model.Booking;
import com.hotel.app.model.BookingStatus;
import com.hotel.app.model.Room;
import com.hotel.app.model.User;
import com.hotel.app.service.BookingService;
import com.hotel.app.service.RoomService;
import com.hotel.app.service.UserService;
import com.hotel.app.util.Utilities;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal; // Importar BigDecimal
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

public class ClientMenu {

    private static Scanner scanner = new Scanner(System.in);
    private static UserService userService;
    private static RoomService roomService;
    private static BookingService bookingService;
    private static EntityManager sharedEntityManager;

    // Construtor para injetar os serviços e o EntityManager
    public ClientMenu(UserService userService, RoomService roomService, BookingService bookingService, EntityManager sharedEntityManager) {
        ClientMenu.userService = userService;
        ClientMenu.roomService = roomService;
        ClientMenu.bookingService = bookingService;
        ClientMenu.sharedEntityManager = sharedEntityManager;
    }

    public void startClientMenu(User currentUser) {
        boolean customerRunning = true;
        while (customerRunning) {
            System.out.println("\n======= SEU ESPAÇO NO HOTEL A-DE-5-ESTRELAS =======");
            System.out.println("        Bem-vindo(a), " + currentUser.getFullName() + "!");
            System.out.println("--------------------------------------------------");
            System.out.println("Como podemos ajudá-lo(a) hoje?");
            System.out.println("1. Reservar um Quarto dos Sonhos");
            System.out.println("2. Ver e Atualizar seu Perfil");
            System.out.println("3. Realizar Pagamento Pendente");
            System.out.println("4. Fazer Check-in/Check-out Online");
            System.out.println("5. Falar com o Suporte (Ajuda e Dúvidas)");
            System.out.println("6. Deixar sua Avaliação e Feedback");
            System.out.println("7. Acompanhar Histórico de Reservas e Cancelar");
            System.out.println("8. Voltar ao Menu Principal");
            System.out.print("Sua escolha: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                customerRunning = handleClientMenuOption(choice, currentUser);
            } catch (NumberFormatException e) {
                System.out.println("\nEntrada inválida. Por favor, digite um número.");
                Utilities.readNonEmptyString("Pressione Enter para tentar novamente...");
            }
        }
    }

    private boolean handleClientMenuOption(int choice, User currentUser) {
        switch (choice) {
            case 1:
               // makeReservation(currentUser);

                System.out.println("\nFazer reservas: Funcionalidade em desenvolvimento!");
                Utilities.readNonEmptyString("Pressione Enter para continuar...");
                return true;
            case 2:
                viewProfile(currentUser);
                return true;
            case 3:
                System.out.println("\nFuncionalidade de Pagamento em breve! Agradecemos sua paciência.");
                Utilities.readNonEmptyString("Pressione Enter para continuar...");
                return true;
            case 4:
                System.out.println("\nCheck-in/Check-out Online: Estamos trabalhando para sua conveniência!");
                Utilities.readNonEmptyString("Pressione Enter para continuar...");
                return true;
            case 5:
                System.out.println("\nSuporte ao Cliente: Em desenvolvimento. Nossa equipe estará online em breve!");
                Utilities.readNonEmptyString("Pressione Enter para continuar...");
                return true;
            case 6:
                System.out.println("\nSua opinião é muito importante! Avaliação e Feedback em construção.");
                Utilities.readNonEmptyString("Pressione Enter para continuar...");
                return true;
            case 7:
                listClientBookings(currentUser);
                return true;
            case 8:
                System.out.println("Voltando ao menu principal. Até logo!");
                return false;
            default:
                System.out.println("Opção inválida. Por favor, escolha um número válido.");
                Utilities.readNonEmptyString("Pressione Enter para tentar novamente...");
                return true;
        }
    }

    // --- MÉTODOS DE OPERAÇÕES DO CLIENTE ---

    private void makeReservation(User currentUser) {

        /*Utilities.cls();
        System.out.println("\n--- Sua Próxima Estadia Começa Aqui! ---");
        System.out.println("Por favor, informe as datas desejadas para sua reserva.");
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Declare as variáveis 'final' fora do loop para garantir o 'effectively final'
        final LocalDate checkInDate;
        final LocalDate checkOutDate;

        while (true) {
            String dateInStr = Utilities.readNonEmptyString("Data de check-in (DD/MM/AAAA): ");
            String dateOutStr = Utilities.readNonEmptyString("Data de check-out (DD/MM/AAAA): ");

            if (Utilities.readCheckInAndCheckOut(dateInStr, dateOutStr)) {
                try {
                    checkInDate = LocalDate.parse(dateInStr, formato);
                    checkOutDate = LocalDate.parse(dateOutStr, formato);
                    break;
                } catch (DateTimeParseException e) {
                    System.out.println("\nFormato de data inválido. Por favor, use DD/MM/AAAA.");
                    // Não há necessidade de imprimir o stack trace para o usuário
                }
            } else {
                System.out.println("\nDatas inválidas. O check-out deve ser após o check-in e o check-in não pode ser no passado.");
                System.out.println("Por favor, verifique suas datas e tente novamente.");
            }
        }

        System.out.println("\n--- Quartos Disponíveis para as Suas Datas ---");
        List<Room> allRooms = roomService.getAllAvailableRooms();
        List<Room> availableRooms = allRooms.stream()
                .filter(room -> bookingService.isRoomAvailable(room.getRoomId(), checkInDate, checkOutDate))
                .toList();

        if (availableRooms.isEmpty()) {
            System.out.println("Lamentamos, mas não há quartos disponíveis para o período selecionado.");
            System.out.println("Por favor, tente outras datas ou entre em contato com o suporte.");
            Utilities.readNonEmptyString("Pressione Enter para voltar...");
            return;
        } else {
            System.out.println("ID | Número | Tipo       | Preço por Noite | Status");
            System.out.println("---------------------------------------------------");
            availableRooms.forEach(room ->
                    System.out.printf("%-2d | %-6s | %-10s | %-15.2f Kz | %s%n",
                            room.getRoomId(),
                            room.getRoomNumber(),
                            room.getType(),
                            room.getPrice().doubleValue(),
                            room.isAvailable() ? "Disponível" : "Ocupado")
            );
        }

        Room selectedRoom = null;
        Integer roomId = null;
        while (selectedRoom == null) {
            System.out.print("\nDigite o ID do quarto que você deseja reservar: ");
            try {
                roomId = Integer.parseInt(scanner.nextLine());
                selectedRoom = availableRooms.stream()
                        .filter(r -> r.getRoomId().equals(roomId))
                        .findFirst()
                        .orElse(null);

                if (selectedRoom == null) {
                    System.out.println("Este ID de quarto não está disponível ou é inválido para as datas selecionadas. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, digite apenas números para o ID do quarto.");
            }
        }

        long numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        BigDecimal totalPrice = selectedRoom.getPrice().multiply(BigDecimal.valueOf(numberOfNights));

        System.out.println("\n--- Detalhes da Sua Reserva ---");
        System.out.println("Hóspede: " + currentUser.getFullName());
        System.out.println("Quarto Escolhido: #" + selectedRoom.getRoomNumber() + " (" + selectedRoom.getType() + ")");
        System.out.println("Entrada: " + checkInDate.format(formato));
        System.out.println("Saída: " + checkOutDate.format(formato));
        System.out.println("Total de Noites: " + numberOfNights);
        System.out.printf("Preço por Noite: %.2f Kz%n", selectedRoom.getPrice().doubleValue());
        System.out.printf("VALOR TOTAL: %.2f Kz%n", totalPrice.doubleValue());

        System.out.print("\nTudo certo? Confirmar esta reserva? (sim/não): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("sim")) {
            Booking newBooking = new Booking(currentUser, selectedRoom, checkInDate, checkOutDate, LocalDate.now(), BookingStatus.PENDING, totalPrice.doubleValue());

            try {
                Booking savedBooking = bookingService.createBooking(newBooking);
                System.out.println("\nReserva realizada com sucesso! Mal podemos esperar para recebê-lo(a)!");
                System.out.println("ID da Sua Reserva: " + savedBooking.getBookingId());
                System.out.println(savedBooking.toString());
            } catch (RuntimeException e) {
                System.out.println("\nOps! Houve um problema ao finalizar sua reserva: " + e.getMessage());
                // e.printStackTrace(); // Para depuração
            }
        } else {
            System.out.println("\nReserva cancelada a seu pedido. Esperamos vê-lo(a) em breve!");
        }
        Utilities.readNonEmptyString("Pressione Enter para voltar ao Menu do Cliente...");
    }
        * */
    }

    private void viewProfile(User user) {
        System.out.println("\n--- Seu Perfil de Cliente ---");
        System.out.println("Aqui estão seus dados cadastrais:");
        System.out.println("---------------------------------");
        System.out.println("ID de Usuário: " + user.getUserId());
        System.out.println("Nome Completo: " + user.getFullName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Telefone: " + user.getPhone());
        System.out.println("Data de Nascimento: " + user.getBirthday().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Status: " + user.getRole()); // Exibir o papel do usuário
        Utilities.readNonEmptyString("\nPressione Enter para voltar ao Menu do Cliente...");
    }

    private void listClientBookings(User currentUser) {
        System.out.println("\n--- Seu Histórico de Reservas ---");
        System.out.println("Verifique suas reservas passadas e futuras aqui.");
        List<Booking> clientBookings = bookingService.listClientBookings(currentUser);

        if (clientBookings.isEmpty()) {
            System.out.println("\nVocê ainda não possui nenhuma reserva registrada conosco.");
            System.out.println("Que tal fazer sua primeira reserva agora mesmo?");
        } else {
            System.out.println("----------------------------------------------------------------------------------");
            System.out.println("ID | Quarto | Check-in    | Check-out    | Data da Reserva | Status   | Total Pago");
            System.out.println("----------------------------------------------------------------------------------");
            for (Booking booking : clientBookings) {
                System.out.printf("%-2d | %-6s | %-12s | %-12s | %-15s | %-9s | %.2f Kz%n",
                        booking.getBookingId(),
                        booking.getRoom().getRoomNumber(),
                        booking.getCheckInDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        booking.getCheckOutDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        booking.getBookingDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), // Nova coluna
                        booking.getStatus(),
                        booking.getTotalPrice());
            }
            System.out.println("----------------------------------------------------------------------------------");
        }
        Utilities.readNonEmptyString("\nPressione Enter para voltar ao Menu do Cliente...");
    }
}