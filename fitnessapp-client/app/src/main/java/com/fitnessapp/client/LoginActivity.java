package com.fitnessapp.client;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button registerButton, loginButton;
    private EditText emailET, passwdET;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout2);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        emailET = findViewById(R.id.loginEmailET);
        passwdET = findViewById(R.id.loginPasswrdET);

        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);

        registerButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        String email = emailET.getText().toString();
        String password = passwdET.getText().toString();
        if(!email.equals("") && password.equals("")){
            switch(view.getId()) {
                case R.id.loginButton:
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        FirebaseUser user = mAuth.getCurrentUser();
                                        System.out.println(myRef.child("Users").child(user.getUid()).getKey());

                                    } else {

                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                case R.id.registerButton:
                    register();
            }
        }
    }

    public void goUserMainPage() {

        Intent intent = new Intent(this, MainPageActivity.class);
        startActivity(intent);
    }

    public void goTrainerMainPage() {

        Intent intent = new Intent(this, TrainerMainPage.class);
        startActivity(intent);
    }

    public void register() {

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
