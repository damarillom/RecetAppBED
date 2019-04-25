package com.dam.bed.recetapp_bed;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {
    public String emailId;
    public double imc;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("server/saving-data/fireblog");

    public User(String emailId) {
        this.emailId = emailId;


    }

    public User(String emailId, double imc) {
        this.emailId = emailId;
        this.imc = imc;
    }


    DatabaseReference usersRef = ref.child("users");

}
