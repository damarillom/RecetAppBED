package com.dam.bed.recetapp_bed;

import java.util.Date;

public class User {
    public String emailId;
    public double altura;
    public double peso;
    public int birthday;
    public String gender;

    public User() {

    }

    public User(String emailId) {
        this.emailId = emailId;
        //this.birthday = new Date();
        this.gender = "Z";
    }

    public User(String emailId, double altura, double peso, int birthday, String gender) {
        this.emailId = emailId;
        this.altura = altura;
        this.peso = peso;
        this.birthday = birthday;
        this.gender = gender;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public int getBirthday() {
        return birthday;
    }

    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
