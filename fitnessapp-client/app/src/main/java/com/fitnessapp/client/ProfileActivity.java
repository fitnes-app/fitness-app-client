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
    private FirebaseUser user;
    private User cUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_profile, frameLayout);
        nameET = findViewById(R.id.profile_name_et);
        emailET = findViewById(R.id.profile_email_et);
        passwordET = findViewById(R.id.profile_password_et);
        addressET = findViewById(R.id.profile_address_et);
        telNumET = findViewById(R.id.profile_telNum_et);
        heightET = findViewById(R.id.profile_height_et);
        weightET = findViewById(R.id.profile_weight_et);
        confChangesbutton = findViewById(R.id.confirmChangesbutton);
        user = mAuth.getCurrentUser();
        //retrieveDataFromDB();
        //retrieveUserFromDB();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //putDataIntoETs();
        System.out.println(cUser.getName());
        System.out.println("ODA");
    }

   /* private void retrieveUserFromDB() {
        myRef.child("Users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }*/

    private void putDataIntoETs() {
        nameET.setText(name);
        emailET.setText(email);
        passwordET.setText(password);
        addressET.setText(address);
        telNumET.setText(telNum);
        heightET.setText(Float.toString(height));
        weightET.setText(Float.toString(weigth));
    }

    /*private void retrieveDataFromDB() {
        getUserName();
        getUserEmail();
        getUserPassword();
        getUserAddress();
        getUserPhone();
        getUserHeight();
        getUserWeight();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getUserEmail() {
        myRef.child("Users").child(user.getUid()).child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                email = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getUserName() {
        myRef.child("Users").child(user.getUid()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getUserPassword() {
        myRef.child("Users").child(user.getUid()).child("password").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                password = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getUserAddress() {
        myRef.child("Users").child(user.getUid()).child("address").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                address = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getUserPhone() {
        myRef.child("Users").child(user.getUid()).child("telNum").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                telNum = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getUserHeight() {
        myRef.child("Users").child(user.getUid()).child("height").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {
                    height = Float.parseFloat(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getUserWeight() {

        myRef.child("Users").child(user.getUid()).child("weight").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                weigth = Float.parseFloat(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }*/

}
