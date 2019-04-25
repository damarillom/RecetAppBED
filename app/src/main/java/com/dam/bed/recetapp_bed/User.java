package com.dam.bed.recetapp_bed;

import java.util.Date;

public class User {
    public String emailId;
    public double weight;
    public double height;
    public char gender;
    public Date birthday;
    public int diet;

    public User() {

    }

    public User(String emailId) {
        this.emailId = emailId;
    }

    public String getEmailId() {
        return emailId;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public char getGender() {
        return gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setDiet(int diet) {
        this.diet = diet;
    }

    public int getDiet() {
        return diet;
    }
}
