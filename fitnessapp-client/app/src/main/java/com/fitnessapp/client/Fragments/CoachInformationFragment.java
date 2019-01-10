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
import java.util.Random;
import java.io.OutputStreamWriter;

public class CoachInformationFragment extends Fragment implements View.OnClickListener {

    private UrlConnectorGetTrainerEmail ucgte;
    private HttpURLConnection conn;
    private HttpURLConnection postConn;
    private URL url;
    private BaseDrawerActivity activity;
    private Button sendEmailButton;
    private String userEmail;

    View RootView;
    String trainerAdd;
    String trainerMail;
    JSONObject trainerSpec;
    String spec;
    String trainerTel;
    String trainerName;
    String trainerPass;

    TextView tw1;
    TextView tw2;
    TextView tw3;
    TextView tw4;
    TextView tw5;
    int assignedUsersToTrainer;
    public CoachInformationFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        ucgte = new UrlConnectorGetTrainerEmail();
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Coach Information");
        ucgte = new UrlConnectorGetTrainerEmail();
        userEmail = getActivity().getIntent().getExtras().getBundle("bundle").getString("userEmail");
        trainerMail = "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RootView = inflater.inflate(R.layout.fragment_coach_information, container, false);
        activity = (BaseDrawerActivity) getActivity();
        ucgte.execute();
        sendEmailButton = RootView.findViewById(R.id.contactWithCoach);
        sendEmailButton.setOnClickListener(this);
        tw1 = RootView.findViewById(R.id.textView);
        tw2 = RootView.findViewById(R.id.textView5);
        tw3 = RootView.findViewById(R.id.textView6);
        tw4 = RootView.findViewById(R.id.textView7);
        tw5 = RootView.findViewById(R.id.textUsers);
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
                // Firstly, we get the client ID since we need it in order to assign him a trainer
                url = new URL(StaticStrings.ipserver  + "/client/findByEmail/" + userEmail);
                System.out.println(url);

                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                int clientID = -1;
                String clientName;
                String clientPass;
                String clientMail;
                float clientWeight;
                float clientHeight;
                JSONObject clientBody;
                JSONObject clientBasW;
                JSONObject clientAdvW;
                String clientTel;
                String clientAdd;
                boolean clientPrem;

                if(conn.getResponseCode() == 200){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);
                    JSONObject cli = arr.getJSONObject(0);

                    clientID = cli.getInt("id");
                    clientName = cli.getString("userName");
                    clientPass = cli.getString("userPassword");
                    clientMail = cli.getString("mail");
                    clientWeight = cli.getInt("weight");
                    clientHeight = cli.getInt("height");
                    clientBody = cli.getJSONObject("bodyTypeId");
                    clientTel = cli.getString("telephone");
                    clientAdd = cli.getString("address");
                    clientAdvW = cli.getJSONObject("advancedWorkout");
                    clientBasW = cli.getJSONObject("basicWorkout");
                    clientPrem = cli.getBoolean("is_Premium");

