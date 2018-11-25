package com.fitnessapp.client.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.fitnessapp.client.R;

public class ProfileFragment extends Fragment {

    private Button confChangesbutton;
    private EditText nameET, emailET, passwordET, addressET, telNumET, heightET, weightET;

    public ProfileFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Profile");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_profile, container, false);
        getEditTexts(RootView);
        return RootView;
    }

    private void getEditTexts(View rootView) {
        nameET = rootView.findViewById(R.id.profile_name_et);
        emailET = rootView.findViewById(R.id.profile_email_et);
        passwordET = rootView.findViewById(R.id.profile_password_et);
        addressET = rootView.findViewById(R.id.profile_address_et);
        telNumET = rootView.findViewById(R.id.profile_telNum_et);
        heightET = rootView.findViewById(R.id.profile_height_et);
        weightET = rootView.findViewById(R.id.profile_weight_et);
        confChangesbutton = rootView.findViewById(R.id.confirmChangesbutton);
    }
}
