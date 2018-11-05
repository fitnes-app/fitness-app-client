package com.fitnessapp.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BecomePremium extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.become_premium, frameLayout);
    }
}