                    System.out.println("CLIENT ID: " + clientID);
                    br.close();
                }else{
                    System.out.println("COULD NOT FIND THE CLIENT");
                    return null;
                }
                conn.disconnect();
                if(clientID == -1) {
                    System.out.println("CLIENT DOESN'T EXIST");
                    return null;
                }

                // Secondly, we get the trainer list and decide which one will be assigned to the client
                int trainerID = 0;

                url = new URL(StaticStrings.ipserver  + "/trainer");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                trainerMail = "";
                if(conn.getResponseCode() == 200){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);

                    // We get a random trainer from the list of trainers
                    Random rand = new Random();
                    int r = rand.nextInt(arr.length());
                    JSONObject rec = arr.getJSONObject(r);
                    trainerID = rec.getInt("id");
                    trainerMail = rec.getString("mail");
                    trainerName = rec.getString("userName");
                    trainerPass = rec.getString("userPassword");
                    trainerSpec = rec.getJSONObject("specialityId");
                    spec = trainerSpec.getString("specialityName");
                    trainerTel = rec.getString("telephone");
                    trainerAdd = rec.getString("address");

                    System.out.println("TRAINER ID: " + trainerID);
                    System.out.println("TRAINER MAIL: " + trainerMail);
                    br.close();
                }else{
                    System.out.println("COULD NOT FIND THE TRAINER");
                    return null;
                }
                conn.disconnect();
                if(trainerID == -1 || trainerMail.equals("")) {
                    System.out.println("TRAINER DOESN'T EXIST");
                    return null;
                }

                // Before continuing we have to check that the client isn't assigned yet
                int tmpClientID = -1;
                int tmpTrainerID = -1;
                url = new URL(StaticStrings.ipserver  + "/assigned");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if(conn.getResponseCode() == 200){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray check = new JSONArray(output);
                    int j = 0;
                    assignedUsersToTrainer = 0;
                    for(int i = 0; i < check.length(); i++){
                        JSONObject assig = check.getJSONObject(i);
                        JSONObject tr = assig.getJSONObject("trainerId");
                        tmpTrainerID = tr.getInt("id");

                        System.out.println(tmpTrainerID + "  " + trainerID);
                        if (tmpTrainerID == trainerID){
                            assignedUsersToTrainer++;
                        }
                    }

                    publishProgress();
                    System.out.println(j);

                    for(int i = 0; i < check.length(); i++){
                        JSONObject assig = check.getJSONObject(i);
                        JSONObject cl = assig.getJSONObject("clientId");
                        tmpClientID = cl.getInt("id");
                        System.out.println(tmpClientID + " " + clientID);
                        if (tmpClientID == clientID){ // If the client is already assigned we just stop here
                            System.out.println("THE CLIENT IS ALREADY ASSIGNED");
                            return null;
                        }
                    }
                }

                // Finally, we assign the trainer to the client by posting the values to the "assigned" table
                url = new URL(StaticStrings.ipserver  + "/assigned/");
                postConn = (HttpURLConnection) url.openConnection();
                postConn.setRequestMethod("POST");
                postConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                postConn.setRequestProperty("Accept", "application/json");
                postConn.setDoOutput(true);

                JSONObject clientJson = new JSONObject()
                    .put("id", clientID)
                    .put("userName", clientName)
                    .put("userPassword", clientPass)
                    .put("mail", clientMail)
                    .put("weight", clientWeight)
                    .put("height", clientHeight)
                    .put("bodyTypeId", clientBody)
                    .put("telephone", clientTel)
                    .put("address", clientAdd)
                    .put("advancedWorkout", clientAdvW)
                    .put("basicWorkout", clientBasW)
                    .put("is_Premium", clientPrem);

                System.out.println("Client Json: " + clientJson);

                JSONObject trainerJson = new JSONObject()
                        .put("id", trainerID)
                        .put("userName", trainerName)
                        .put("userPassword", trainerPass)
                        .put("mail", trainerMail)
                        .put("specialityId", trainerSpec)
                        .put("telephone", trainerTel)
                        .put("address", trainerAdd);

                System.out.println("Trainer Json: " + trainerJson);

                String assignation = new JSONObject()
                        .put("clientId", clientJson)
                        .put("trainerId", trainerJson)
                        .toString();

                OutputStreamWriter os = new OutputStreamWriter(postConn.getOutputStream());
                os.write(assignation);
                os.flush();
                os.close();

                System.out.println("Final Json: " + assignation);
                System.out.println("CONNECTION CODE: " + postConn.getResponseCode());

                postConn.disconnect();

            } catch (Exception e) {
                System.out.println("User could not be created:" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            tw1.setText(trainerName);
            tw2.setText(trainerMail);
            tw3.setText(trainerTel);
            tw4.setText("Trainer's speciality: " + spec);
            tw5.setText("Number of assigned users to him: " + assignedUsersToTrainer);
            super.onPostExecute(result);
        }
    }

}
