package com.fitnessapp.client.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Spinner;

import com.fitnessapp.client.R;
import com.fitnessapp.client.Utils.StaticStrings;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class PopupCreateDailiesFragment extends DialogFragment {

    private View RootView;
    private Spinner assignDailyExercisesSpinner;
    private ListView assignDailyExercisesLW;
    private Button assignDailyExercisesAddButton;
    private Button doneButtonLabel;

    private HttpURLConnection conn;
    private URL url;
    private UrlConnectorGetMuscularGroupsData muscularGroupsData;
    private FilterExerciseByMuscularGroup filterExercise;
    private UrlConnectorAssignDailies assignDailies;
    private JSONObject dailyJSON;
    private JSONArray advancedExercises = new JSONArray();

    private HashMap<String, Integer> muscularGroupsIdLog = new HashMap<String, Integer>();
    private HashMap<String, Integer> exercisesIdLog = new HashMap<String, Integer>();
    private ArrayList<String> spinnerOptions = new ArrayList<String>();
    private ArrayList<String> exercises = new ArrayList<String>();
    private ArrayList<Integer> alreadyAssignedExercises = new ArrayList<Integer>();
    private ArrayList<String> selectedExercises = new ArrayList<String>();
    private int dailyId = 0;
    private String selectedMuscularGroup="";
    private int selectedMuscularGroupId=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RootView = inflater.inflate(R.layout.popup_add_daily_exercises, container, false);
        dailyId = getArguments().getInt("dailyId");
        assignDailyExercisesSpinner = RootView.findViewById(R.id.assignDailyExercisesSpinner);
        assignDailyExercisesLW = RootView.findViewById(R.id.assignDailyExercisesLW);
        assignDailyExercisesAddButton = RootView.findViewById(R.id.assignDailyExercisesAddButton);
        doneButtonLabel = RootView.findViewById(R.id.doneButtonLabel);

        doneButtonLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        spinnerOptions.add(getResources().getString(R.string.filterByMuscularGroup));
        assignDailyExercisesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position!=0) {
                    selectedMuscularGroup = spinnerOptions.get(position);
                    selectedMuscularGroupId = muscularGroupsIdLog.get(selectedMuscularGroup);
                    exercisesIdLog.clear();
                    filterExercise = new FilterExerciseByMuscularGroup();
                    filterExercise.execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        assignDailyExercisesAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        assignDailyExercisesAddButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                int cntChoice = assignDailyExercisesLW.getCount();

                if(cntChoice>0) {
                    SparseBooleanArray sparseBooleanArray = assignDailyExercisesLW.getCheckedItemPositions();

                    for (int i = 0; i < cntChoice; i++) {
                        if (sparseBooleanArray.get(i)) {
                            selectedExercises.add(assignDailyExercisesLW.getItemAtPosition(i).toString());
                        }
                    }
                    assignDailies = new UrlConnectorAssignDailies();
                    assignDailies.execute();
                }
            }
        });

        muscularGroupsData = new UrlConnectorGetMuscularGroupsData();
        muscularGroupsData.execute();

        return RootView;
    }

    public static PopupCreateDailiesFragment newInstance() {
        return new PopupCreateDailiesFragment();
    }
    private void loadExercises(View rootView) {
        setExercises();
        ArrayAdapter<String> aa = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, exercises);
        assignDailyExercisesLW.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        assignDailyExercisesLW.setAdapter(aa);
        assignDailyExercisesLW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

            }
        });
    }
    public void setExercises() {
        exercises = new ArrayList<String>(exercisesIdLog.keySet());
    }
    private void setNoResultsMessage(View rootView) {
        exercises = new ArrayList<String>();
        exercises.add(getActivity().getResources().getString(R.string.noResultsMessage));
        ArrayAdapter<String> aa = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, exercises);
        assignDailyExercisesLW.setAdapter(aa);
    }

    private class UrlConnectorGetMuscularGroupsData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {

                url = new URL(StaticStrings.ipserver + "/musculargroup/");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                try {
                    if (conn.getResponseCode() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String output = br.readLine();
                        JSONArray arr = new JSONArray(output);

                        for (int i = 0; i < arr.length(); i++) {
                            String muscularGroupName = arr.getJSONObject(i).getString("muscularGroupName");
                            Integer muscularGroupId = arr.getJSONObject(i).getInt("id");
                            muscularGroupsIdLog.put(muscularGroupName, muscularGroupId);
                            spinnerOptions.add(muscularGroupName);
                        }

                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                ArrayAdapter<String> adapterMuscularGroup = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, spinnerOptions);
                                adapterMuscularGroup.setDropDownViewResource(android.R.layout.simple_spinner_item);
                                assignDailyExercisesSpinner.setAdapter(adapterMuscularGroup);

                            }
                        });
                        br.close();
                    } else {
                        System.out.println("ERROR GETTING Muscular_Groups");
                        System.out.println("ERROR CODE -> " + conn.getResponseCode() + "FOR MuscularGroup GET");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                conn.disconnect();
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

    }
    private class FilterExerciseByMuscularGroup extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                //GET WHICH EXERCISES HAS ALREADY ASSIGNED THIS DAILY
                url = new URL(StaticStrings.ipserver + "/dailyadvancedworkout/findDailyExercises/" + dailyId);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                try {
                    if (conn.getResponseCode() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String output = br.readLine();
                        JSONArray arr = new JSONArray(output);

                        if (arr.length()!=0 && arr.length() > 0) {
                            for (int i = 0; i < arr.length(); i++) {
                                Integer exerciseId = arr.getJSONObject(i).getInt("id");
                                alreadyAssignedExercises.add(exerciseId);
                            }
                        }
                        br.close();
                    } else {
                        System.out.println("ERROR GETTING Daily_Advanced_Workout's Exercises");
                        System.out.println("ERROR CODE -> " + conn.getResponseCode());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                conn.disconnect();
                //GET ABLE EXERCISES FILTERED BY MUSCULAR GROUP AND THAT AREN'T ALREADY ASSIGNED TO THAT DAILY
                url = new URL(StaticStrings.ipserver + "/advancedexercise/findByMuscularGroupId/" + selectedMuscularGroupId);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                try {
                    if (conn.getResponseCode() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String output = br.readLine();
                        JSONArray arr = new JSONArray(output);

                        if (arr.length() > 0) {
                            for (int i = 0; i < arr.length(); i++) {
                                String exerciseName = arr.getJSONObject(i).getString("exerciseName");
                                Integer exerciseId = arr.getJSONObject(i).getInt("id");
                                if(!alreadyAssignedExercises.contains(exerciseId)) {
                                    exercisesIdLog.put(exerciseName, exerciseId);
                                }
                            }
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    loadExercises(RootView);

                                }
                            });
                        } else {
                            //Exercises not found for that filter
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    setNoResultsMessage(RootView);

                                }
                            });
                        }
                        br.close();
                    } else {
                        System.out.println("ERROR GETTING Advanced_Exercises");
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
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }
    private class UrlConnectorAssignDailies extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                //GET SELECTED EXERCISES'S JSON
                int exerciseId = 0;
                for(int i=0; i<selectedExercises.size();i++) {
                    exerciseId = exercisesIdLog.get(selectedExercises.get(i));
                    url = new URL(StaticStrings.ipserver + "/advancedexercise/" + exerciseId);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");

                    try {
                        if (conn.getResponseCode() == 200) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            String output = br.readLine();
                            JSONObject exerciseJSON = new JSONObject(output);

                            advancedExercises.put(exerciseJSON);

                            br.close();
                        } else {
                            System.out.println("ERROR GETTING Advanced_Exercise JSON TO BE ASSIGNED");
                            System.out.println("ERROR CODE -> " + conn.getResponseCode());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    conn.disconnect();
                }

                //GET DAILIE'S JSON WHERE WE WILL ADD THE LIST OF EXERCISES'S JSON
                url = new URL(StaticStrings.ipserver + "/dailyadvancedworkout/" + dailyId);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                try {
                    if (conn.getResponseCode() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String output = br.readLine();

                        dailyJSON = new JSONObject(output);

                        br.close();
                    } else {
                        System.out.println("ERROR GETTING Daily_Advanced_Workout JSON");
                        System.out.println("ERROR CODE -> " + conn.getResponseCode());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                conn.disconnect();
                //UPDATE DAILY_ADVANCED_WORKOUT TO ASSIGN ADVANCED_EXERCISES JSON LIST
                URL url = new URL(StaticStrings.ipserver + "/dailyadvancedworkout/" + dailyId);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                dailyJSON.put("advancedExercises",advancedExercises);

                String jsonString = dailyJSON.toString();
                OutputStream os = conn.getOutputStream();
                os.write(jsonString.getBytes());
                os.flush();
                os.close();
                System.out.println("CONNECTION CODE: " + conn.getResponseCode() +" WHEN ASSIGN EXERCISES TO DAILY_ADVANCED_WORKOUT");
                conn.disconnect();
            } catch (Exception e) {
                System.out.println("ERROR: Something went wrong");
                e.printStackTrace();
            }
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    assignDailyExercisesSpinner.setSelection(0);
                    selectedExercises.clear();
                    exercises.clear();
                    ArrayAdapter<String> aa = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, exercises);
                    assignDailyExercisesLW.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    assignDailyExercisesLW.setAdapter(aa);
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }
}
