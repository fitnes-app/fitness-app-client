package com.fitnessapp.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        Spinner spinnerRoles = (Spinner) findViewById(R.id.spinnerRoles);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterRoles = ArrayAdapter.createFromResource(this, R.array.rolesOptions, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapterRoles.setDropDownViewResource(android.R.layout.simple_spinner_item);
        // Apply the adapter to the spinner
        spinnerRoles.setAdapter(adapterRoles);

        Spinner spinnerSpecialities = (Spinner) findViewById(R.id.spinnerSpecialities);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterSpecialities = ArrayAdapter.createFromResource(this, R.array.specialitiesOptions, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapterSpecialities.setDropDownViewResource(android.R.layout.simple_spinner_item);
        // Apply the adapter to the spinner
        spinnerSpecialities.setAdapter(adapterSpecialities);

        Button buttonBack = (Button) findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back(v);
            }
        });

        Button buttonRegister = (Button) findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(v);
            }
        });
    }


    public void back(View view) {
        // Prepare el moviment dsde la clase que estas fins a DisplayMessage...
        Intent intent = new Intent(this, MainPageActivity.class);
        //Efectua el cambi de activity
        startActivity(intent);
    }

    public void register(View view) {
        // Prepare el moviment dsde la clase que estas fins a DisplayMessage...
        Intent intent = new Intent(this, MainActivity.class);
        //Efectua el cambi de activity
        startActivity(intent);
    }
}
