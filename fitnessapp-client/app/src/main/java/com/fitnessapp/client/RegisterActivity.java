package com.fitnessapp.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        Spinner spinnerRoles = (Spinner) findViewById(R.id.spinnerRoles);
        String[] roles = {"Trainer","Simple User"};
        spinnerRoles.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roles));

        Spinner spinnerSpecialities = (Spinner) findViewById(R.id.spinnerSpecialities);
        String[] specialities = {"Swimming","Crossfit", "Athletism", "Climbing"};
        spinnerSpecialities.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, specialities));
    }


}
