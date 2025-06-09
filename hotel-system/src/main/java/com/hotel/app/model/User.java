package com.hotel.app.model;

import jakarta.persistence.*;


import java.time.LocalDate;

/**
 * Representa um usuário no sistema de gestão hoteleira.
 * Anotado com JPA para mapeamento para a tabela 'users' no banco de dados.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "fullname", nullable = false)
    private String fullName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Enumerated(EnumType.STRING) // Armazena o enum como String no DB
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "password", nullable = false)
    private String password;

    public User() {}

    /**
     * Construtor completo para criar um objeto User com um ID existente (útil ao recuperar do DB).
     *
     * @param userId O ID único do usuário.
     * @param fullName O nome completo do usuário.
     * @param email O endereço de e-mail do usuário.
     * @param phone O número de telefone do usuário.
     * @param birthday A data de nascimento do usuário.
     * @param role A função do usuário no sistema.
     * @param password A senha do usuário.
     */
    public User(Integer userId, String fullName, String email, String phone, LocalDate birthday, Role role, String password){
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.role = role;
        this.password = password;
    }

    /**
     * Construtor para criar um novo objeto User sem um ID (o ID será gerado pelo DB).
     *
     * @param fullName O nome completo do usuário.
     * @param email O endereço de e-mail do usuário.
     * @param phone O número de telefone do usuário.
     * @param birthday A data de nascimento do usuário.
     * @param role A função do usuário no sistema.
     * @param password A senha do usuário.
     */
    public User(String fullName, String email, String phone, LocalDate birthday, Role role, String password){
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.role = role;
        this.password = password;
    }

    // --- Getters ---
    public Integer getUserId() {return userId;}
    public String getFullName() {return fullName;}
    public String getEmail() {return email;}
    public String getPhone() {return phone;}
    public LocalDate getBirthday() {return birthday;}
    public Role getRole() {return role;}
    public String getPassword() {return password;}

    // --- Setters ---
    public void setUserId(Integer userId) {this.userId = userId;}
    public void setFullName(String fullName) {this.fullName = fullName;}
    public void setEmail(String email) {this.email = email;}
    public void setPhone(String phone) {this.phone = phone;}
    public void setBirthday(LocalDate birthday) {this.birthday = birthday;}
    public void setRole(Role role) {this.role = role;}
    public void setPassword(String password) {this.password = password;}

    @Override
    public String toString() {
        return "User{" +
                "Id do usuario=" + userId +
                ", Nome completo='" + fullName + '\'' +
                ", Email='" + email + '\'' +
                ", numero de telefone='" + phone + '\'' +
                ", data de nascimento=" + birthday +
                ", Hierârquia=" + role +
                '}';
    }
}