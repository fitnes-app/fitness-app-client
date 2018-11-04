package com.fitnessapp.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class CoachInformation extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.coach_information, frameLayout);
    }
}
