package com.fitnessapp.client.Fragments;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class ProgressTrackerFragment extends Fragment {

    private LineChart mChart;
    private UrlConnectorGetProgress ucgp;
    private URL url;
    private HttpURLConnection conn;
    private BaseDrawerActivity activity;
    public ProgressTrackerFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Progress Tracker");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_progress_tracker, container, false);
        activity = (BaseDrawerActivity)getActivity();
        loadData(RootView);
        return RootView;
    }


    private void loadData(View rootView) {
        mChart = rootView.findViewById(R.id.linechart);
        // add data
        setData();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        mChart.setDescription("");
        Spinner spinner = rootView.findViewById(R.id.spinnerRoutines);
        String[] letra = {"Routine1","Routine2","Routine3","Routine4","Routine5"};
        spinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, letra));
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
    private class UrlConnectorGetProgress extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... params) {
            try {

                URL url = new URL(StaticStrings.ipserver  + "/client/find/" + activity.userId);
                System.out.println(url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                int userID = -1;

                if(conn.getResponseCode() == 200){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);
                    JSONObject user = arr.getJSONObject(0);
                    userID = user.getInt("id");
                    System.out.println("USER: " + user);
                    br.close();
                }else{
                    System.out.println("COULD NOT FIND USER");
                    return null;
                }

                conn.disconnect();
                if(userID == -1) {
                    System.out.println("USER NOT EXISTS");
                    return null;
                }
                conn.disconnect();

                ArrayList<Integer> ingredientIds = new ArrayList<>();
                url = new URL(StaticStrings.ipserver  + "/client/find/" + activity.userId);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if(conn.getResponseCode() == 200){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);
                    for(int i = 0; i<arr.length(); i++){
                        String ingredientName = arr.getJSONObject(i).getString("name");
                    }
                    br.close();
                    System.out.println("INGREDIENTS: " + arr);
                }else{
                    System.out.println("COULD NOT FIND INGREDIENTS");
                    return null;
                }
                conn.disconnect();

            } catch (Exception e) {
                System.out.println("User could not be created: ");
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
