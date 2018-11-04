package com.fitnessapp.client;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TableLayout;

import com.fitnessapp.client.Utils.MenuNavigatorListener;
import com.fitnessapp.client.Utils.Table;

import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity {

    private ArrayList<String> exercicesTmp = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        MenuNavigatorListener myListener = new MenuNavigatorListener(this);
        navigationView.setNavigationItemSelectedListener(myListener);

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
