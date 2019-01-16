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

import com.fitnessapp.client.ArrayAdapters.UserArrayAdapter;
import com.fitnessapp.client.BaseDrawerActivity;
import com.fitnessapp.client.R;
import com.fitnessapp.client.Utils.StaticStrings;
import com.fitnessapp.client.Utils.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainPageTrainerFragment extends Fragment {

    private View RootView;
    private UrlConnectorGetTrainerEmail ucgte;
    private HttpURLConnection conn;
    private URL url;
    private HashMap<String, Integer> clientsIdLog = new HashMap<String, Integer>();
    ArrayList<String> clientNames;
    private ArrayList<User> usersAssigned;
    private String trainerMail;
    private int trainerID;
    private BaseDrawerActivity activity;
    ArrayList<String> assignedUsers = new ArrayList<String>();
    ArrayList<Integer> userIds = new ArrayList<Integer>();
    ArrayList<String> userMails = new ArrayList<String>();
    ArrayList<Integer> userWeights = new ArrayList<Integer>();
    ArrayList<Integer> userHeights = new ArrayList<Integer>();
    ArrayList<String> userTels = new ArrayList<String>();
    ArrayList<String> userAdds = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private ListView lw;

    public MainPageTrainerFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Trainer Main Page");
        usersAssigned = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RootView = inflater.inflate(R.layout.fragment_main_page_trainer, container, false);
        ucgte = new UrlConnectorGetTrainerEmail();
        activity = (BaseDrawerActivity) getActivity();
        activity.userMail = getActivity().getIntent().getExtras().getBundle("bundle").getString("userEmail");
        trainerMail = activity.userMail;
        ucgte.execute();

        return RootView;
    }
    private void loadClients(View rootView) {
        lw = rootView.findViewById(R.id.assignedUsersList);
        setClientNames();
        UserArrayAdapter aa = new UserArrayAdapter(getActivity(), usersAssigned);
        lw.setAdapter(aa);
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                openUserDetail(v, position);

            }
        });
    }
    public void setClientNames() {
        clientNames = new ArrayList<String>(clientsIdLog.keySet());
    }
    private void setNoResultsMessage(View rootView) {
        lw = rootView.findViewById(R.id.assignedUsersList);
        clientNames = new ArrayList<String>();
        clientNames.add(getActivity().getResources().getString(R.string.noUsersAssignedMessage));
        ArrayAdapter<String> aa = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, clientNames);
        lw.setAdapter(aa);
    }
    public void openUserDetail(View view, int position) {
        Fragment fragment = new UserDetailsFragment();
        ArrayList<String> keys = new ArrayList<String>(clientsIdLog.keySet());
        String neededKey = keys.get(position);
        int selectedUserId = clientsIdLog.get(neededKey);
        Bundle b = new Bundle();
        b.putInt("selectedUserId",selectedUserId);
        fragment.setArguments(b);
        BaseDrawerActivity bda = (BaseDrawerActivity) getActivity();
        bda.displaySelectedFragment(fragment);

    }
    private class UrlConnectorGetTrainerEmail extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                url = new URL(StaticStrings.ipserver + "/trainer/findByEmail/" + trainerMail);
                System.out.println(url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);
                    if (arr.length() == 0) {
                        System.out.println("The trainer isn't in the DB");
                        return null;
                    }
                    JSONObject tra = arr.getJSONObject(0);

                    trainerID = tra.getInt("id");
                    activity.userId = trainerID;
                    System.out.println(trainerID);
                    br.close();
                }
            }catch(Exception e) {
                System.out.println("Something went wrong");
                e.printStackTrace();
            }
            conn.disconnect();
            try{
                url = new URL(StaticStrings.ipserver + "/assigned/findByTrainerId/" + trainerID);

                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String output = br.readLine();
                    JSONArray arr = new JSONArray(output);
                    if(arr.length()>0) {
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject client = arr.getJSONObject(i).getJSONObject("clientId");
                            String clientName = client.getString("userName");
                            String clientMail = client.getString("mail");
                            String clientPhone = client.getString("telephone");
                            int clientId = client.getInt("id");
                            clientsIdLog.put(clientName, clientId);
                            usersAssigned.add(new User(clientName,clientMail,clientPhone));
                        }
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    loadClients(RootView);

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
                    System.out.println("ERROR GETTING Users");
                    System.out.println("ERROR CODE -> " + conn.getResponseCode());
            }

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
    }
}
