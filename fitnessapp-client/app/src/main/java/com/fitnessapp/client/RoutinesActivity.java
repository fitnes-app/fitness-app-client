package com.fitnessapp.client;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

public class RoutinesActivity extends BaseDrawerActivity {

    private ArrayList<String> routines = new ArrayList<>();
    private GridView gv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_list_routines, frameLayout);

        gv = findViewById(R.id.gv);
        setRoutines();
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, routines);
        gv.setAdapter(aa);
    }

    public void setRoutines(){
        routines.add("Routine 1");
        routines.add("Routine 2");
        routines.add("Routine 3");
        routines.add("Routine 4");
        routines.add("Routine 5");
        routines.add("Routine 6");
        routines.add("Routine 7");
        routines.add("Routine 8");
        routines.add("Routine 9");
    }
}
