package com.fitnessapp.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class CheckUserProgress extends BaseDrawerActivityTrainer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.checkuserprogress, frameLayout);
    }
}
