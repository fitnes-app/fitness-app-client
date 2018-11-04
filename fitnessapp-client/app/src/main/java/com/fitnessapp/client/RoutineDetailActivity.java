package com.fitnessapp.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;

import com.fitnessapp.client.utils.Table;

import java.util.ArrayList;

public class RoutineDetailActivity extends AppCompatActivity {

    private ArrayList<String> exercicesTmp = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_detail);
        initExercices();
        setTable();
    }

    public void initExercices(){
        exercicesTmp.add("Push ups");
        exercicesTmp.add("Medicine ball");
        exercicesTmp.add("Bulgarian Training Bag");
    }

    public void setTable(){
        Table tabla = new Table(this, (TableLayout)findViewById(R.id.tabla));
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
}
