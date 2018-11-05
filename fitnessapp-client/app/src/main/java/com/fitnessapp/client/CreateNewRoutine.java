package com.fitnessapp.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class CreateNewRoutine extends BaseDrawerActivityTrainer {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.create_new_routine, frameLayout);

    }
}
