package com.fitnessapp.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SizeTracker extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_size__tracker, frameLayout);

        Button buttonBack = (Button) findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back(v);
            }
        });
    }

    public void back(View view) {
        // Prepare el moviment dsde la clase que estas fins a DisplayMessage...
        Intent intent = new Intent(this, MainPageActivity.class);
        //Efectua el cambi de activity
        startActivity(intent);
    }
}
