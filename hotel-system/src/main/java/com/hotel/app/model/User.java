package com.hotel.app.model;

import java.time.LocalDate;

public class User {


    private Long id;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private LocalDate birthday;
    private Role role;

    public User(String fullName, String email, String password, String phone, LocalDate birthday, Role role){
        this.fullName= fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.birthday = birthday;
        this.role= role;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

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
