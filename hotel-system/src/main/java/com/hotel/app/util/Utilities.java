package com.hotel.app.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Classe utilitária que fornece métodos estáticos para validação de dados
 * e para obter input validado do utilizador.
 */
public final class Utilities {

    private static final Scanner scanner = new Scanner(System.in);
    private Utilities() {}

    /**
     * Valida se uma string corresponde a um formato de e-mail válido.
     * @param email A string a ser validada.
     * @return true se o e-mail for válido, false caso contrário.
     */
    public static boolean isValidEmailFormat(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return email != null && pattern.matcher(email).matches();
    }

    /**
     * Valida se uma string é uma senha forte, com critérios de tamanho (8-20),
     * letras maiúsculas/minúsculas, números e caracteres especiais.
     * @param password A senha a ser validada.
     * @return true se a senha for forte, false caso contrário.
     */
    public static boolean isValidStrongPasswordFormat(String password) {
        if (password == null || password.length() < 8 || password.length() > 20) {
            return false;
        }
        if (!password.matches(".*[A-Z].*")) return false;
        if (!password.matches(".*[a-z].*")) return false;
        if (!password.matches(".*\\d.*")) return false;
        if (!password.matches(".*[^a-zA-Z0-9 ].*")) return false;
        return true;
    }

    /**
     * Valida se uma string de nome contém apenas letras, espaços, acentuações e apóstrofos,
     * e tem no máximo 50 caracteres.
     * @param name O nome a ser validado.
     * @return true se o nome for válido, false caso contrário.
     */
    public static boolean isValidPersonNameFormat(String name) {
        if (name == null || name.trim().isEmpty() || name.length() > 50) {
            return false;
        }
        // Permite letras (qualquer idioma), espaços, apóstrofo e hífen (para nomes compostos)
        String nameRegex = "^[\\p{L}\\s'\\-]+$";
        Pattern pattern = Pattern.compile(nameRegex);
        return pattern.matcher(name).matches();
    }

