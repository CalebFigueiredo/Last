package com.hotel.app.model;

import java.time.LocalDate;


/**
 * Representa um usuário no sistema de gestão hoteleira.
 * Inclui detalhes como informações de contato, credenciais de login e função no sistema.
 */
public class User {


    private Integer userId;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private LocalDate birthday;
    private Role role;


    /**
     * @param userId userId do usuario no banco de dados
     * @param email indica o email do usuario
     * @param fullName Nome completo do usuario
     * @param birthday data de nascimento
     * @param password palavra passe
     * @param phone numero de telefone
     * @param role identifica a hierarquia do usuario: Administrador ou Cliente/Hóspede
     *
    */
    // Construtor completo (com ID)

    public User(Integer userId, String fullName, String email, String phone, LocalDate birthday, Role role, String password){
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.birthday = birthday;
        this.role = role;
    }

    /**
     * Construtor sem ID (útil para novos usuários onde o ID será gerado pelo DB)
     * aqui serve para adicionar novos usuarios ao banco de dados, onde o Id será gerado automaticamente
     */
    public User(String fullName, String email,  String phone,  LocalDate birthday, Role role, String password){
        this.fullName= fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.birthday = birthday;
        this.role= role;
    }


    public Integer  getUserId() {return userId;}
    public void setUserId(Integer  userId) {this.userId = this.userId;}

    public String getFullName() {return fullName;}
    public void setFullName(String fullName) {this.fullName = fullName;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}

    public LocalDate getBirthday() {return birthday;}
    public void setBirthday(LocalDate birthday) {this.birthday = birthday;}

    public Role getRole() {return role;}
    public void setRole(Role role) {this.role = role;}

}
