package com.fitnessapp.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ProfileActivity extends BaseDrawerActivity {

    private String name, email, password, role, speciality, address, telNum;
    private float height, weigth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_profile, frameLayout);
    }
}
