package com.fitnessapp.client.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.fitnessapp.client.ArrayAdapters.RoutineArrayAdapter;
import com.fitnessapp.client.R;
import com.fitnessapp.client.Utils.Routine;
import com.fitnessapp.client.Utils.StaticStrings;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ConsultRoutinesFragment extends Fragment {

    private ArrayList<String> routines;
    private ListView gv;
    private View RootView;
    private Spinner durationSpinner;

    private Boolean isPremium;
    private String userEmail="";
    private HashMap<String, Integer> routinesIdLog = new HashMap<String, Integer>();
    private ArrayList<Routine> routinesOBJ;
    private int workoutDuration;

    private URL url;
    private HttpURLConnection conn;
    private UrlConnectorGetRoutinesData routinesData;

    public ConsultRoutinesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Routines");
        routinesOBJ = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RootView = inflater.inflate(R.layout.fragment_consult_routines, container, false);
        userEmail = getActivity().getIntent().getExtras().getBundle("bundle").getString("userEmail");
        durationSpinner = RootView.findViewById(R.id.durationFilter);
        ArrayAdapter<CharSequence> adapterRoles = ArrayAdapter.createFromResource(getActivity(), R.array.routinesDurationFilter, android.R.layout.simple_spinner_item);
        adapterRoles.setDropDownViewResource(android.R.layout.simple_spinner_item);
        durationSpinner.setAdapter(adapterRoles);
        durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (isPremium && position == 1) {
                    workoutDuration = 5;
                    routinesIdLog.clear();
                    filterAdvancedWorkoutsByDuration filterAdvancedWorkout = new filterAdvancedWorkoutsByDuration();
                    filterAdvancedWorkout.execute();
                } else if (isPremium && position == 2) {
                    workoutDuration = 3;
                    routinesIdLog.clear();
                    filterAdvancedWorkoutsByDuration filterAdvancedWorkout = new filterAdvancedWorkoutsByDuration();
                    filterAdvancedWorkout.execute();
                } else if (!isPremium && position == 1) {
                    workoutDuration = 5;
                    routinesIdLog.clear();
                    filterBasicWorkoutsByDuration filterBasicWorkout = new filterBasicWorkoutsByDuration();
                    filterBasicWorkout.execute();
                } else if (!isPremium && position == 2) {
                    workoutDuration = 3;
                    routinesIdLog.clear();
                    filterBasicWorkoutsByDuration filterBasicWorkout = new filterBasicWorkoutsByDuration();
                    filterBasicWorkout.execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        isPremium = getActivity().getIntent().getExtras().getBundle("bundle").getBoolean("isPremium");
        routinesData = new UrlConnectorGetRoutinesData();
        routinesData.execute();

        return RootView;
    }

    private void loadRoutines(View rootView) {
        gv = rootView.findViewById(R.id.gv);
        setRoutines();
        RoutineArrayAdapter aa = new RoutineArrayAdapter(getActivity(), routinesOBJ);
        gv.setAdapter(aa);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                openRoutineDetail(v, position);

            }
        });
    }

    public void setRoutines() {
        routines = new ArrayList<String>(routinesIdLog.keySet());
    }

    public void openRoutineDetail(View view, int position) {
        Fragment fragment = new RoutineDetailFragment();
        ArrayList<String> keys = new ArrayList<String>(routinesIdLog.keySet());
        String neededKey = keys.get(position);
        int selectedWorkoutId = routinesIdLog.get(neededKey);
        Bundle b = new Bundle();
        b.putBoolean("isPremium",isPremium);
        b.putInt("selectedWorkoutId",selectedWorkoutId);
        b.putString("userEmail",userEmail);
        fragment.setArguments(b);
        BaseDrawerActivity bda = (BaseDrawerActivity) getActivity();
        bda.displaySelectedFragment(fragment);

    }

    private void setNoResultsMessage(View rootView) {
        gv = rootView.findViewById(R.id.gv);
        routines = new ArrayList<>();
        routines.add(getActivity().getResources().getString(R.string.noResultsMessage));
        ArrayAdapter<String> aa = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, routines);
        gv.setAdapter(aa);
    }


private class filterAdvancedWorkoutsByDuration extends AsyncTask<Void,Void,Void>{
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    url = new URL(StaticStrings.ipserver + "/advancedworkout/findByDuration/" + workoutDuration);
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
                                    String workoutName = arr.getJSONObject(i).getString("name");
                                    Integer workoutId = arr.getJSONObject(i).getInt("id");
                                    Integer workoutDuration = arr.getJSONObject(i).getInt("duration");
                                    routinesOBJ.add(new Routine(workoutName,workoutDuration));
                                    routinesIdLog.put(workoutName, workoutId);
                                }
                                getActivity().runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        loadRoutines(RootView);

                                    }
                                });
                            } else {
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

        private class filterBasicWorkoutsByDuration extends AsyncTask<Void,Void,Void> {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    url = new URL(StaticStrings.ipserver + "/basicworkout/findByDuration/" + workoutDuration);
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
                                    String workoutName = arr.getJSONObject(i).getString("name");
                                    Integer workoutId = arr.getJSONObject(i).getInt("id");
                                    Integer workoutDuration = arr.getJSONObject(i).getInt("duration");
                                    routinesOBJ.add(new Routine(workoutName,workoutDuration));
                                    routinesIdLog.put(workoutName, workoutId);
                                }
                                getActivity().runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        loadRoutines(RootView);

                                    }
                                });
                            } else {
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
    private class UrlConnectorGetRoutinesData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (isPremium) {
                    getAdvancedWorkouts();
                } else {
                    getBasicWorkouts();
                }
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

        protected void getAdvancedWorkouts() {
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

                        if (arr.length() > 0) {
                            for (int i = 0; i < arr.length(); i++) {
                                String workoutName = arr.getJSONObject(i).getString("name");
                                Integer workoutId = arr.getJSONObject(i).getInt("id");
                                Integer workoutDuration = arr.getJSONObject(i).getInt("duration");
                                routinesOBJ.add(new Routine(workoutName,workoutDuration));
                                routinesIdLog.put(workoutName, workoutId);
                            }
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    loadRoutines(RootView);

                                }
                            });
                        } else {
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
            } catch (Exception e) {
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

                        if (arr.length() > 0) {
                            for (int i = 0; i < arr.length(); i++) {
                                String workoutName = arr.getJSONObject(i).getString("name");
                                Integer workoutId = arr.getJSONObject(i).getInt("id");
                                Integer workoutDuration = arr.getJSONObject(i).getInt("duration");
                                routinesOBJ.add(new Routine(workoutName,workoutDuration));
                                routinesIdLog.put(workoutName, workoutId);
                            }
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    loadRoutines(RootView);

                                }
                            });
                        } else {
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
            } catch (Exception e) {
                System.out.println("ERROR: Something went wrong");
                e.printStackTrace();
            }
        }
    }
}

