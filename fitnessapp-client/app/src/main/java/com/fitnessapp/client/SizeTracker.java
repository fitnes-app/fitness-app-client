package com.fitnessapp.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SizeTracker extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_size__tracker, frameLayout);
    }
}
