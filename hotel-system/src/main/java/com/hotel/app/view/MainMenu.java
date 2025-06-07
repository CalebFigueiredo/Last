package com.hotel.app.view;

import com.hotel.app.util.Utilities;
import com.hotel.app.model.Role;

import java.util.Scanner;


public class MainMenu {
    public static final Scanner scanner = new Scanner(System.in);

    public static void mainMenu(){

        int option;
        boolean running = false;

        while (!running){
            try{
                System.out.println("\t**Hotel A 5 Estrelas**");
                System.out.println("Bem-vindo(a)! Um refúgio de luxo espera por você.");
                System.out.println("Garantimos conforto, lazer e segurança superiores.");
                System.out.println("Inicie sua experiência conosco:");
                System.out.println("1- Efetuar Login");
                System.out.println("2- Realizar Cadastro");
                System.out.println("0- Sair");
                System.out.print("Sua preferência: ");
                option = scanner.nextInt();

                switch (option){
                    case 1: //Login
                        break;

                    case 2: //cadastro
                        break;

                    case 0:
                        System.out.println("Obrigado pela sua paciência");
                        System.out.println("Saindo!...");
                        running = false;
                        break;
                    default:
                        System.out.println("Opção Inválida");
                        System.out.println("Tente novamente...");
                        running = false;
                        break;

                }



            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

    }

    public static void login(){

    }
    public static void regist(){

        String fullName, email, password, confirmPassword;
        Role role;

        boolean running = false;
        while (!running){
            try {
                System.out.println("Insere seus dados!");
                fullName = Utilities.readPersonName("Nome Completo: ");

                email = Utilities.readEmail("Email: ");

                repeatPassword:
                password = Utilities.readPassword("Password: ");
                confirmPassword = Utilities.readPassword("Confirmar password: ");

                if (!confirmPassword.equals(password)){
                    running = false;
                    Utilities.cls();
                    System.out.println("A password deve ser a mesma");
                }


            } catch (Exception e) {
                throw new RuntimeException(e);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }

        }

    }
}
