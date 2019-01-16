package com.fitnessapp.client.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.fitnessapp.client.BaseDrawerActivity;
import com.fitnessapp.client.R;
import com.fitnessapp.client.Utils.StaticStrings;
import com.fitnessapp.client.Utils.Table;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class MainPageFragment extends Fragment {
    private View RootView;
    private ArrayList<String> exercises_names = new ArrayList<>();
    private ArrayList<String> exercises_sets = new ArrayList<>();
    private ArrayList<String> exercises_repetitions = new ArrayList<>();
    private Table tabla;
    private TextView welcome;
    private TextView dailyTipLabel;
    private UrlConnectorGetUserData userData;
    private HttpURLConnection conn;
    private URL url;
    private BaseDrawerActivity activity;

    private String userEmail;
    private String workoutId = "";
    private Boolean isPremium;

    public MainPageFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.titleActivityMainPage);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RootView = inflater.inflate(R.layout.fragment_main_page, container, false);
        activity = (BaseDrawerActivity) getActivity();
        tabla = new Table(activity, (TableLayout) RootView.findViewById(R.id.tabla));
        welcome = RootView.findViewById(R.id.welcomeuser);
        dailyTipLabel = RootView.findViewById(R.id.dailyTipLabel);
        activity.userMail = getActivity().getIntent().getExtras().getBundle("bundle").getString("userEmail");
        userEmail = activity.userMail;
        userData = new UrlConnectorGetUserData();
        userData.execute();

        return RootView;
    }

    public void setTable(){
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
    public void setRestDayLabel(){
        tabla.agregarCabecera(R.array.mainPageTable_restDayLabel);

    }
    public void setNoWorkoutLabel(){
        tabla.agregarCabecera(R.array.mainPageTable_noWorkoutLabel);

    }
     private class UrlConnectorGetUserData extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... params) {
            try {
                JSONObject client;
                JSONObject workout;

                url = new URL(StaticStrings.ipserver  + "/client/findByEmail/" + userEmail);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                String clientUsername = "";
                String workoutDuration = "";

                try {
                    if (conn.getResponseCode() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String output = br.readLine();
                        JSONArray arr = new JSONArray(output);

                        client = arr.getJSONObject(0);
                        activity.client = client;
                        clientUsername = client.getString("userName");
                        welcome.setText("Welcome, " + clientUsername);
                        activity.userId = client.getInt("id");
                        isPremium = client.getBoolean("is_Premium");
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Bundle b = getActivity().getIntent().getExtras().getBundle("bundle");
                                b.putBoolean("isPremium",isPremium);
                                getActivity().getIntent().putExtra("bundle",b);
                            }
                        });
                        if(!isPremium && client.has("basicWorkout")){
                            workout = client.getJSONObject("basicWorkout");
                            workoutDuration = workout.getString("duration");
                            workoutId = workout.getString("id");
                            setBasicWorkout();
                        }else if(isPremium && client.has("advancedWorkout")){
                            workout = client.getJSONObject("advancedWorkout");
                            workoutDuration = workout.getString("duration");
                            workoutId = workout.getString("id");
                            setAdvancedWorkout();
                        }else if(isPremium && client.has("basicWorkout")){
                            workout = client.getJSONObject("basicWorkout");
                            workoutDuration = workout.getString("duration");
                            workoutId = workout.getString("id");
                            setBasicWorkout();
                        }else{
                            //HAS NON WORKOUT ASSIGNED
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    setNoWorkoutLabel();

                                }
                            });
                        }

                        br.close();
                    } else {
                        System.out.println("COULD NOT FIND THE CLIENT");
                        System.out.println("ERROR CODE -> "+ conn.getResponseCode());
                        return null;
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                conn.disconnect();

                JSONArray dailyTips;

                url = new URL(StaticStrings.ipserver  + "/dailytip/");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "text/plain; charset=utf-8");

                try {
                    if (conn.getResponseCode() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String output = br.readLine();
                        dailyTips = new JSONArray(output);
                        Random rn = new Random();
                        int index = rn.nextInt(dailyTips.length());
                        JSONObject dailyTip = (JSONObject) dailyTips.get(index);
                        dailyTipLabel.setText(dailyTip.getString("text"));
                    } else {
                        System.out.println("COULD NOT FIND ANY daily_tip");
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
            activity.setUserInformation();
            super.onPostExecute(result);
        }

        protected void setAdvancedWorkout(){
            try {
                String currentDailyId = "";
                JSONObject currentDaily;
                JSONArray currentDailyExercises = new JSONArray();
                url = new URL(StaticStrings.ipserver + "/dailyadvancedworkout/findByAdvancedWorkoutId/" + workoutId);
                conn = (HttpURLConnection) url.openConnection();

                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);
                    Calendar c = Calendar.getInstance();
                    String dayName = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                    int dayOfWeek = 0;
                    if (dayName.equals("Monday")) {
                        dayOfWeek = 1;
                    } else if (dayName.equals("Tuesday")) {
                        dayOfWeek = 2;
                    } else if (dayName.equals("Wednesday")) {
                        dayOfWeek = 3;
                    } else if (dayName.equals("Thursday")) {
                        dayOfWeek = 4;
                    } else if (dayName.equals("Friday")) {
                        dayOfWeek = 5;
                    } else if (dayName.equals("Saturday")) {
                        dayOfWeek = 6;
                    } else if (dayName.equals("Sunday")) {
                        dayOfWeek = 7;
                    }
                    for (int i = 0; i < arr.length(); i++) {
                        Integer weekDay = arr.getJSONObject(i).getInt("week_day");
                        if (dayOfWeek == weekDay) {
                            //currentDailyId = arr.getJSONObject(i).getString("id");
                            currentDailyExercises = arr.getJSONObject(i).getJSONArray("advancedExercises");
                        }
                    }
                    if(null!=currentDailyExercises && currentDailyExercises.length()>0) {

                        for (int i = 0; i < currentDailyExercises.length(); i++) {
                            exercises_names.add(currentDailyExercises.getJSONObject(i).getString("exerciseName"));
                            exercises_sets.add(String.valueOf(currentDailyExercises.getJSONObject(i).getInt("exerciseSets")));
                            exercises_repetitions.add(String.valueOf(currentDailyExercises.getJSONObject(i).getInt("repetitions")));
                        }
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                setTable();

                            }
                        });
                    }else{
                        //REST DAY
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                setRestDayLabel();

                            }
                        });
                    }

                    br.close();
                } else {
                    System.out.println("COULD NOT FIND THE Advanced_Workout");
                }
                conn.disconnect();
            }catch(Exception e){
                System.out.println("ERROR: Something went wrong");
                e.printStackTrace();
            }
        }

         protected void setBasicWorkout(){
             try {
                 String currentDailyId = "";
                 JSONObject currentDaily;
                 JSONArray currentDailyExercises = new JSONArray();
                 url = new URL(StaticStrings.ipserver + "/dailybasicworkout/findByBasicWorkoutId/" + workoutId);
                 conn = (HttpURLConnection) url.openConnection();

                 if (conn.getResponseCode() == 200) {
                     BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                     String output = br.readLine();
                     JSONArray arr = new JSONArray(output);
                     Calendar c = Calendar.getInstance();
                     String dayName = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                     int dayOfWeek = 0;
                     if (dayName.equals("Monday")) {
                         dayOfWeek = 1;
                     } else if (dayName.equals("Tuesday")) {
                         dayOfWeek = 2;
                     } else if (dayName.equals("Wednesday")) {
                         dayOfWeek = 3;
                     } else if (dayName.equals("Thursday")) {
                         dayOfWeek = 4;
                     } else if (dayName.equals("Friday")) {
                         dayOfWeek = 5;
                     } else if (dayName.equals("Saturday")) {
                         dayOfWeek = 6;
                     } else if (dayName.equals("Sunday")) {
                         dayOfWeek = 7;
                     }
                     for (int i = 0; i < arr.length(); i++) {
                         Integer weekDay = arr.getJSONObject(i).getInt("week_day");
                         if (dayOfWeek == weekDay) {
                             //currentDailyId = arr.getJSONObject(i).getString("id");
                             currentDailyExercises = arr.getJSONObject(i).getJSONArray("basicExercises");
                         }
                     }
                     if(null!=currentDailyExercises && currentDailyExercises.length()>0) {

                         for (int i = 0; i < currentDailyExercises.length(); i++) {
                             exercises_names.add(currentDailyExercises.getJSONObject(i).getString("exerciseName"));
                             exercises_sets.add(String.valueOf(currentDailyExercises.getJSONObject(i).getInt("exerciseSets")));
                             exercises_repetitions.add(String.valueOf(currentDailyExercises.getJSONObject(i).getInt("repetitions")));
                         }
                         getActivity().runOnUiThread(new Runnable() {

                             @Override
                             public void run() {

                                 setTable();

                             }
                         });
                     }else{
                         //REST DAY
                         getActivity().runOnUiThread(new Runnable() {

                             @Override
                             public void run() {

                                 setRestDayLabel();

                             }
                         });
                     }
                     br.close();
                 } else {
                     System.out.println("COULD NOT FIND THE Basic_Workout");
                 }
                 conn.disconnect();
             }catch(Exception e){
                 System.out.println("ERROR: Something went wrong");
                 e.printStackTrace();
             }
         }
    }
}
