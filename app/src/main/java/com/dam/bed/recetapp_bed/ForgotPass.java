package com.dam.bed.recetapp_bed;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPass extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText inputEmail;
    FirebaseAuth.AuthStateListener mAuthListner;
    @Override
    protected void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(mAuthListner);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_forgot_pass);

        getSupportActionBar().setTitle(R.string.reset_password);

        Button sendEmail = (Button) findViewById(R.id.send_email);
        inputEmail = (EditText) findViewById(R.id.email);
        TextView btnGoLogin = (TextView) findViewById(R.id.login_page);

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("***"+inputEmail.getText().toString());
                mAuth.sendPasswordResetEmail(inputEmail.getText().toString());
                /**Toast.makeText(getApplicationContext(),"Se ha enviado un correo",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ForgotPass.this, Login.class));
                finish();*/
                Snackbar.make(v,"Se ha enviado un mensaje",Snackbar.LENGTH_INDEFINITE)
                        .setAction("GO LOGIN", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ForgotPass.this, Login.class));
                        finish();
                    }
                }).show();
            }
        });
        btnGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPass.this, Login.class));
            }
        });
    }
}
