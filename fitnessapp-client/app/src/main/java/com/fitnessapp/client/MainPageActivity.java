package com.fitnessapp.client;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.widget.TableLayout;
import com.fitnessapp.client.utils.Table;

import java.util.ArrayList;

public class MainPageActivity extends BaseDrawerActivity {

    private ArrayList<String> exercicesTmp = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main_page, frameLayout);
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
