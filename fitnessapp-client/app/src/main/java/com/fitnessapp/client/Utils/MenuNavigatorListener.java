package com.fitnessapp.client.Utils;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.fitnessapp.client.CoachInformation;
import com.fitnessapp.client.ProgressTrackerActivity;
import com.fitnessapp.client.R;
import com.fitnessapp.client.RoutinesActivity;
import com.fitnessapp.client.Settings;
import com.fitnessapp.client.Size_Tracker;

public class MenuNavigatorListener extends Activity implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Intent i;
        if (id == R.id.cons_rout_it) {
            i = new Intent(this, RoutinesActivity.class);
            startActivity(i);
        } else if (id == R.id.size_tr_it) {
            i = new Intent(this, Size_Tracker.class);
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
            i = new Intent(this, RoutinesActivity.class);
            startActivity(i);

        }
/*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);*/
        return true;
    }
}
