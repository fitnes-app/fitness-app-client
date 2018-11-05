package com.fitnessapp.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class UserDetailsActivity extends BaseDrawerActivityTrainer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.user_details, frameLayout);
    }
}
