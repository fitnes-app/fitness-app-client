package com.fitnessapp.client;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AssignedUsers extends BaseDrawerActivityTrainer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.assigned_users, frameLayout);

        ArrayList<String> example = new ArrayList<String>();
        example.add("User 1");
        example.add("User 2");
        example.add("User 3");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,example);
        ListView users = (ListView) findViewById(R.id.users);
        users.setAdapter(adapter);

        users.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 1) {
                            //code specific to first list item
                            Intent myIntent = new Intent(view.getContext(), UserInfo.class);
                            startActivityForResult(myIntent, 0);
                        }
                    }
                }
        );

    }

    }

