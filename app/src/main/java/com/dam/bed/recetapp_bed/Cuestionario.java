package com.dam.bed.recetapp_bed;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Cuestionario extends AppCompatActivity {
    Button buttonOK;
    EditText editTextHeight;
    EditText editTextWeight;
    EditText editTextYear;
    double userHeight;
    double userWeight;
    int userYear;

    RadioGroup radioGender;
    RadioGroup radioDiet;

    String gender;
    String diet;

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
        editTextWeight = (EditText) findViewById(R.id.weight);
        editTextYear = (EditText) findViewById(R.id.year);

        radioGender = (RadioGroup) findViewById(R.id.radiogroup);
        radioDiet = (RadioGroup) findViewById(R.id.radiogroupDiet);

        final int radioMale = findViewById(R.id.radioMale).getId();
        final int radioFemale = findViewById(R.id.radioFemale).getId();
        final int radioOther = findViewById(R.id.radioOther).getId();

        final int radioVegan = findViewById(R.id.radioVegan).getId();
        final int radioVeget = findViewById(R.id.radioVegetarian).getId();
        final int radioOmni = findViewById(R.id.radioOmnivore).getId();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                //System.out.println("*********"+user.getAltura());
                if (user.isQuest()) {
                    editTextHeight.setText(Double.toString(user.getAltura()));
                    editTextWeight.setText(Double.toString(user.getPeso()));
                    editTextYear.setText(Integer.toString(user.getBirthday()));
                    String gender = user.getGender();
                    String diet = user.getDiet();
                    RadioButton radioM = (RadioButton) findViewById(R.id.radioMale);
                    RadioButton radioF = (RadioButton) findViewById(R.id.radioFemale);
                    RadioButton radioO = (RadioButton) findViewById(R.id.radioOther);
                    RadioButton radioVegano = (RadioButton) findViewById(R.id.radioVegan);
                    RadioButton radioVegetarian = (RadioButton) findViewById(R.id.radioVegetarian);
                    RadioButton radioOmnivore = (RadioButton) findViewById(R.id.radioOmnivore);
                    if (gender.equals("M")) {
                        radioM.setChecked(true);
                    } else if (gender.equals("F")) {
                        radioF.setChecked(true);
                    } else if (gender.equals("O")) {
                        radioO.setChecked(true);
                    }
                    if (diet.equals("Omniv")) {
                        radioOmnivore.setChecked(true);
                    } else if (diet.equals("Vegetarian")) {
                        radioVegetarian.setChecked(true);
                    } else if (diet.equals("Vegan")) {
                        radioVegano.setChecked(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("databaseError = " + databaseError);
            }
        };
        String replace = mAuth.getCurrentUser().getEmail().replace("@", "\\").replace(".", "-");
        FirebaseDatabase.getInstance().getReference("users/" + replace).addValueEventListener(valueEventListener);


        System.out.println("uid" + mAuth.getUid());
        //System.out.println("uid current user" + mAuth.getCurrentUser().getUid());
        System.out.println("current user" + mAuth.getCurrentUser());
        //System.out.println("current user email" + mAuth.getCurrentUser().getEmail());

        buttonOK = (Button) findViewById(R.id.acceptButton);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = true;
                //comprobamos que los campos no estén vacíos
                try {
                    userHeight = Double.parseDouble(editTextHeight.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(Cuestionario.this, "Fill Height", Toast.LENGTH_SHORT).show();
                    check = false;
                }
                try {
                    userWeight = Double.parseDouble(editTextWeight.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(Cuestionario.this, "Fill Weight", Toast.LENGTH_SHORT).show();
                    check = false;
                }
                try {
                    userYear = Integer.parseInt(editTextYear.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(Cuestionario.this, "Fill Year", Toast.LENGTH_SHORT).show();
                    check = false;
                }

                int idCheckedGender = radioGender.getCheckedRadioButtonId();
                int idCheckedDiet = radioDiet.getCheckedRadioButtonId();

                if (idCheckedGender == -1) {
                    Toast.makeText(Cuestionario.this, "Select Gender", Toast.LENGTH_SHORT).show();
                    check = false;
                }

                if (idCheckedDiet == -1) {
                    Toast.makeText(Cuestionario.this, "Select Diet", Toast.LENGTH_SHORT).show();
                    check = false;
                }

                //si no están vacíos, continuamos
                if (check) {
                    System.out.println("Button Accepted, hay que enviarlo a la base de datos");

                    //GENDER
                    System.out.println("idChecked" + idCheckedGender);

                    if (idCheckedGender == radioMale) {
                        gender = "M";
                    } else if (idCheckedGender == radioFemale) {
                        gender = "F";
                    } else if (idCheckedGender == radioOther) {
                        gender = "O";
                    }

                    //DIET
                    if (idCheckedDiet == radioVegan) {
                        diet = "Vegan";
                    } else if (idCheckedDiet == radioVeget) {
                        diet = "Vegetarian";
                    } else if (idCheckedDiet == radioOmni) {
                        diet = "Omniv";
                    }

                    String email = mAuth.getCurrentUser().getEmail();


                    System.out.println("userHeight = " + userHeight);
                    System.out.println("userWeight = " + userWeight);
                    System.out.println("userYear = " + userYear);
                    System.out.println("diet = " + diet);
                    System.out.println("gender = " + gender);
                    System.out.println("email = " + email);

                    //Crear Map para actualizar la BD
                    String replacedEmail = email.replace("@", "\\").
                            replace(".", "-");
                    Map<String, Object> datosActualizar = new HashMap<>();

                    datosActualizar.put("birthday", userYear);
                    datosActualizar.put("altura", userHeight);
                    datosActualizar.put("peso", userWeight);
                    datosActualizar.put("gender", gender);
                    datosActualizar.put("diet", diet);
                    datosActualizar.put("quest", true);

                    FirebaseDatabase.getInstance().getReference("users/" + replacedEmail).
                            updateChildren(datosActualizar);

//                DatabaseReference ref = database.getReference("users/"+"amarilleitor96\\gmail-com");
//                FirebaseDatabase.getInstance().getReference().getKey(ref);


                    //Go to main
                    Intent intent = new Intent(Cuestionario.this, MainActivity.class);
                    startActivity(intent);


                }else{
                    System.out.println("Algún campo vacío!");
                }
            }
        });
    }
}



