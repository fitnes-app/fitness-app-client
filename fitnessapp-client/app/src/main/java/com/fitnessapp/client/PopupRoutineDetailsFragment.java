package com.fitnessapp.client;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import com.fitnessapp.client.Utils.StaticStrings;
import com.fitnessapp.client.Utils.Table;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PopupRoutineDetailsFragment extends DialogFragment {
    private View RootView;
    private TextView popuptitle;
    private Table tabla;
    private TextView kcltv;

    private UrlConnectorGetDailyWorkoutDetails dailyDetails;
    private HttpURLConnection conn;
    private URL url;

    private Boolean isPremium;
    private int dailyId;
    private int avgKcal;
    private ArrayList<String> exercises_names = new ArrayList<>();
    private ArrayList<String> exercises_sets = new ArrayList<>();
    private ArrayList<String> exercises_repetitions = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RootView = inflater.inflate(R.layout.popup_routine_details, container, false);
        kcltv = RootView.findViewById(R.id.kcltv);
        isPremium = getArguments().getBoolean("isPremium");
        dailyId = getArguments().getInt("dailyId");

        dailyDetails = new UrlConnectorGetDailyWorkoutDetails();
        dailyDetails.execute();

        return RootView;
    }
    public static PopupRoutineDetailsFragment newInstance() {
        return new PopupRoutineDetailsFragment();
    }

    public void setTable(View rootView){
        tabla = new Table(getActivity(), (TableLayout) rootView.findViewById(R.id.tablaDailyDetails));
        tabla.agregarCabecera(R.array.mainPageTable_headers);
        for(int i = 0; i < exercises_names.size() ; i++)
        {
            ArrayList<String> elementos = new ArrayList<>();
            elementos.add(exercises_names.get(i));
            elementos.add(exercises_sets.get(i));
            elementos.add(exercises_repetitions.get(i));
            tabla.agregarFilaTabla(elementos);
        }
    }
    private class UrlConnectorGetDailyWorkoutDetails extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (isPremium) {
                    getDailyAdvancedWorkoutDetails();
                } else {
                    getDailyBasicWorkoutDetails();
                }
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        kcltv.setText("Avg. Burnt Kcal: " + avgKcal);
                    }
                });
            } catch (Exception e) {
                System.out.println("ERROR: Something went wrong");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
        protected void getDailyAdvancedWorkoutDetails(){
            try {

                JSONArray currentDailyExercises = new JSONArray();
                url = new URL(StaticStrings.ipserver + "/dailyadvancedworkout/" + dailyId);
                conn = (HttpURLConnection) url.openConnection();

                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONObject dailyWorkout = new JSONObject(output);
                    JSONArray arr = dailyWorkout.getJSONArray("advancedExercises");
                    for (int i = 0; i < arr.length(); i++) {
                        exercises_names.add(arr.getJSONObject(i).getString("exerciseName"));
                        exercises_sets.add(String.valueOf(arr.getJSONObject(i).getInt("exerciseSets")));
                        exercises_repetitions.add(String.valueOf(arr.getJSONObject(i).getInt("repetitions")));
                        avgKcal += arr.getJSONObject(i).getInt("kcal");
                    }
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            setTable(RootView);

                        }
                    });

                    br.close();
                } else {
                    System.out.println("COULD NOT FIND THE DailyAdvanced_Workout");
                }
                conn.disconnect();
            }catch(Exception e){
                System.out.println("ERROR: Something went wrong");
                e.printStackTrace();
            }
        }
        protected void getDailyBasicWorkoutDetails(){
            try {

                JSONArray currentDailyExercises = new JSONArray();
                url = new URL(StaticStrings.ipserver + "/dailybasicworkout/" + dailyId);
                conn = (HttpURLConnection) url.openConnection();

                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONObject dailyWorkout = new JSONObject(output);
                    JSONArray arr = dailyWorkout.getJSONArray("basicExercises");
                    for (int i = 0; i < arr.length(); i++) {
                        exercises_names.add(arr.getJSONObject(i).getString("exerciseName"));
                        exercises_sets.add(String.valueOf(arr.getJSONObject(i).getInt("exerciseSets")));
                        exercises_repetitions.add(String.valueOf(arr.getJSONObject(i).getInt("repetitions")));
                        avgKcal += arr.getJSONObject(i).getInt("kcal");
                    }
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            setTable(RootView);

                        }
                    });

                    br.close();
                } else {
                    System.out.println("COULD NOT FIND THE DailyBasic_Workout");
                }
                conn.disconnect();
            }catch(Exception e){
                System.out.println("ERROR: Something went wrong");
                e.printStackTrace();
            }
        }
    }
}

