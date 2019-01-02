package com.fitnessapp.client.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.fitnessapp.client.BaseDrawerActivity;
import com.fitnessapp.client.CreateNewRoutine;
import com.fitnessapp.client.R;
import com.fitnessapp.client.RoutineDetailActivity;
import com.fitnessapp.client.Utils.StaticStrings;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ConsultRoutinesFragment extends Fragment {

    private ArrayList<String> routines;
    private ListView gv;
    private View RootView ;
    private Boolean isPremium;
    private HashMap<String, Integer> routinesIdLog = new HashMap<String, Integer>();

    private URL url;
    private HttpURLConnection conn;
    private UrlConnectorGetRoutinesData routinesData;

    public ConsultRoutinesFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Routines");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RootView = inflater.inflate(R.layout.fragment_consult_routines, container, false);
        isPremium = getActivity().getIntent().getExtras().getBundle("bundle").getBoolean("isPremium");
        routinesData = new UrlConnectorGetRoutinesData();
        routinesData.execute();
        //loadRoutines(RootView);
        return RootView;
    }

    private void loadRoutines(View rootView) {
        gv = rootView.findViewById(R.id.gv);
        setRoutines();
        ArrayAdapter<String> aa = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, routines);
        gv.setAdapter(aa);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                openRoutineDetail(v);

            }
        });
    }

    public void setRoutines(){
        routines = new ArrayList<String>(routinesIdLog.keySet());
/*        routines.add("Routine 1");
        routines.add("Routine 2");
        routines.add("Routine 3");
        routines.add("Routine 4");
        routines.add("Routine 5");
        routines.add("Routine 6");
        routines.add("Routine 7");
        routines.add("Routine 8");
        routines.add("Routine 9");*/
    }

    public void openRoutineDetail(View view){
        Fragment fragment = new RoutineDetailFragment();
        BaseDrawerActivity bda = (BaseDrawerActivity)getActivity();
        bda.displaySelectedFragment(fragment);
        //Move to routineDetailFragment ??
    }

    private void setNoResultsMessage(View rootView) {
        gv = rootView.findViewById(R.id.gv);
        routines = new ArrayList<String>();
        routines.add(getActivity().getResources().getString(R.string.noResultsMessage));
        ArrayAdapter<String> aa = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, routines);
        gv.setAdapter(aa);
    }
    private class UrlConnectorGetRoutinesData extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try{
                if(isPremium){
                    getAdvancedWorkouts();
                }else{
                    getBasicWorkouts();
                }
            }catch(Exception e){
                System.out.println("ERROR: Something went wrong");
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

        protected void getAdvancedWorkouts(){
            try {

                url = new URL(StaticStrings.ipserver + "/advancedworkout/");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                try {
                    if (conn.getResponseCode() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String output = br.readLine();
                        JSONArray arr = new JSONArray(output);

                        if(arr.length()>0) {
                            for (int i = 0; i < arr.length(); i++) {
                                String workoutName = arr.getJSONObject(i).getString("name");
                                Integer workoutId = arr.getJSONObject(i).getInt("id");
                                routinesIdLog.put(workoutName,workoutId);
                            }
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    loadRoutines(RootView);

                                }
                            });
                        }else{
                            //Advanced_workouts not found
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    setNoResultsMessage(RootView);

                                }
                            });
                        }
                        br.close();
                    } else {
                        System.out.println("ERROR GETTING Advanced_Workouts");
                        System.out.println("ERROR CODE -> " + conn.getResponseCode());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                conn.disconnect();
            }catch (Exception e){
                System.out.println("ERROR: Something went wrong");
                e.printStackTrace();
            }
        }

        protected void getBasicWorkouts() {
            try {
                url = new URL(StaticStrings.ipserver + "/basicworkout/");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                try {
                    if (conn.getResponseCode() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String output = br.readLine();
                        JSONArray arr = new JSONArray(output);

                        if(arr.length()>0) {
                            for (int i = 0; i < arr.length(); i++) {
                                String workoutName = arr.getJSONObject(i).getString("name");
                                Integer workoutId = arr.getJSONObject(i).getInt("id");
                                routinesIdLog.put(workoutName,workoutId);
                            }
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    loadRoutines(RootView);

                                }
                            });
                        }else{
                            //Basic_workouts not found
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    setNoResultsMessage(RootView);

                                }
                            });
                        }
                        br.close();
                    } else {
                        System.out.println("ERROR GETTING Basic_Workouts");
                        System.out.println("ERROR CODE -> " + conn.getResponseCode());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                conn.disconnect();
            }catch (Exception e){
                System.out.println("ERROR: Something went wrong");
                e.printStackTrace();
            }
        }
    }
}
