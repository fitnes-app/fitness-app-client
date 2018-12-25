package com.fitnessapp.client.Fragments;

import android.content.Intent;
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
import com.fitnessapp.client.Utils.Table;
import com.fitnessapp.client.Utils.User;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainPageFragment extends Fragment {

    private ArrayList<String> exercicesTmp = new ArrayList<>();
    private Table tabla;
    private TextView welcome;
    private UrlConnectorGetUserData userData;
    private HttpURLConnection conn;
    private URL url;
    private BaseDrawerActivity activity;

    private User user;

    private String username;

    public MainPageFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.titleActivityMainPage);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_main_page, container, false);
  /*      Intent i = getIntent();
        if(i!=null){
            Bundle b = i.getBundleExtra("b");
            user = (User) b.getSerializable("user");
        }*/
        activity = (BaseDrawerActivity) getActivity();
        welcome = RootView.findViewById(R.id.welcomeuser);
        username="";
        userData = new UrlConnectorGetUserData();
        userData.execute();
        initExercices();
        setTable(RootView);
        return RootView;
    }

    public void initExercices(){
        exercicesTmp.add("Push ups");
        exercicesTmp.add("Medicine ball");
        exercicesTmp.add("Bulgarian Training Bag");
    }

    public void setTable(View rootView){
        tabla = new Table(getActivity(), (TableLayout) rootView.findViewById(R.id.tabla));
        tabla.agregarCabecera(R.array.mainPageTable_headers);
        for(int i = 0; i < exercicesTmp.size() ; i++)
        {
            ArrayList<String> elementos = new ArrayList<>();
            elementos.add(exercicesTmp.get(i));
            elementos.add("Casilla [" + i + ", 1]");
            elementos.add("Casilla [" + i + ", 2]");
            tabla.agregarFilaTabla(elementos);
        }
    }

     private class UrlConnectorGetUserData extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... params) {
            try {
                String userEmail = getActivity().getIntent().getExtras().getString("userEmail");
                url = new URL("http://localhost:8080/fitness-app-api-web/api"  + "/client/findByEmail/" + userEmail);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                int clientID = -1;
                String clientUsername = "";
                if(conn.getResponseCode() == 200){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);
                    JSONObject client = arr.getJSONObject(0);
                    clientID = client.getInt("clientID");
                    clientUsername = client.getString("userName");
                    System.out.println("CLIENT ID: " + clientID);
                    System.out.println("CLIENT USERNAME: " + clientUsername);
                    br.close();
                }else{
                    System.out.println("COULD NOT FIND THE ASIGNATION");
                    return null;
                }
                conn.disconnect();
                if(clientID == -1) {
                    System.out.println("ASIGNATION NOT EXISTS");
                    return null;
                }
/*                url = new URL("http://localhost:8080/fitness-app-api-web/api"  + "/client/" + clientID);
                conn = (HttpURLConnection) url.openConnection();

                if(conn.getResponseCode() == 200){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);
                    JSONObject client = arr.getJSONObject(0);
                    username=client.getString("username");
                    welcome.setText("Welcome, " + username);

                    System.out.println("CLIENT USERNAME: " + username);
                    br.close();
                }else{
                    System.out.println("COULD NOT FIND THE TRAINER");
                    return null;
                }
                conn.disconnect();
                if(clientID == -1 || username=="") {
                    System.out.println("CLIENT NOT EXISTS");
                    return null;
                }*/
            } catch (Exception e) {
                System.out.println("User could not be retrieved:" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
