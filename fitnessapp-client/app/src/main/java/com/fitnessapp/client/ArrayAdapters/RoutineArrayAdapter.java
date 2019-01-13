package com.fitnessapp.client.ArrayAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fitnessapp.client.R;
import com.fitnessapp.client.Utils.Routine;

import java.util.ArrayList;

public class RoutineArrayAdapter extends ArrayAdapter<Routine> {

    public RoutineArrayAdapter(Context context, ArrayList<Routine> routines) {
        super(context, 0, routines);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Routine routine = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_routine_list, parent, false);
        }
        // Lookup view for data population
        TextView tvName = convertView.findViewById(R.id.routineName);
        TextView tvDuration = convertView.findViewById(R.id.routineDuration);
        // Populate the data into the template view using the data object
        tvDuration.setText(String.valueOf(routine.getDuration()));
        tvName.setText(routine.getName());
        // Return the completed view to render on screen
        return convertView;
    }

}
