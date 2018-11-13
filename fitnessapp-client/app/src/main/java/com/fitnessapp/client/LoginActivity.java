package com.fitnessapp.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginlayout);
        Spinner spinner = findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.loginOptions, R.layout.spinnerlayout);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> spn,
                                               android.view.View v,
                                               int posicion,
                                               long id) {


                        if(spn.getItemAtPosition(posicion).toString().equals("User Login")){
                            goUserMainPage(v);
                        }
                        if(spn.getItemAtPosition(posicion).toString().equals("Trainer Login")){
                            goTrainerMainPage(v);
                        }

                    }
                    public void onNothingSelected(AdapterView<?> spn) {
                    }
                });
        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(v);
            }
        });
    }

    public void goUserMainPage(View view) {

        Intent intent = new Intent(this, MainPageActivity.class);
        startActivity(intent);
    }

    public void goTrainerMainPage(View view) {

        Intent intent = new Intent(this, TrainerMainPage.class);
        //Efectua el cambi de activity
        startActivity(intent);
    }

    public void register(View view) {

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
