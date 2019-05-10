package com.dam.bed.recetapp_bed;

public class SingletonRecetApp {
    private static final SingletonRecetApp ourInstance = new SingletonRecetApp();

    public static SingletonRecetApp getInstance() {
        return ourInstance;
    }

    private SingletonRecetApp() {
    }

    public String replaceEmail(String email) {
        String replacedEmail = email.replace("@", "\\").replace(".", "-");
        return replacedEmail;
    }
}
