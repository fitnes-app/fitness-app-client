package com.fitnessapp.client.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fitnessapp.client.BaseDrawerActivity;
import com.fitnessapp.client.BaseDrawerActivityTrainer;
import com.fitnessapp.client.R;
import com.fitnessapp.client.Utils.StaticStrings;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainPageTrainerFragment extends Fragment {


    private UrlConnectorGetTrainerEmail ucgte;
    private HttpURLConnection conn;
    private URL url;

    private String trainerMail;
    private int trainerID;
    ArrayList<String> assignedUsers = new ArrayList<String>();
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private ListView lw;

    public MainPageTrainerFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        ucgte = new UrlConnectorGetTrainerEmail();
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Trainer Main Page");
        trainerMail = getActivity().getIntent().getExtras().getBundle("bundle").getString("userEmail");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_main_page_trainer, container, false);
        ucgte.execute();
        lw = (ListView) RootView.findViewById(R.id.lis1);
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                assignedUsers);
        lw.setAdapter(adapter);
        return RootView;
    }

    private class UrlConnectorGetTrainerEmail extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                url = new URL(StaticStrings.ipserver + "/trainer/findByEmail/" + trainerMail);
                System.out.println(url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if(conn.getResponseCode() == 200){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);
                    if (arr.length() == 0){
                        System.out.println("The trainer isn't in the DB");
                        return null;
                    }
                    JSONObject tra = arr.getJSONObject(0);

                    trainerID = tra.getInt("id");
                    System.out.println(trainerID);
                }


                url = new URL(StaticStrings.ipserver + "/assigned");

                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray check = new JSONArray(output);
                    int j = 0;
                    int tmpTrainerID;
                    String tmpUser;
                    System.out.println("Length: " + check.length());

                    for (int i = 0; i < check.length(); i++) {
                        JSONObject assig = check.getJSONObject(i);
                        JSONObject tr = assig.getJSONObject("trainerId");
                        tmpTrainerID = tr.getInt("id");

                        System.out.println(tmpTrainerID + "  " + trainerID);
                        if (tmpTrainerID == trainerID) {
                            JSONObject cl = assig.getJSONObject("clientId");
                            tmpUser = cl.getString("userName");
                            System.out.println(tmpUser);
                            assignedUsers.add(tmpUser);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            adapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }
    }
}
