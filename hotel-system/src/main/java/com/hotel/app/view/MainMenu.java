package com.hotel.app.view;

import com.hotel.app.util.Utilities;

import java.util.Scanner;


public class MainMenu {

    static Scanner scanner = new Scanner(System.in);
    // --- Exemplo de uso (main para testes) ---
    public static void main(String[] args) {
        System.out.println("--- Demonstração dos Validadores com Repetição ---");

        String email = Utilities.getValidEmail("Insira o e-mail: ");
        System.out.println("E-mail validado: " + email);

        String password = Utilities.getValidStrongPassword("Insira uma senha forte: ");
        System.out.println("Senha validada: " + password);

        String name = Utilities.getValidPersonName("Insira o nome completo: ");
        System.out.println("Nome validado: " + name);

        String generalInput = Utilities.getNonEmptyString("Insira um valor qualquer (não vazio): ");
        System.out.println("Input não vazio validado: " + generalInput);

        String birthDate = Utilities.getValidBirthDate("Insira a data de nascimento: ");
        System.out.println("Data de nascimento validada: " + birthDate);

        String phoneNumber = Utilities.getValidPhoneNumber("Insira o número de telefone (Angola, ex: 9XXXXXXXX): ");
        System.out.println("Número de telefone validado: " + phoneNumber);

        double amount = Utilities.getValidPaymentAmount("Insira o valor do pagamento: ");
        System.out.println("Valor de pagamento validado: " + amount);

        // Para as datas de check-in/check-out, a validação depende de dois inputs.
        // É mais comum pedir um de cada vez e validar no final, ou criar um método mais complexo
        // que peça ambos sequencialmente. Por simplicidade, farei um exemplo que pede ambos aqui.
        String checkinDate;
        String checkoutDate;
        while(true) {
            checkinDate = Utilities.getNonEmptyString("Insira a data de Check-in (DD/MM/AAAA, não anterior a hoje): ");
            checkoutDate = Utilities.getNonEmptyString("Insira a data de Check-out (DD/MM/AAAA, posterior ao Check-in): ");
            if (Utilities.isValidCheckinCheckoutDatesFormat(checkinDate, checkoutDate)) {
                System.out.println("Datas de Check-in/Out válidas!");
                break;
            } else {
                System.out.println("Erro nas datas de Check-in/Out. Certifique-se de que o check-in não é passado e o check-out é depois do check-in. Tente novamente.");
            }
        }
        System.out.println("Check-in: " + checkinDate + ", Check-out: " + checkoutDate);

        // Fechar o scanner quando não for mais usado (importante em aplicações reais)
        scanner.close();
    }
}
