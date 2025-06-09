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

    private Utilities() {
    }

    // O método cls() foi removido daqui.

    /**
     * Solicita e valida um número inteiro do utilizador, repetindo até que um número válido seja inserido.
     *
     * @param prompt A mensagem a ser exibida ao utilizador.
     * @return O número inteiro validado.
     */
    public static int readIntInput(String prompt) {
        int number;
        while (true) {
            System.out.print(prompt);
            try {
                number = Integer.parseInt(scanner.nextLine().trim());
                return number;
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, insira um número inteiro válido.");
            }
        }
    }

    /**
     * Valida se uma string corresponde a um formato de e-mail válido.
     *
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
     *
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
     *
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
     *
     * @param str A string a ser verificada.
     * @return true se a string tiver conteúdo, false caso contrário.
     */
    public static boolean isNotNullOrEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }


    /**
     * Valida um par de strings de data de check-in e check-out (formato DD/MM/AAAA).
     * O check-in deve ser igual ou posterior à data atual, e o check-out deve ser
     * estritamente posterior ao check-in.
     *
     * @param checkinDateStr  A data de check-in como string.
     * @param checkoutDateStr A data de check-out como string.
     * @return true se as datas forem válidas e seguirem a lógica, false caso contrário.
     */
    public static boolean readCheckInAndCheckOut(String checkinDateStr, String checkoutDateStr) { // Este é um validador, não um leitor interativo
        if (!isNotNullOrEmpty(checkinDateStr) || !isNotNullOrEmpty(checkoutDateStr)) {
            return false;
        }
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withResolverStyle(ResolverStyle.STRICT);

            LocalDate checkinDate = LocalDate.parse(checkinDateStr, dateFormatter);
            LocalDate checkoutDate = LocalDate.parse(checkoutDateStr, dateFormatter);

            // Correção para check-in: deve ser HOJE ou no futuro
            if (checkinDate.isBefore(LocalDate.now())) {
                System.out.println("A data de Check-in não pode ser anterior à data atual.");
                return false;
            }


            return checkoutDate.isAfter(checkinDate);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Valida se uma string corresponde ao formato de número de telefone angolano (9 dígitos, começando com '9').
     *
     * @param phoneNumber O número de telefone a ser validado.
     * @return true se o número for válido, false caso contrário.
     */
    public static boolean isValidPhoneNumberFormat(String phoneNumber) {
        if (!isNotNullOrEmpty(phoneNumber)) {
            return false;
        }
        String phoneRegex = "^9\\d{8}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        return pattern.matcher(phoneNumber).matches();
    }

    /**
     * Valida se um valor numérico é positivo.
     *
     * @param amount O valor a ser validado.
     * @return true se o valor for maior que zero, false caso contrário.
     */
    public static boolean isValidPaymentAmountFormat(double amount) {
        return amount > 0;
    }

    // --- Métodos de Obtenção de Input Validado com Repetição ---

    /**
     * Solicita e valida um e-mail do utilizador, repetindo até que um formato válido seja inserido.
     *
     * @param prompt A mensagem a ser exibida.
     * @return O e-mail validado.
     */
    public static String readEmail(String prompt) {
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
     *
     * @param prompt A mensagem a ser exibida.
     * @return A senha forte validada.
     */
    public static String readPassword(String prompt) {
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
     *
     * @param prompt A mensagem a ser exibida.
     * @return O nome de pessoa validado.
     */
    public static String readPersonName(String prompt) {
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
     *
     * @param prompt A mensagem a ser exibida.
     * @return A string não vazia e validada.
     */

    public static String readNonEmptyString(String prompt) {
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
     * Solicita e valida um número de telefone, repetindo até que seja válido (formato angolano).
     *
     * @param prompt A mensagem a ser exibida.
     * @return O número de telefone validado.
     */
    public static String readPhoneNumber(String prompt) {
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
     *
     * @param prompt A mensagem a ser exibida.
     * @return O valor de pagamento validado.
     */
    public static double readPaymentAmount(String prompt) {
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
     * Lê uma data de nascimento do utilizador, validando o formato (DD/MM/YYYY) e a lógica (idade).
     * Repete a solicitação até que uma data válida seja fornecida.
     *
     * @param prompt Mensagem a ser exibida ao utilizador (ex: "Data de Nascimento").
     * @return A data de nascimento validada como um objeto LocalDate.
     */
    //Por melhorar
    /**
     * Lê uma data de nascimento do utilizador, validando o formato (DD/MM/YYYY) e a lógica (idade).
     * Repete a solicitação até que uma data válida seja fornecida.
     *
     * @param prompt Mensagem a ser exibida ao utilizador (ex: "Data de Nascimento").
     * @return A data de nascimento validada como um objeto LocalDate.
     */
    public static LocalDate readBirthDate(String prompt) {
        // Usa ResolverStyle.STRICT para evitar datas inválidas como 30/02
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withResolverStyle(ResolverStyle.STRICT);
        LocalDate birthDate;
        while (true) {
            String dateString = readNonEmptyString(prompt + " (DD/MM/YYYY): ");
            try {
                birthDate = LocalDate.parse(dateString, formatter);

                LocalDate today = LocalDate.now();
                // Idade mínima de 18 anos
                LocalDate minAgeDate = today.minusYears(18);
                // Idade máxima de 120 anos (para evitar datas absurdas)
                LocalDate maxAgeDate = today.minusYears(120);

                // A data de nascimento deve ser antes ou igual à data que representa 18 anos atrás
                // E deve ser depois ou igual à data que representa 120 anos atrás
                if (birthDate.isAfter(minAgeDate)) {
                    System.out.println("Data de nascimento inválida. O usuário deve ter pelo menos 18 anos de idade.");
                } else if (birthDate.isBefore(maxAgeDate)) {
                    System.out.println("Data de nascimento inválida. O usuário não pode ser tão velho (limite de 120 anos).");
                } else {
                    return birthDate; // Data válida
                }
            } catch (DateTimeParseException e) {
                // Esta exceção é capturada se o formato não for dd/MM/yyyy ou se a data for inválida (ex: 31/02/2024)
                System.out.println("Formato de data inválido ou data inexistente. Por favor, use o formato DD/MM/YYYY (ex: 09/06/2000).");
            }
        }
    }


    /**
     * Solicita e valida um número decimal (double) do utilizador, repetindo até que seja um número válido.
     * Permite o uso de vírgula ou ponto como separador decimal.
     * @param prompt A mensagem a ser exibida ao utilizador.
     * @return O valor double validado.
     */
    public static double readDoubleInput(String prompt) {
        double number;
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().replace(",", "."); // Aceita vírgula ou ponto
                if (input.trim().isEmpty()) {
                    System.out.println("Este campo não pode ser vazio. Por favor, preencha-o.");
                    continue;
                }
                number = Double.parseDouble(input);
                return number;
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, insira um número decimal válido.");
            }
        }
    }

}