    /**
     * Verifica se uma string não é nula, vazia ou composta apenas por espaços em branco.
     * @param str A string a ser verificada.
     * @return true se a string tiver conteúdo, false caso contrário.
     */
    public static boolean isNotNullOrEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * Valida se uma string representa uma data de nascimento válida (formato DD/MM/AAAA)
     * e se a data está no passado.
     * @param birthDateStr A data de nascimento como string.
     * @return true se a data for válida e passada, false caso contrário.
     */
    public static boolean isValidBirthDateFormat(String birthDateStr) {
        if (!isNotNullOrEmpty(birthDateStr)) {
            return false;
        }
        try {
            LocalDate birthDate = LocalDate.parse(birthDateStr,
                    DateTimeFormatter.ofPattern("dd/MM/yyyy").withResolverStyle(ResolverStyle.STRICT));
            // A data deve ser estritamente anterior à data atual
            return birthDate.isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Valida um par de strings de data de check-in e check-out (formato DD/MM/AAAA).
     * O check-in deve ser igual ou posterior à data atual, e o check-out deve ser
     * estritamente posterior ao check-in.
     * @param checkinDateStr A data de check-in como string.
     * @param checkoutDateStr A data de check-out como string.
     * @return true se as datas forem válidas e seguirem a lógica, false caso contrário.
     */
    public static boolean isValidCheckinCheckoutDatesFormat(String checkinDateStr, String checkoutDateStr) {
        if (!isNotNullOrEmpty(checkinDateStr) || !isNotNullOrEmpty(checkoutDateStr)) {
            return false;
        }
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withResolverStyle(ResolverStyle.STRICT);

            LocalDate checkinDate = LocalDate.parse(checkinDateStr, dateFormatter);
            LocalDate checkoutDate = LocalDate.parse(checkoutDateStr, dateFormatter);

            if (checkinDate.isBefore(LocalDate.now())) {
                return false;
            }
            return checkoutDate.isAfter(checkinDate);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Valida se uma string corresponde ao formato de número de telefone angolano (9 dígitos, começando com '9').
     * @param phoneNumber O número de telefone a ser validado.
     * @return true se o número for válido, false caso contrário.
     */
    public static boolean isValidPhoneNumberFormat(String phoneNumber) {
        if (!isNotNullOrEmpty(phoneNumber)) {
            return false;
        }
        String phoneRegex = "^9\\d{8}$"; // Formato angolano: 9 seguido de 8 dígitos
        Pattern pattern = Pattern.compile(phoneRegex);
        return pattern.matcher(phoneNumber).matches();
    }

    /**
     * Valida se um valor numérico é positivo.
     * @param amount O valor a ser validado.
     * @return true se o valor for maior que zero, false caso contrário.
     */
    public static boolean isValidPaymentAmountFormat(double amount) {
        return amount > 0;
    }

    // --- Métodos de Obtenção de Input Validado com Repetição ---

    /**
     * Solicita e valida um e-mail do utilizador, repetindo até que um formato válido seja inserido.
     * @param prompt A mensagem a ser exibida.
     * @return O e-mail validado.
     */
    public static String getValidEmail(String prompt) {
        String email;
        while (true) {
            System.out.print(prompt);
            email = scanner.nextLine();
            if (isValidEmailFormat(email)) {
                return email;
            } else {
                System.out.println("Formato de e-mail inválido. Por favor, tente novamente.");
            }
        }
    }

    /**
     * Solicita e valida uma senha forte do utilizador, repetindo até que seja válida.
     * @param prompt A mensagem a ser exibida.
     * @return A senha forte validada.
     */
    public static String getValidStrongPassword(String prompt) {
        String password;
        while (true) {
            System.out.print(prompt);
            password = scanner.nextLine();
            if (isValidStrongPasswordFormat(password)) {
                return password;
            } else {
                System.out.println("Senha fraca. Deve ter 8-20 caracteres, incluir maiúsculas, minúsculas, números e especiais. Tente novamente.");
            }
        }
    }

    /**
     * Solicita e valida um nome de pessoa, repetindo até que seja válido.
     * @param prompt A mensagem a ser exibida.
     * @return O nome de pessoa validado.
     */
    public static String getValidPersonName(String prompt) {
        String name;
        while (true) {
            System.out.print(prompt);
            name = scanner.nextLine();
            if (isValidPersonNameFormat(name)) {
                return name;
            } else {
                System.out.println("Nome inválido. Verifique caracteres permitidos (letras, espaços, acentos, apóstrofo) e tamanho (máx. 50). Tente novamente.");
            }
        }
    }

    /**
     * Solicita uma string e repete até que ela não seja nula ou vazia.
     * @param prompt A mensagem a ser exibida.
     * @return A string não vazia e validada.
     */
    public static String getNonEmptyString(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine();
            if (isNotNullOrEmpty(input)) {
                return input;
            } else {
                System.out.println("Este campo não pode ser vazio. Por favor, preencha-o.");
            }
        }
    }

    /**
     * Solicita e valida uma data de nascimento, repetindo até que seja válida (formato DD/MM/AAAA e no passado).
     * @param prompt A mensagem a ser exibida.
     * @return A data de nascimento validada.
     */
    public static String getValidBirthDate(String prompt) {
        String birthDateStr;
        while (true) {
            System.out.print(prompt + " (DD/MM/AAAA): ");
            birthDateStr = scanner.nextLine();
            if (isValidBirthDateFormat(birthDateStr)) {
                return birthDateStr;
            } else {
                System.out.println("Data de nascimento inválida. Deve estar no formato DD/MM/AAAA e ser uma data passada. Tente novamente.");
            }
        }
    }

    /**
     * Solicita e valida um número de telefone, repetindo até que seja válido (formato angolano).
     * @param prompt A mensagem a ser exibida.
     * @return O número de telefone validado.
     */
    public static String getValidPhoneNumber(String prompt) {
        String phoneNumber;
        while (true) {
            System.out.print(prompt);
            phoneNumber = scanner.nextLine();
            if (isValidPhoneNumberFormat(phoneNumber)) {
                return phoneNumber;
            } else {
                System.out.println("Número de telefone inválido. Verifique o formato (ex: 9XXXXXXXX para Angola). Tente novamente.");
            }
        }
    }

    /**
     * Solicita e valida um valor de pagamento, repetindo até que seja um número positivo.
     * @param prompt A mensagem a ser exibida.
     * @return O valor de pagamento validado.
     */
    public static double getValidPaymentAmount(String prompt) {
        double amount;
        while (true) {
            System.out.print(prompt);
            try {
                amount = Double.parseDouble(scanner.nextLine().replace(",", "."));
                if (isValidPaymentAmountFormat(amount)) {
                    return amount;
                } else {
                    System.out.println("Valor inválido. O valor deve ser um número positivo. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, insira um número.");
            }
        }
    }

    /**
     * Valida se o PIN de pagamento fornecido corresponde ao PIN de referência.
     * Em cenários reais, o 'referencePin' seria obtido de forma segura (e.g., hash do DB).
     * @param enteredPin O PIN digitado.
     * @param referencePin O PIN esperado para comparação.
     * @return true se os PINs coincidirem e não forem vazios, false caso contrário.
     */
    public static boolean validatePaymentPin(String enteredPin, String referencePin) {
        return isNotNullOrEmpty(enteredPin) && enteredPin.equals(referencePin);
    }


}