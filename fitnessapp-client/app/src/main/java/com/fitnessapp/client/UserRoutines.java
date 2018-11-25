package com.fitnessapp.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class UserRoutines extends BaseDrawerActivityTrainer {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.user_routines, frameLayout);

        ArrayList<String> currentRoutines = new ArrayList<String>();
        currentRoutines.add("Routine X");

        ArrayAdapter<String> adapterCurrent = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,currentRoutines);
        ListView currentlyRoutines = findViewById(R.id.currentlyRoutines);
        currentlyRoutines.setAdapter(adapterCurrent);

        currentlyRoutines.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            //code specific to first list item
                            Intent myIntent = new Intent(view.getContext(), RoutineDetailActivity.class);
                            startActivityForResult(myIntent, 0);
                        }

                    }
                }
        );

        ArrayList<String> otherRoutinesList = new ArrayList<String>();
        otherRoutinesList.add("Routine 1");
        otherRoutinesList.add("Routine 10");
        otherRoutinesList.add("Routine 23");

        ArrayAdapter<String> adapterOther = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,otherRoutinesList);
        ListView otherRoutines = (ListView) findViewById(R.id.otherRoutines);
        otherRoutines.setAdapter(adapterOther);

        otherRoutines.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            //code specific to first list item
                            Intent myIntent = new Intent(view.getContext(), RoutineDetailActivity.class);
                            startActivityForResult(myIntent, 0);
                        }
                        if (position == 1) {
                            //code specific to first list item
                            Intent myIntent = new Intent(view.getContext(), RoutineDetailActivity.class);
                            startActivityForResult(myIntent, 0);
                        }
                        if (position == 2) {
                            //code specific to first list item
                            Intent myIntent = new Intent(view.getContext(), RoutineDetailActivity.class);
                            startActivityForResult(myIntent, 0);
                        }
                    }
                }
        );

        FloatingActionButton addRoutine = findViewById(R.id.addRoutine);
        addRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              openCreateNewRoutine(view);
            }
        });
    }
    public void openCreateNewRoutine(View view) {
        // Prepare el moviment dsde la clase que estas fins a DisplayMessage...
        Intent intent = new Intent(this, CreateNewRoutine.class);
        //Efectua el cambi de activity
        startActivity(intent);
    }
}
