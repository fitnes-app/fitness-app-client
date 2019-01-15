package com.fitnessapp.client.Fragments;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fitnessapp.client.BaseDrawerActivity;
import com.fitnessapp.client.R;
import com.fitnessapp.client.Utils.StaticStrings;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLIntegrityConstraintViolationException;

public class CreateNewRoutineFragment extends Fragment{

    private View RootView;
    private EditText newRoutineNameText;
    private Spinner newRoutineSpinner;
    private TextView newRoutineBodyTypeText;
    private Button createNewRoutineContinueBut;

    private int clientId;
    private int bodyTypeId;
    private String bodyTypeValue="";
    private int workoutDuration=0;
    private String newRoutineName="";
    private int newWorkoutId=0;
    private int [] wDays;

    private UrlConnectorGetBodyTypeInfo bodyTypeInfo;
    private UrlConnectorCreateNewRoutine createRoutine;
    private HttpURLConnection conn;
    private URL url;
    private JSONObject bodyTpeJSON;
    private JSONObject workout;
    private JSONObject client;

    public CreateNewRoutineFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Create New Routine");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RootView = inflater.inflate(R.layout.fragment_create_routine, container, false);
        clientId = getArguments().getInt("clientId");
        bodyTypeId = getArguments().getInt("bodyTypeId");

        newRoutineNameText = RootView.findViewById(R.id.newRoutineNameText);
        newRoutineSpinner = RootView.findViewById(R.id.newRoutineSpinner);
        newRoutineBodyTypeText = RootView.findViewById(R.id.newRoutineBodyTypeText);
        createNewRoutineContinueBut = RootView.findViewById(R.id.createNewRoutineContinueBut);

        ArrayAdapter<CharSequence> adapterRoles = ArrayAdapter.createFromResource(getActivity(), R.array.routinesDurationPicker, android.R.layout.simple_spinner_item);
        adapterRoles.setDropDownViewResource(android.R.layout.simple_spinner_item);
        newRoutineSpinner.setAdapter(adapterRoles);
        newRoutineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position == 0){
                    workoutDuration = -1;
                }else if(position == 1){
                    workoutDuration = 5;
                }else if(position == 2){
                    workoutDuration = 3;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        createNewRoutineContinueBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!"".equals(newRoutineNameText.getText().toString())
                        && null!=newRoutineNameText.getText().toString()
                        && workoutDuration!=-1) {
                    newRoutineName = newRoutineNameText.getText().toString();
                    createRoutine = new UrlConnectorCreateNewRoutine();
                    createRoutine.execute();
                }else{
                    Toast.makeText(getActivity(), R.string.emptyFields,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        bodyTypeInfo = new UrlConnectorGetBodyTypeInfo();
        bodyTypeInfo.execute();
        return RootView;
    }
    public void goNextPage() {
        Fragment fragment = new CreateDailiesFragment();

        Bundle b = new Bundle();
        b.putInt("newWorkoutId",newWorkoutId);
        b.putInt("workoutDuration",workoutDuration);
        fragment.setArguments(b);
        BaseDrawerActivity bda = (BaseDrawerActivity) getActivity();
        bda.displaySelectedFragment(fragment);

    }
    private class UrlConnectorGetBodyTypeInfo  extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params){
            try {

                url = new URL(StaticStrings.ipserver + "/bodytype/" + bodyTypeId);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                try {
                    if (conn.getResponseCode() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String output = br.readLine();
                        bodyTpeJSON = new JSONObject(output);

                        bodyTypeValue = bodyTpeJSON.getString("bodyTypeValue");
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                newRoutineBodyTypeText.setText(bodyTypeValue);
                            }
                        });
                        br.close();
                    } else {
                        System.out.println("ERROR GETTING BodyType");
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

    private class UrlConnectorCreateNewRoutine  extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params){
            //CREATE NEW ADVANCED_WORKOUT
            try {

                URL url = new URL(StaticStrings.ipserver + "/advancedworkout/");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String jsonString = new JSONObject()
                        .put("bodyTypeId", bodyTpeJSON)
                        .put("duration", workoutDuration)
                        .put("name", newRoutineName)
                        .toString();

                OutputStream os = conn.getOutputStream();
                os.write(jsonString.getBytes());
                os.flush();
                os.close();
                System.out.println("CONNECTION CODE: " + conn.getResponseCode() + "FOR GET WORKOUT");
                conn.disconnect();

            } catch (Exception e) {
                if(e.equals( new SQLIntegrityConstraintViolationException())){
                    Toast.makeText(getActivity(), "ERROR: Already exists a workout with this name",
                            Toast.LENGTH_SHORT).show();
                }else {
                    System.out.println("AdvancedWorkout could not be created: ");
                }
                e.printStackTrace();
            }
            //GET JUST CREATED ABOVE WORKOUT WITH HIS ID
            try {

                URL url = new URL(StaticStrings.ipserver + "/advancedworkout/findByName/" + newRoutineName);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");

                try {
                    if (conn.getResponseCode() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String output = br.readLine();
                        workout = new JSONObject(output);
                        newWorkoutId = workout.getInt("id");
                        br.close();
                    } else {
                        System.out.println("AdvancedWorkout not found");
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
            //CREATE RELATED DAILY_ADVANCED_WORKOUTS
            try {
                if(workoutDuration==5){
                    wDays = new int[]{1 , 2 , 3 , 4 ,5};
                }else if(workoutDuration == 3){
                    wDays = new int[]{1 , 3 , 5};
                }
                for(int i=0; i<workoutDuration; i++) {
                    URL url = new URL(StaticStrings.ipserver + "/dailyadvancedworkout/");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    String jsonString = new JSONObject()
                            .put("advancedWorkoutId", workout)
                            .put("week_day", wDays[i])
                            .toString();

                    OutputStream os = conn.getOutputStream();
                    os.write(jsonString.getBytes());
                    os.flush();
                    os.close();
                    System.out.println("CONNECTION CODE: " + conn.getResponseCode() + "FOR POST DAILY_WORKOUT");
                    conn.disconnect();
                }
            } catch (Exception e) {
                System.out.println("DailyAdvancedWorkout could not be created: ");
                e.printStackTrace();
            }
            //GET THE USER TO MODIFY
            try {

                URL url = new URL(StaticStrings.ipserver + "/client/" + clientId);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");

                try {
                    if (conn.getResponseCode() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String output = br.readLine();
                        client = new JSONObject(output);

                        br.close();
                    } else {
                        System.out.println("Client not found");
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
            //ASSIGN THE NEW WORKOUT TO THE USER
            try {

                URL url = new URL(StaticStrings.ipserver + "/client/" + clientId);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);


                        client.put("advancedWorkout", workout);
                        client.put("basicWorkout", null);

                String jsonString = client.toString();
                OutputStream os = conn.getOutputStream();
                os.write(jsonString.getBytes());
                os.flush();
                os.close();
                System.out.println("CONNECTION CODE: " + conn.getResponseCode() + "FOR PUT CLIENT'S WORKOUT");
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
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    goNextPage();
                }
            });
        }

    }
}
