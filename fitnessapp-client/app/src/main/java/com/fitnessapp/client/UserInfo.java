package com.fitnessapp.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserInfo extends BaseDrawerActivityTrainer{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_user_info, frameLayout);

        Button buttonUserDetails = (Button) findViewById(R.id.buttonUserDetails);

        buttonUserDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserDetails(v);
            }
        });

        Button buttonUserProgress = (Button) findViewById(R.id.buttonUserProgress);

        buttonUserProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserProgress(v);
            }
        });
        Button buttonUserRoutines = (Button) findViewById(R.id.buttonUserRoutines);

        buttonUserRoutines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserRoutines(v);
            }
        });
    }
    public void openUserDetails(View view) {
        // Prepare el moviment dsde la clase que estas fins a DisplayMessage...
        Intent intent = new Intent(this, UserDetailsActivity.class);
        //Efectua el cambi de activity
        startActivity(intent);
    }
    public void openUserProgress(View view) {
        // Prepare el moviment dsde la clase que estas fins a DisplayMessage...
        Intent intent = new Intent(this, CheckUserProgress.class);
        //Efectua el cambi de activity
        startActivity(intent);
    }
    public void openUserRoutines(View view) {
        // Prepare el moviment dsde la clase que estas fins a DisplayMessage...
        Intent intent = new Intent(this, UserRoutines.class);
        //Efectua el cambi de activity
        startActivity(intent);
    }
    }

