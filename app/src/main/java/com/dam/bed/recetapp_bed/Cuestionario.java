package com.dam.bed.recetapp_bed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Cuestionario extends AppCompatActivity {

    Button buttonOK;
    EditText editTextHeight;
    EditText editTextWeight;
    EditText editTextYear;
    String userHeight;
    String userWeight;
    String userYear;

    //firebaseauth
    private FirebaseAuth mAuth;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("recetappbed");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuestionario);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();

        editTextHeight = (EditText) findViewById(R.id.height);
        editTextWeight= (EditText) findViewById(R.id.weight);
        editTextYear = (EditText) findViewById(R.id.year);



        System.out.println("uid" + mAuth.getUid());
        System.out.println("uid current user" + mAuth.getCurrentUser().getUid());
        System.out.println("current user" + mAuth.getCurrentUser());
        System.out.println("current user email" + mAuth.getCurrentUser().getEmail());


        buttonOK = (Button) findViewById(R.id.acceptButton);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Button Accepted, hay que enviarlo a la base de datos");
                userHeight = editTextHeight.getText().toString();
                userWeight = editTextWeight.getText().toString();
                userYear = editTextYear.getText().toString();
                System.out.println("userHeight = " + userHeight);
                System.out.println("userWeight = " + userWeight);
                System.out.println("userYear = " + userYear);
                //if not null
//                FirebaseDatabase.getInstance().getReference("users").
//                        setValue(new User(mAuth.getUid(), Double.parseDouble(userHeight),
//                                Double.parseDouble(userWeight), Integer.parseInt(userYear),
//                                "Z"));
                String email = mAuth.getCurrentUser().getEmail();
                String replaceEmail = email.replace("@", "\\").
                        replace(".", "-");
                Map<String,Object> datosActualizar = new HashMap<>();

                datosActualizar.put("birthday",userYear);
                datosActualizar.put("altura",userHeight);
                datosActualizar.put("peso",userWeight);
                datosActualizar.put("gender","Z");
                FirebaseDatabase.getInstance().getReference("users/" + replaceEmail).updateChildren(datosActualizar);

//                DatabaseReference ref = database.getReference("users/"+"amarilleitor96\\gmail-com");
//                FirebaseDatabase.getInstance().getReference().getKey(ref);
            }
        });

    }


}
