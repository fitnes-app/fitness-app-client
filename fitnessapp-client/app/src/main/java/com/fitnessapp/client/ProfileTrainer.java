package com.fitnessapp.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ProfileTrainer extends BaseDrawerActivityTrainer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_profile_trainer, frameLayout);
    }
}
