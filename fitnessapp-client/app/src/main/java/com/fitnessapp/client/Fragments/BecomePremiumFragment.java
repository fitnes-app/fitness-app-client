package com.fitnessapp.client.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BecomePremiumFragment extends Fragment {

    private UrlConnectorUpdateUser updateUser;
    private UrlConnectorGetUser getUser;
    private HttpURLConnection conn;
    private Button buttonBecome;
    private TextView txt;
    private String userEmail;
    private JSONObject client;
    private int userId;
    private URL url;
    private boolean userIsPremium;

    public BecomePremiumFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Become Premium");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_become_premium, container, false);
        buttonBecome = RootView.findViewById(R.id.contract);
        txt = RootView.findViewById(R.id.contractCoachTerms2);

        userEmail = getActivity().getIntent().getExtras().getBundle("bundle").getString("userEmail");
        getUser = new UrlConnectorGetUser();
        getUser.execute();

        buttonBecome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser = new UrlConnectorUpdateUser();
                updateUser.execute();
            }
        });
        txt.setVisibility(RootView.VISIBLE);
        return RootView;
    }

    private class UrlConnectorUpdateUser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                url = new URL(StaticStrings.ipserver + "/client/" + userId);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);


                client.put("is_Premium", true);

                String jsonString = client.toString();
                OutputStream os = conn.getOutputStream();
                os.write(jsonString.getBytes());
                os.flush();
                os.close();
                System.out.println("CONNECTION CODE: " + conn.getResponseCode());
                conn.disconnect();
            } catch (Exception e) {
                System.out.println("User could not be updated: ");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            if(userIsPremium == true) {
                txt.setText("You're already Premium! Congratulations!");
                buttonBecome.setVisibility(View.GONE);
            }
            super.onPostExecute(result);
        }
    }

    private class UrlConnectorGetUser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                url = new URL(StaticStrings.ipserver + "/client/findByEmail/" + userEmail);
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

        @Override
        protected void onPostExecute(Void result){
            if(userIsPremium == true) {
                txt.setText("You're already Premium! Congratulations!");
                buttonBecome.setVisibility(View.GONE);
            }
            super.onPostExecute(result);
        }

    }
}


