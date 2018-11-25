package com.fitnessapp.client;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.fitnessapp.client.Utils.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends BaseDrawerActivity {

    private String name, email, password,address, telNum;
    private float height, weigth;
    private Button confChangesbutton;
    private EditText nameET, emailET, passwordET, addressET, telNumET, heightET, weightET;
    private User cUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getLayoutInflater().inflate(R.layout.activity_profile, frameLayout);
        nameET = findViewById(R.id.profile_name_et);
        emailET = findViewById(R.id.profile_email_et);
        passwordET = findViewById(R.id.profile_password_et);
        addressET = findViewById(R.id.profile_address_et);
        telNumET = findViewById(R.id.profile_telNum_et);
        heightET = findViewById(R.id.profile_height_et);
        weightET = findViewById(R.id.profile_weight_et);
        confChangesbutton = findViewById(R.id.confirmChangesbutton);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //putDataIntoETs();
        System.out.println(cUser.getName());
        System.out.println("ODA");
    }
}
