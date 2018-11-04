package com.fitnessapp.client.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.fitnessapp.client.CoachInformation;
import com.fitnessapp.client.MainPageActivity;
import com.fitnessapp.client.ProgressTrackerActivity;
import com.fitnessapp.client.R;
import com.fitnessapp.client.RoutinesActivity;
import com.fitnessapp.client.Settings;
import com.fitnessapp.client.SizeTracker;

public class MenuNavigatorListener extends Activity implements NavigationView.OnNavigationItemSelectedListener {

    private Context c;

    public MenuNavigatorListener(Context aContext) {
        this.c = aContext;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Intent i;
        if (id == R.id.cons_rout_it) {
            i = new Intent(this.c, RoutinesActivity.class);
            startActivity(i);
        } else if (id == R.id.size_tr_it) {
            i = new Intent(this.c, SizeTracker.class);
            startActivity(i);

        } else if (id == R.id.prog_tr_it) {
            i = new Intent(this.c, ProgressTrackerActivity.class);
            startActivity(i);

        } else if (id == R.id.coach_it) {
            i = new Intent(this.c, CoachInformation.class);
            startActivity(i);

        } else if (id == R.id.settings_it) {
            i = new Intent(this.c, Settings.class);
            startActivity(i);

        } else if (id == R.id.bec_premium_it) {
            i = new Intent(this.c, RoutinesActivity.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
