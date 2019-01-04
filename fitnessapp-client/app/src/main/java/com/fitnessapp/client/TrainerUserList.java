package com.fitnessapp.client;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fitnessapp.client.Fragments.ConsultRoutinesFragment;
import com.fitnessapp.client.Fragments.MainPageTrainerFragment;

public class TrainerUserList extends BaseDrawerActivityTrainer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainer_list_element);

       /* TextView tw1 = (TextView) findViewById(R.id.tv1);
        TextView tw2 = (TextView) findViewById(R.id.tv2);
        TextView tw3 = (TextView) findViewById(R.id.tv3);
        TextView tw4 = (TextView) findViewById(R.id.tv4);
        TextView tw5 = (TextView) findViewById(R.id.tv5);
        TextView tw6 = (TextView) findViewById(R.id.tv6);

        String userName = getIntent().getStringExtra("keyUser");
        int userId = getIntent().getIntExtra("keyID",0);
        String userMail = getIntent().getStringExtra("keyMail");
        String userAdd = getIntent().getStringExtra("keyAdd");
        int userHeight = getIntent().getIntExtra("keyHeight",0);
        int userWeight = getIntent().getIntExtra("keyWeight",0);
        String userTel = getIntent().getStringExtra("keyTel");

        tw1.setText(userName);
        tw2.setText("User's mail: " + userMail);
        tw3.setText("User's height: " + userHeight);
        tw4.setText("User's weight: " + userWeight);
        tw5.setText("User's telephone: " + userTel);
        tw6.setText("User's address: " + userAdd);

        Button buttonClickOBJ;
        buttonClickOBJ = (Button) findViewById(R.id.buttonBack);

        buttonClickOBJ.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // DO SOMETHING
            }
        });*/
    }
}
