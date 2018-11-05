package com.fitnessapp.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class BaseDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    Toolbar toolbar;
    FrameLayout frameLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        frameLayout = (FrameLayout) findViewById(R.id.content_frame);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Intent i;
        if (id == R.id.cons_rout_it) {
            i = new Intent(this, RoutinesActivity.class);
            startActivity(i);
        } else if (id == R.id.size_tr_it) {
            i = new Intent(this, SizeTracker.class);
            startActivity(i);

        } else if (id == R.id.prog_tr_it) {
            i = new Intent(this, ProgressTrackerActivity.class);
            startActivity(i);

        } else if (id == R.id.coach_it) {
            i = new Intent(this, CoachInformation.class);
            startActivity(i);

        } else if (id == R.id.settings_it) {
            i = new Intent(this, Settings.class);
            startActivity(i);

        } else if (id == R.id.bec_premium_it) {
            i = new Intent(this, BecomePremium.class);
            startActivity(i);

        }else if (id == R.id.contact_it) {
            i = new Intent(this, ContactWithUs.class);
            startActivity(i);

        } else if (id == R.id.main_page_it) {
            i = new Intent(this, MainPageActivity.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
