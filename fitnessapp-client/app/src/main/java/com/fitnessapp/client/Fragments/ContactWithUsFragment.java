package com.fitnessapp.client.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.fitnessapp.client.R;

public class ContactWithUsFragment extends Fragment implements View.OnClickListener {

    private EditText subject, body;
    private Button sendEmail;
    public ContactWithUsFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.titleActivityContactWithUs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_contact_with_us, container, false);
        subject  = RootView.findViewById(R.id.emailet);
        body = RootView.findViewById(R.id.bodyet);
        sendEmail = RootView.findViewById(R.id.sendemailButton);
        sendEmail.setOnClickListener(this);
        return RootView;
    }

    @Override
    public void onClick(View view) {
        String subjectString = subject.getText().toString();
        String bodyString = body.getText().toString();
        if(!"".equals(subjectString) && !"".equals(bodyString)) {
            Intent mailIntent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.parse("mailto:?subject=" + subjectString + "&body=" + bodyString + "&to=" + "alcarras.jordi.godia@gmail.com");
            mailIntent.setData(data);
            startActivity(Intent.createChooser(mailIntent, "Send mail..."));
        }
    }
}
