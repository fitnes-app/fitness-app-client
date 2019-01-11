package com.fitnessapp.client;

import android.content.Intent;
import android.os.AsyncTask;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.fitnessapp.client.Utils.StaticStrings;
import com.fitnessapp.client.Utils.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BaseDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawer;
    protected Toolbar toolbar;
    protected FrameLayout frameLayout;
    protected NavigationView navigationView;
    public int userId;
    public String userMail;
    public FirebaseAuth mAuth;
    public User user;
    public String roleValue;
    private boolean userIsPremium;
    private UrlConnectorGetUser getUser;
    private HttpURLConnection conn;
    private JSONObject client;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( BaseDrawerActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                System.out.println("newToken:" + newToken);

            }
        });
        Intent i = getIntent();
        if(i != null){
            Bundle b = i.getBundleExtra("bundle");
            roleValue = b.getString("userType");
        }
        setContentView(R.layout.activity_menu);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        frameLayout = findViewById(R.id.content_frame);
        drawer = findViewById(R.id.drawer_layout);
        userEmail = getIntent().getExtras().getBundle("bundle").getString("userEmail");
        getUser = new UrlConnectorGetUser();
        getUser.execute();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Fragment f = null;
        navigationView = findViewById(R.id.nav_view);
        if(("Trainer").equals(roleValue)){
            navigationView.inflateMenu(R.menu.activity_trainer_drawer);
            navigationView.setCheckedItem(R.id.tr_main_page_it);
            f = new MainPageTrainerFragment();
        }else{
            navigationView.inflateMenu(R.menu.activity_user_drawer);
            navigationView.setCheckedItem(R.id.main_page_it);
            f = new MainPageFragment();
        }

        if(userIsPremium){
            hideItem(navigationView);
        }

        displaySelectedFragment(f);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public static void hideItem(NavigationView navigationView)
    {
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.bec_premium_it).setVisible(false);
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

        } else if (id == R.id.bec_premium_it && !userIsPremium) {
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

        } else if (id == R.id.profile_trainer_it) {
            fragment = new ProfileTrainerFragment();
            displaySelectedFragment(fragment);

        } else if (id == R.id.tr_main_page_it) {
            fragment = new MainPageTrainerFragment();
            displaySelectedFragment(fragment);

        }else if (id == R.id.asg_users_it) {
            fragment = new AssignedUsersFragment();
            displaySelectedFragment(fragment);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void displaySelectedFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mAuth.signOut();
    }

    private class UrlConnectorGetUser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL(StaticStrings.ipserver + "/client/findByEmail/" + userEmail);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);

                    client = arr.getJSONObject(0);
                    userId = client.getInt("id");
                    userIsPremium = client.getBoolean("is_Premium");

                    System.out.println("The user is premium? " + userIsPremium);
                    System.out.println(client);

                    br.close();
                } else {
                    System.out.println("COULD NOT FIND THE CLIENT");
                    System.out.println("ERROR CODE -> " + conn.getResponseCode());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
