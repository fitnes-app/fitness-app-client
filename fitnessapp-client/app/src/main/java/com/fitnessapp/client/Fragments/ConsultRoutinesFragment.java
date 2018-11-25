package com.fitnessapp.client.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.fitnessapp.client.CreateNewRoutine;
import com.fitnessapp.client.R;
import com.fitnessapp.client.RoutineDetailActivity;

import java.util.ArrayList;

public class ConsultRoutinesFragment extends Fragment {

    private ArrayList<String> routines = new ArrayList<>();
    private GridView gv;

    public ConsultRoutinesFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Routines");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_consult_routines, container, false);
        loadRoutines(RootView);
        return RootView;
    }

    private void loadRoutines(View rootView) {
        gv = rootView.findViewById(R.id.gv);
        setRoutines();
        ArrayAdapter<String> aa = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, routines);
        gv.setAdapter(aa);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                openRoutineDetail(v);

            }
        });
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

    public void openRoutineDetail(View view){
        Fragment fragment = new RoutineDetailFragment();
        //Move to routineDetailFragment ??
    }
}
