package com.fitnessapp.client.ArrayAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fitnessapp.client.R;
import com.fitnessapp.client.Utils.User;

import java.util.ArrayList;

public class UserArrayAdapter extends ArrayAdapter<User> {

    public UserArrayAdapter(Context context, ArrayList<User> routines) {
        super(context, 0, routines);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_user_list, parent, false);
        }
        // Lookup view for data population
        TextView tvName = convertView.findViewById(R.id.clientName);
        TextView tvMail = convertView.findViewById(R.id.clientMail);
        TextView tvPhone = convertView.findViewById(R.id.clientPhone);
        // Populate the data into the template view using the data object
        tvName.setText(user.getName());
        tvMail.setText(user.getEmail());
        tvPhone.setText(user.getTelNum());
        // Return the completed view to render on screen
        return convertView;
    }

}
