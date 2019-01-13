package com.fitnessapp.client.Utils;

import java.io.Serializable;

public class User implements Serializable {

    private String name, email, password, role, speciality,address,bodyType, telNum;
    private float height, weigth;

    public User(){}

    public User(String name, String email, String telNum){
        this.name = name;
        this.email = email;
        this.telNum = telNum;
    }
    public User(String name,String email,String password,String role,String speciality){
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.speciality = speciality;
        this.address = "";
        this.bodyType ="";
        this.telNum = "";
        this.height = 0f;
        this.weigth = 0f;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeigth() {
        return weigth;
    }

    public void setWeigth(float weigth) {
        this.weigth = weigth;
    }

    @Override
    public String toString(){
        return name + " " + email;
    }

}
