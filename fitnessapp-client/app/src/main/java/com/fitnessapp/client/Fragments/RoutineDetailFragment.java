package com.fitnessapp.client.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.HashMap;

public class RoutineDetailFragment extends Fragment {

    private HashMap<Integer, Integer> dailiesIdLog = new HashMap<Integer, Integer>();

    private View RootView;
    private TextView workoutNameText;
    private TextView workoutDurationText;
    private TextView kcltv;
    private ListView lv;
    private Button buttonAssign;

    private Boolean isPremium;
    private String userEmail="";
    private int workoutId;
    private int assignedWorkoutId;
    private int userId;
    private String workoutName = "";
    private int workoutDuration = 0;
    private int avgKcal;

    private UrlConnectorGetWorkoutDetails workoutDetails;
    private UrlConnectorUpdateUser updateUser;
    private HttpURLConnection conn;
    private URL url;
    private JSONObject client;
    private JSONObject workout;

    public RoutineDetailFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.titleActivityRoutineDetail);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RootView = inflater.inflate(R.layout.fragment_routine_detail, container, false);
        isPremium = getArguments().getBoolean("isPremium");
        workoutId = getArguments().getInt("selectedWorkoutId");
        userEmail = getArguments().getString("userEmail");
        workoutNameText = RootView.findViewById(R.id.workoutNameText);
        workoutDurationText = RootView.findViewById(R.id.workoutDurationText);
        buttonAssign = RootView.findViewById(R.id.buttonAssign);
        kcltv = RootView.findViewById(R.id.kcltv);

        Button buttonBack = RootView.findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back(v);
            }
        });
        buttonAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser = new UrlConnectorUpdateUser();
                updateUser.execute();
            }
        });
        workoutDetails = new UrlConnectorGetWorkoutDetails();
        workoutDetails.execute();

        return RootView;
    }

    public void back(View view) {

        Fragment fragment = new ConsultRoutinesFragment();
        BaseDrawerActivity bda = (BaseDrawerActivity)getActivity();
        bda.displaySelectedFragment(fragment);
    }
    public void openDailyDetail(View view, int position) {

        FragmentManager fragmentManager = getFragmentManager();

        PopupRoutineDetailsFragment popupFragment =  PopupRoutineDetailsFragment.newInstance();

        Bundle b = new Bundle();
        b.putBoolean("isPremium",isPremium);
        int dailyId = dailiesIdLog.get(position+1);
        b.putInt("dailyId",dailyId);
        popupFragment.setArguments(b);
        popupFragment.show(fragmentManager,"details");
    }
    private class UrlConnectorGetWorkoutDetails extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (isPremium) {
                    getAdvancedWorkoutDetails();
                } else {
                    getBasicWorkoutDetails();
                }
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        workoutNameText.setText("\t" + workoutName);
                        workoutDurationText.setText("\t" + workoutDuration + " days");
                        kcltv.setText("Avg. Burnt Kcal: " + avgKcal);
                        String[] dailiesTitles = {};
                        if (workoutDuration == 3) {
                            dailiesTitles = getActivity().getResources().getStringArray(R.array.threeDaysWorkoutTitles);
                        } else if (workoutDuration == 5) {
                            dailiesTitles = getActivity().getResources().getStringArray(R.array.fiveDaysWorkoutTitles);
                        }
                        lv = RootView.findViewById(R.id.dailiesList);
                        ArrayAdapter<String> aa = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, dailiesTitles);
                        lv.setAdapter(aa);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View v,
                                                    int position, long id) {

                                openDailyDetail(v, position);
                            }
                        });

                        if (workoutId == assignedWorkoutId) {
                            buttonAssign.setVisibility(View.GONE);
                        } else {
                            buttonAssign.setVisibility(View.VISIBLE);
                        }
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

        protected void getAdvancedWorkoutDetails() {
            try {

                url = new URL(StaticStrings.ipserver + "/client/findByEmail/" + userEmail);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                try {
                    if (conn.getResponseCode() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String output = br.readLine();
                        JSONArray arr = new JSONArray(output);

                        client = arr.getJSONObject(0);
                        userId = client.getInt("id");
                        if (client.has("advancedWorkout")) {
                            assignedWorkoutId = client.getJSONObject("advancedWorkout").getInt("id");
                        }

                        br.close();
                    } else {
                        System.out.println("COULD NOT FIND THE CLIENT");
                        System.out.println("ERROR CODE -> " + conn.getResponseCode());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                url = new URL(StaticStrings.ipserver + "/advancedworkout/" + workoutId);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                try {
                    if (conn.getResponseCode() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String output = br.readLine();

                        workout = new JSONObject(output);
                        workoutName = workout.getString("name");
                        workoutDuration = workout.getInt("duration");

                        getDailyAdvancedWorkouts();
                        br.close();
                    } else {
                        System.out.println("COULD NOT FIND THE Advanced_workout");
                        System.out.println("ERROR CODE -> " + conn.getResponseCode());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                conn.disconnect();
            } catch (Exception e) {
                System.out.println("ERROR: Something went wrong");
                e.printStackTrace();
            }
        }

        protected void getBasicWorkoutDetails() {
            try {

                url = new URL(StaticStrings.ipserver + "/client/findByEmail/" + userEmail);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                try {
                    if (conn.getResponseCode() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String output = br.readLine();
                        JSONArray arr = new JSONArray(output);

                        client = arr.getJSONObject(0);
                        userId = client.getInt("id");
                        if (client.has("basicWorkout")) {
                            assignedWorkoutId = client.getJSONObject("basicWorkout").getInt("id");
                        }

                        br.close();
                    } else {
                        System.out.println("COULD NOT FIND THE CLIENT");
                        System.out.println("ERROR CODE -> " + conn.getResponseCode());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                url = new URL(StaticStrings.ipserver + "/basicworkout/" + workoutId);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                try {
                    if (conn.getResponseCode() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String output = br.readLine();

                        workout = new JSONObject(output);
                        workoutName = workout.getString("name");
                        workoutDuration = workout.getInt("duration");

                        getDailyBasicWorkouts();
                        br.close();
                    } else {
                        System.out.println("COULD NOT FIND THE Basic_workout");
                        System.out.println("ERROR CODE -> " + conn.getResponseCode());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                conn.disconnect();
            } catch (Exception e) {
                System.out.println("ERROR: Something went wrong");
                e.printStackTrace();
            }
        }

        protected void getDailyAdvancedWorkouts() {
            try {

                JSONArray currentDailyExercises = new JSONArray();
                url = new URL(StaticStrings.ipserver + "/dailyadvancedworkout/findByAdvancedWorkoutId/" + workoutId);
                conn = (HttpURLConnection) url.openConnection();

                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);

                    for (int i = 0; i < arr.length(); i++) {
                        Integer dailyWeekDay = arr.getJSONObject(i).getInt("week_day");
                        Integer dailyId = arr.getJSONObject(i).getInt("id");
                        dailiesIdLog.put(dailyWeekDay, dailyId);
                        currentDailyExercises = arr.getJSONObject(i).getJSONArray("advancedExercises");
                        for (int j = 0; j < currentDailyExercises.length(); j++) {
                            avgKcal += currentDailyExercises.getJSONObject(j).getInt("kcal");
                        }
                    }

                    br.close();
                } else {
                    System.out.println("COULD NOT FIND THE Advanced_Workout");
                }
                conn.disconnect();
            } catch (Exception e) {
                System.out.println("ERROR: Something went wrong");
                e.printStackTrace();
            }
        }

        protected void getDailyBasicWorkouts() {
            try {

                JSONArray currentDailyExercises = new JSONArray();
                url = new URL(StaticStrings.ipserver + "/dailybasicworkout/findByBasicWorkoutId/" + workoutId);
                conn = (HttpURLConnection) url.openConnection();

                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);

                    for (int i = 0; i < arr.length(); i++) {
                        Integer dailyWeekDay = arr.getJSONObject(i).getInt("week_day");
                        Integer dailyId = arr.getJSONObject(i).getInt("id");
                        dailiesIdLog.put(dailyWeekDay, dailyId);
                        currentDailyExercises = arr.getJSONObject(i).getJSONArray("basicExercises");
                        for (int j = 0; j < currentDailyExercises.length(); j++) {
                            avgKcal += currentDailyExercises.getJSONObject(j).getInt("kcal");
                        }
                    }

                    br.close();
                } else {
                    System.out.println("COULD NOT FIND THE Basic_Workout");
                }
                conn.disconnect();
            } catch (Exception e) {
                System.out.println("ERROR: Something went wrong");
                e.printStackTrace();
            }
        }
    }
    private class UrlConnectorUpdateUser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {

                URL url = new URL(StaticStrings.ipserver + "/client/" + userId);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                if(isPremium){
                    client.put("advancedWorkout",workout);
                    client.put("basicWorkout",null);
                }else{
                    client.put("basicWorkout",workout);
                    client.put("advancedWorkout",null);
                }
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        buttonAssign.setVisibility(View.GONE);
                    }
                });
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
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
    }

