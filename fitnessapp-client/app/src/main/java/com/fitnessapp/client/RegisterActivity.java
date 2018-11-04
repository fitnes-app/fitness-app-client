package com.fitnessapp.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
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

    }


}
