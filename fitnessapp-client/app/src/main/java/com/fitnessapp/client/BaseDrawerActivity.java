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

import com.fitnessapp.client.Fragments.AssignedUsersFragment;
import com.fitnessapp.client.Fragments.BecomePremiumFragment;
import com.fitnessapp.client.Fragments.CoachInformationFragment;
import com.fitnessapp.client.Fragments.ConsultRoutinesFragment;
import com.fitnessapp.client.Fragments.ContactWithUsFragment;
import com.fitnessapp.client.Fragments.MainPageFragment;
import com.fitnessapp.client.Fragments.MainPageTrainerFragment;
import com.fitnessapp.client.Fragments.ProfileFragment;
import com.fitnessapp.client.Fragments.ProfileTrainerFragment;
import com.fitnessapp.client.Fragments.ProgressTrackerFragment;
import com.fitnessapp.client.Fragments.SettingsFragment;
import com.fitnessapp.client.Fragments.SizeTrackerFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BaseDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawer;
    protected Toolbar toolbar;
    protected FrameLayout frameLayout;
    protected NavigationView navigationView;
    protected FirebaseAuth mAuth;
    protected FirebaseDatabase mDatabase;
    protected DatabaseReference myRef;

    public String roleValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();
        getUserType();
        frameLayout = findViewById(R.id.content_frame);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.main_page_it);
    }

    private void getUserType() {
        FirebaseUser user = mAuth.getCurrentUser();
        myRef.child("Users").child(user.getUid()).child("role").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roleValue = dataSnapshot.getValue().toString();
                if (roleValue.equals("Simple User")) {
                    Fragment fragment = new MainPageFragment();
                    displaySelectedFragment(fragment);
                    //Cargar dinàmicamente el menú del Sample user
                } else {
                    Fragment fragment = new MainPageTrainerFragment();
                    displaySelectedFragment(fragment);
                    //Cargar dinàmicamente el menú del Trainer
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Fragment fragment  = null;
        if (id == R.id.cons_rout_it) {
            fragment = new ConsultRoutinesFragment();
            displaySelectedFragment(fragment);
        } else if (id == R.id.size_tr_it) {
            fragment = new SizeTrackerFragment();
            displaySelectedFragment(fragment);

        } else if (id == R.id.prog_tr_it) {
            fragment = new ProgressTrackerFragment();
            displaySelectedFragment(fragment);

        } else if (id == R.id.coach_it) {
            fragment = new CoachInformationFragment();
            displaySelectedFragment(fragment);

        } else if (id == R.id.settings_it) {
            fragment = new SettingsFragment();
            displaySelectedFragment(fragment);

        } else if (id == R.id.bec_premium_it) {
            fragment = new BecomePremiumFragment();
            displaySelectedFragment(fragment);

        }else if (id == R.id.contact_it) {
            fragment = new ContactWithUsFragment();
            displaySelectedFragment(fragment);

        } else if (id == R.id.main_page_it) {
            fragment = new MainPageFragment();
            displaySelectedFragment(fragment);

        } else if (id == R.id.profile_it) {
            fragment = new ProfileFragment();
            displaySelectedFragment(fragment);
        } else if (id == R.id.asg_users_it) {
            fragment = new AssignedUsersFragment();
            displaySelectedFragment(fragment);

        } else if (id == R.id.profile_it) {
            fragment = new ProfileTrainerFragment();
            displaySelectedFragment(fragment);
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
