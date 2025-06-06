package com.hotel.app.model;

/**
 * Define os diferentes papéis (funções) que um usuário pode ter no sistema do hotel.
 * Cliente (hóspede), com acesso para gerenciar suas próprias reservas.
 * Funcionário do hotel, com acesso limitado para operações diárias.
 * Administrador do sistema, com acesso total.
 */
public enum Role {
    GUEST,
    EMPLOYEE,
    ADMINISTRATOR
}