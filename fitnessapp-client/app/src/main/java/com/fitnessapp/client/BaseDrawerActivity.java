package com.fitnessapp.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.fitnessapp.client.Fragments.MainPageFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BaseDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    Toolbar toolbar;
    FrameLayout frameLayout;
    NavigationView navigationView;

    protected FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        frameLayout = findViewById(R.id.content_frame);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.main_page_it);
        Fragment fragment = new MainPageFragment();
        displaySelectedFragment(fragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Intent i;
        Fragment fragment  = null;
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
            fragment = new MainPageFragment();
            displaySelectedFragment(fragment);

        } else if (id == R.id.profile_it) {
            i = new Intent(this, ProfileActivity.class);
            startActivity(i);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displaySelectedFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
