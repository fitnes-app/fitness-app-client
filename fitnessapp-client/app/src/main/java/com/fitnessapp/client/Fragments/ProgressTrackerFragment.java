package com.fitnessapp.client.Fragments;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.fitnessapp.client.BaseDrawerActivity;
import com.fitnessapp.client.QuestionActivity;
import com.fitnessapp.client.R;
import com.fitnessapp.client.Utils.StaticStrings;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ProgressTrackerFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private LineChart mChart;
    private Boolean isPremium;
    private String workoutId = "", workoutDuration = "";
    private ArrayList<String> exercicesNames;
    private ArrayList<JSONObject> dailyExercices, dailyTrackers;
    private UrlConnectorGetProgress ucgp;
    private UrlConnectorGetExercices ucge;
    private UrlConnectorPostTracker ucpt;
    private boolean isAdvancedWorkout;
    private URL url;
    private HttpURLConnection conn;
    private BaseDrawerActivity activity;
    private View rootView;
    private Button itButton;


    public ProgressTrackerFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Progress Tracker");
        dailyExercices = new ArrayList<>();
        exercicesNames = new ArrayList<>();
        dailyTrackers = new ArrayList<>();
        ucgp = new UrlConnectorGetProgress();
        ucge = new UrlConnectorGetExercices();
        ucpt = new UrlConnectorPostTracker();
        isAdvancedWorkout = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_progress_tracker, container, false);
        itButton = rootView.findViewById(R.id.itButton);
        itButton.setOnClickListener(this);
        activity = (BaseDrawerActivity)getActivity();
        ucge.execute();
        ucgp.execute();
        loadData(rootView);
        return rootView;
    }


    private void loadData(View rootView) {
        mChart = rootView.findViewById(R.id.linechart);
        // add data
        setData();
        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        mChart.setDescription("");
        Spinner repsSpinner = rootView.findViewById(R.id.spinnerReps);
        Spinner setsSpinner = rootView.findViewById(R.id.spinnerSets);

        setsSpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.setsArray)));
        repsSpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.repsArray)));
    }

    private void setData() {
        ArrayList<String> xVals = setXAxisValues();

        ArrayList<Entry> yVals = setYAxisValues();

        LineDataSet set1;

        set1 = new LineDataSet(yVals, "Burnt kcal");
        set1.setFillAlpha(110);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets
        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);
        // set data
        mChart.setData(data);

    }

    private ArrayList<String> setXAxisValues(){
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("10");
        xVals.add("20");
        xVals.add("30");
        xVals.add("30.5");
        xVals.add("40");

        return xVals;
    }

    private ArrayList<Entry> setYAxisValues(){
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        yVals.add(new Entry(60, 0));
        yVals.add(new Entry(48, 1));
        yVals.add(new Entry(70.5f, 2));
        yVals.add(new Entry(100, 3));
        yVals.add(new Entry(180.9f, 4));

        return yVals;
    }

    public void setExerciceSpinnerData(View rootView){
        Spinner exercices = rootView.findViewById(R.id.spinnerRoutines);
        exercices.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, this.exercicesNames));
        exercices.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        ucpt.execute();
    }

    private class UrlConnectorGetExercices extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... params) {
            try {

                URL url = new URL(StaticStrings.ipserver + "/client/" + activity.userId);
                System.out.println(url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                JSONObject workout, user;
                int userID = -1;

                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);
                    user = arr.getJSONObject(0);
                    userID = user.getInt("id");
                    isPremium = user.getBoolean("is_Premium");
                    System.out.println("USER: " + user);

                    if (user.has("basicWorkout")) {
                        isAdvancedWorkout = false;
                        workout = user.getJSONObject("basicWorkout");
                        workoutDuration = workout.getString("duration");
                        workoutId = workout.getString("id");
                        setBasicWorkout();
                    } else if (user.has("advancedWorkout")) {
                        isAdvancedWorkout = true;
                        workout = user.getJSONObject("advancedWorkout");
                        workoutDuration = workout.getString("duration");
                        workoutId = workout.getString("id");
                        setAdvancedWorkout();
                    }
                    br.close();
                } else {
                    System.out.println("COULD NOT FIND USER");
                    return null;
                }

                conn.disconnect();
                if (userID == -1) {
                    System.out.println("USER NOT EXISTS");
                    return null;
                }
            } catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

        protected void setAdvancedWorkout(){
            try {
                String currentDailyId = "";
                JSONObject currentDaily;
                JSONArray currentDailyExercises = new JSONArray();
                url = new URL(StaticStrings.ipserver + "/dailyadvancedworkout/findByAdvancedWorkoutId/" + workoutId);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

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
                            exercicesNames.add(currentDailyExercises.getJSONObject(i).getString("exerciseName"));
                            dailyExercices.add(currentDailyExercises.getJSONObject(i));
                        }
                    }
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            setExerciceSpinnerData(rootView);
                        }
                    });

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
                            currentDailyExercises = arr.getJSONObject(i).getJSONArray("basicExercises");
                        }
                    }
                    if(null!=currentDailyExercises && currentDailyExercises.length()>0) {

                        for (int i = 0; i < currentDailyExercises.length(); i++) {
                            exercicesNames.add(currentDailyExercises.getJSONObject(i).getString("exerciseName"));
                        }
                    }

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            setExerciceSpinnerData(rootView);
                        }
                    });
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

    private class UrlConnectorGetProgress extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... params) {

            try {
                if(isAdvancedWorkout) {
                    url = new URL(StaticStrings.ipserver + "/advancedClientTracking/findByClientId/" + activity.userId);
                    conn = (HttpURLConnection) url.openConnection();

                    if (conn.getResponseCode() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String output = br.readLine();
                        JSONArray arr = new JSONArray(output);
                        if (null != arr && arr.length() > 0) {

                            for (int i = 0; i < arr.length(); i++) {
                                dailyTrackers.add(arr.getJSONObject(i));
                            }
                        }
                        br.close();
                    }
                } else {
                    url = new URL(StaticStrings.ipserver + "/basicClientTracking/findByClientId/" + activity.userId);
                    conn = (HttpURLConnection) url.openConnection();

                    if (conn.getResponseCode() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String output = br.readLine();
                        JSONArray arr = new JSONArray(output);
                        if (null != arr && arr.length() > 0) {

                            for (int i = 0; i < arr.length(); i++) {
                                dailyTrackers.add(arr.getJSONObject(i));
                            }
                        }
                        br.close();
                    }
                }
                conn.disconnect();
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
    }


    private class UrlConnectorPostTracker extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... params) {

            try {
                if(isAdvancedWorkout) {
                    url = new URL(StaticStrings.ipserver + "/dailyadvancedworkout/findByAdvancedWorkoutId/" + workoutId);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    /*{"userName":"asdf3", "userPassword":"asdf2", "mail":"asdf@asdf.com","weight":2,"height":2,"bodyTypeId":{"id":1,"body_type_value":1},"telephone":"asdf", "address":"asdfdsd"}*/
                    String jsonString = new JSONObject()
                            .put("userName", user.getName())
                            .put("userPassword", user.getPassword())
                            .put("mail", user.getEmail())
                            .put("weight", user.getWeigth())
                            .put("height", user.getHeight())
                            .put("bodyTypeId", subjson)
                            .put("telephone", user.getTelNum())
                            .put("address", user.getAddress())
                            .put("is_Premium", isPremium)
                            .toString();

                    OutputStream os = conn.getOutputStream();
                    os.write(jsonString.getBytes());
                    os.flush();
                    os.close();
                    System.out.println("CONNECTION CODE: " + conn.getResponseCode());
                } else {
                    url = new URL(StaticStrings.ipserver + "/dailybasicworkout/findByAdvancedWorkoutId/" + workoutId);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);


                    /*{"userName":"asdf3", "userPassword":"asdf2", "mail":"asdf@asdf.com","weight":2,"height":2,"bodyTypeId":{"id":1,"body_type_value":1},"telephone":"asdf", "address":"asdfdsd"}*/
                    String jsonString = new JSONObject().toString();

                    OutputStream os = conn.getOutputStream();
                    os.write(jsonString.getBytes());
                    os.flush();
                    os.close();
                    System.out.println("CONNECTION CODE: " + conn.getResponseCode());
                }
                conn.disconnect();
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
    }
}
