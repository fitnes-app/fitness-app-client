package com.fitnessapp.client.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fitnessapp.client.BaseDrawerActivity;
import com.fitnessapp.client.R;
import com.fitnessapp.client.Utils.StaticStrings;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CoachInformationFragment extends Fragment implements View.OnClickListener {

    private UrlConnectorGetTrainerEmail ucgte;
    private HttpURLConnection conn;
    private URL url;
    private BaseDrawerActivity activity;
    private String trainerMail;
    private Button sendEmailButton;
    public CoachInformationFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Coach Information");
        trainerMail = "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_coach_information, container, false);
        activity = (BaseDrawerActivity) getActivity();
        ucgte.execute();
        sendEmailButton = RootView.findViewById(R.id.contactWithCoach);
        sendEmailButton.setOnClickListener(this);
        return RootView;
    }

    @Override
    public void onClick(View view) {
        if(!("").equals(trainerMail)) {
            Intent mailIntent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.parse("mailto:&to=" + trainerMail);
            mailIntent.setData(data);
            startActivity(Intent.createChooser(mailIntent, "Send mail..."));
        }
    }

    private class UrlConnectorGetTrainerEmail extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... params) {
            try {
                //CREATE CLIENT IN DB
                url = new URL(StaticStrings.ipserver  + "/asignee/findByClientId/" + activity.mAuth);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                int trainerID = -1;
                if(conn.getResponseCode() == 200){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);
                    JSONObject assignee = arr.getJSONObject(0);
                    trainerID = assignee.getInt("trainerID");
                    System.out.println("TRAINER ID: " + trainerID);
                    br.close();
                }else{
                    System.out.println("COULD NOT FIND THE ASIGNATION");
                    return null;
                }
                conn.disconnect();
                if(trainerID == -1) {
                    System.out.println("ASIGNATION NOT EXISTS");
                    return null;
                }
                url = new URL(StaticStrings.ipserver  + "/trainer/" + trainerID);
                conn = (HttpURLConnection) url.openConnection();

                trainerID = -1;
                trainerMail = "";
                if(conn.getResponseCode() == 200){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);
                    JSONObject trainer = arr.getJSONObject(0);
                    trainerID = trainer.getInt("id");
                    trainerMail = trainer.getString("mail");
                    System.out.println("TRAINER ID: " + trainerID);
                    System.out.println("TRAINER MAIL: " + trainerMail);
                    br.close();
                }else{
                    System.out.println("COULD NOT FIND THE TRAINER");
                    return null;
                }
                conn.disconnect();
                if(trainerID == -1 || trainerMail.equals("")) {
                    System.out.println("TRAINER NOT EXISTS");
                    return null;
                }
            } catch (Exception e) {
                System.out.println("User could not be created:" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

}
