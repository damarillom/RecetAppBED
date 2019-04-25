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

}
