package com.dam.bed.recetapp_bed;

public class User {
    public String emailId;
    public double imc;

    public User() {

    }

    public User(String emailId) {
        this.emailId = emailId;


    }

    public User(String emailId, double imc) {
        this.emailId = emailId;
        this.imc = imc;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public double getImc() {
        return imc;
    }

    public void setImc(double imc) {
        this.imc = imc;
    }
}
