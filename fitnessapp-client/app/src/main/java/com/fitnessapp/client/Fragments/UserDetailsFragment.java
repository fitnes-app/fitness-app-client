package com.fitnessapp.client.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fitnessapp.client.BaseDrawerActivity;
import com.fitnessapp.client.R;
import com.fitnessapp.client.Utils.StaticStrings;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class UserDetailsFragment extends Fragment implements View.OnClickListener {

    private View RootView;
    private TextView usernameText;
    private TextView emailText;
    private TextView telephoneText;
    private TextView heightText;
    private TextView weightText;
    private TextView bodyTypeText;
    private UrlConnectorGetClientDetails userDetails;
    private HttpURLConnection conn;
    private URL url;

    private int clientId;
    private int bodyTypeId;
    private String username ="";
    private String email="";
    private String telephone="";
    private Double height;
    private Double weight;
    private String bodyType="";

    public UserDetailsFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Client Detail");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RootView = inflater.inflate(R.layout.trainer_list_element, container, false);
        clientId = getArguments().getInt("selectedUserId");

        usernameText = RootView.findViewById(R.id.usernameText);
        emailText = RootView.findViewById(R.id.emailText);
        telephoneText = RootView.findViewById(R.id.telephoneText);
        heightText = RootView.findViewById(R.id.heightText);
        weightText = RootView.findViewById(R.id.weightText);
        bodyTypeText = RootView.findViewById(R.id.bodyTypeText);

        Button buttonBack = RootView.findViewById(R.id.userDetailsBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back(v);
            }
        });
        Button contactButton = RootView.findViewById(R.id.contactButton);
        contactButton.setOnClickListener(this);
        Button userDetailsCreateWorkout = RootView.findViewById(R.id.userDetailsCreateWorkout);
        userDetailsCreateWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateRoutinePage(v);
            }
        });
        userDetails = new UrlConnectorGetClientDetails();
        userDetails.execute();

        return RootView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.contactButton:
                if(!("").equals(email)) {
                    Intent mailIntent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.parse("mailto:&to=" + email);
                    mailIntent.setData(data);
                    startActivity(Intent.createChooser(mailIntent, "Send mail..."));
                }
                break;

            default:
                break;
        }
    }

    public void back(View view) {

        Fragment fragment = new MainPageTrainerFragment();
        BaseDrawerActivity bda = (BaseDrawerActivity)getActivity();
        bda.displaySelectedFragment(fragment);
    }
    public void goCreateRoutinePage(View w){
        Fragment fragment = new CreateNewRoutineFragment();

        Bundle b = new Bundle();
        b.putInt("clientId",clientId);
        b.putInt("bodyTypeId",bodyTypeId);
        fragment.setArguments(b);
        BaseDrawerActivity bda = (BaseDrawerActivity) getActivity();
        bda.displaySelectedFragment(fragment);
    }

    private class UrlConnectorGetClientDetails extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                JSONObject client;

                url = new URL(StaticStrings.ipserver  + "/client/" + clientId);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                try {
                    if (conn.getResponseCode() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String output = br.readLine();

                        client = new JSONObject(output);
                        username = client.getString("userName");
                        email= client.getString("mail");
                        telephone= client.getString("telephone");
                        height= client.getDouble("height");
                        weight= client.getDouble("weight");
                        JSONObject subjson = client.getJSONObject("bodyTypeId");
                        bodyType= subjson.getString("bodyTypeValue");
                        bodyTypeId = subjson.getInt("id");

                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                usernameText.setText(username);
                                emailText.setText(email);
                                telephoneText.setText(telephone);
                                heightText.setText(height.toString());
                                weightText.setText(weight.toString());
                                bodyTypeText.setText(bodyType);
                            }
                        });

                        br.close();
                    } else {
                        System.out.println("ERROR CODE -> "+ conn.getResponseCode());
                        return null;
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                conn.disconnect();


            } catch (Exception e) {
                System.out.println("Something went wrong");
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
