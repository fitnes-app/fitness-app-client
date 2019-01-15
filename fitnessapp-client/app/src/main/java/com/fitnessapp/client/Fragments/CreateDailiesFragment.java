package com.fitnessapp.client.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fitnessapp.client.BaseDrawerActivity;
import com.fitnessapp.client.R;
import com.fitnessapp.client.Utils.StaticStrings;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateDailiesFragment extends Fragment {

    private View RootView;
    private Button Daily1But;
    private Button Daily2But;
    private Button Daily3But;
    private Button Daily4But;
    private Button Daily5But;
    private Button doneButtonLabel;


    private UrlConnectorGetDailiesInfo dailiesInfo;
    private HttpURLConnection conn;
    private URL url;
    private Bundle b = new Bundle();

    private int workoutId=0;
    private int workoutDuration=0;
    private int daily1Id=0;
    private int daily2Id=0;
    private int daily3Id=0;
    private int daily4Id=0;
    private int daily5Id=0;

    public CreateDailiesFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Define the new dailies");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RootView = inflater.inflate(R.layout.fragment_create_dailies, container, false);
        workoutId = getArguments().getInt("newWorkoutId");
        workoutDuration = getArguments().getInt("workoutDuration");
        Daily1But = RootView.findViewById(R.id.Daily1But);
        Daily2But = RootView.findViewById(R.id.Daily2But);
        Daily3But = RootView.findViewById(R.id.Daily3But);
        Daily4But = RootView.findViewById(R.id.Daily4But);
        Daily5But = RootView.findViewById(R.id.Daily5But);
        doneButtonLabel = RootView.findViewById(R.id.doneButtonLabel);

        if(workoutDuration == 3){
            Daily2But.setVisibility(View.GONE);
            Daily4But.setVisibility(View.GONE);
        }

        Daily1But.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.putInt("dailyId",daily1Id);
                goNextPage(b);
            }
        });

        Daily2But.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.putInt("dailyId",daily2Id);
                goNextPage(b);
            }
        });

        Daily3But.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.putInt("dailyId",daily3Id);
                goNextPage(b);
            }
        });

        Daily4But.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.putInt("dailyId",daily4Id);
                goNextPage(b);
            }
        });

        Daily5But.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.putInt("dailyId",daily5Id);
                goNextPage(b);
            }
        });

        doneButtonLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneGoBack();
            }
        });

        dailiesInfo = new UrlConnectorGetDailiesInfo();
        dailiesInfo.execute();
        return RootView;
    }
    public void goNextPage(Bundle b) {
        FragmentManager fragmentManager = getFragmentManager();
        PopupCreateDailiesFragment fragment =  PopupCreateDailiesFragment.newInstance();
        fragment.setArguments(b);
        fragment.show(fragmentManager,"dailies");

    }
    public void doneGoBack(){
        Fragment fragment = new MainPageTrainerFragment();
        BaseDrawerActivity bda = (BaseDrawerActivity)getActivity();
        bda.displaySelectedFragment(fragment);
    }
    private class UrlConnectorGetDailiesInfo  extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params){
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

                    for (int i = 0; i < arr.length(); i++) {
                        if(arr.getJSONObject(i).getInt("week_day") == 1){
                            daily1Id = arr.getJSONObject(i).getInt("id");
                        }else if(arr.getJSONObject(i).getInt("week_day") == 2){
                            daily2Id = arr.getJSONObject(i).getInt("id");
                        }else if(arr.getJSONObject(i).getInt("week_day") == 3){
                            daily3Id = arr.getJSONObject(i).getInt("id");
                        }else if(arr.getJSONObject(i).getInt("week_day") == 4){
                            daily4Id = arr.getJSONObject(i).getInt("id");
                        }else if(arr.getJSONObject(i).getInt("week_day") == 5){
                            daily5Id = arr.getJSONObject(i).getInt("id");
                        }
                    }
                    System.out.println("UrlConnectorGetDailiesInfo:ConectionCode -> " + conn.getResponseCode());
                    System.out.println("daily1Id-> " + daily1Id);
                    System.out.println("daily2Id-> " + daily2Id);
                    System.out.println("daily3Id-> " + daily3Id);
                    System.out.println("daily4Id-> " + daily4Id);
                    System.out.println("daily5Id-> " + daily5Id);
                    br.close();
                } else {
                    System.out.println("COULD NOT FIND THE Advanced_Workout");
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
