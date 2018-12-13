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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button registerButton, loginButton;
    private EditText emailET, passwdET;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    public String roleValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout2);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();

        emailET = findViewById(R.id.loginEmailET);
        passwdET = findViewById(R.id.loginPasswrdET);

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {}

    @Override
    public void onClick(View view) {
        String email = emailET.getText().toString();
        String password = passwdET.getText().toString();
            switch(view.getId()) {
                case R.id.loginButton:
                    if(!email.equals("") && !password.equals("")) {
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            FirebaseUser user = mAuth.getCurrentUser();
                                            myRef.child("Users").child(user.getUid()).child("role").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    roleValue = dataSnapshot.getValue().toString();
                                                    if (roleValue.equals("Simple User")) {
                                                        goUserMainPage();
                                                    } else {
                                                        goTrainerMainPage();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                }
                                            });
                                        } else {

                                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    break;
                case R.id.registerButton:
                    register();
                default:
                    break;
            };
    }

    private void goUserMainPage() {
        Bundle b = new Bundle();
        b.putString("userType","Simple User");
        Intent intent = new Intent(this, BaseDrawerActivity.class);
        intent.putExtra("bundle",b);
        startActivity(intent);
    }

    private void goTrainerMainPage() {
        Bundle b = new Bundle();
        b.putString("userType","Trainer");
        Intent intent = new Intent(this, BaseDrawerActivity.class);
        intent.putExtra("bundle",b);
        startActivity(intent);
    }

    private void register() {

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
