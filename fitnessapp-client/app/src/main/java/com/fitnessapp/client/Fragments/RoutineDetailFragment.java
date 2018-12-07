package com.fitnessapp.client.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;

import com.fitnessapp.client.BaseDrawerActivity;
import com.fitnessapp.client.R;
import com.fitnessapp.client.RoutinesActivity;
import com.fitnessapp.client.Utils.Table;

import java.util.ArrayList;

public class RoutineDetailFragment extends Fragment {

    private ArrayList<String> exercicesTmp = new ArrayList<>();
    public RoutineDetailFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Routine Detail");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_routine_detail, container, false);
        initExercices();
        setTable(RootView);
        Button buttonBack = RootView.findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back(v);
            }
        });
        return RootView;
    }

    public void initExercices(){
        exercicesTmp.add("Push ups");
        exercicesTmp.add("Medicine ball");
        exercicesTmp.add("Bulgarian Training Bag");
    }

    public void setTable(View v){
        Table tabla = new Table(getActivity(), (TableLayout)v.findViewById(R.id.tabla));
        tabla.agregarCabecera(R.array.mainPageTable_headers);
        for(int i = 0; i < exercicesTmp.size() ; i++)
        {
            ArrayList<String> elementos = new ArrayList<>();
            elementos.add(exercicesTmp.get(i));
            elementos.add("Casilla [" + i + ", 1]");
            elementos.add("Casilla [" + i + ", 2]");
            tabla.agregarFilaTabla(elementos);
        }
    }
    public void back(View view) {

        Fragment fragment = new ConsultRoutinesFragment();
        BaseDrawerActivity bda = (BaseDrawerActivity)getActivity();
        bda.displaySelectedFragment(fragment);
    }
}
