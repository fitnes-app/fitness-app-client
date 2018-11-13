package com.fitnessapp.client.utils;

public class User {

    private String name, email, password, role, speciality;

    public User(String name,String email,String password,String role,String speciality){
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.speciality = speciality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    @Override
    public String toString(){
        return name + " " + email;
    }

}
