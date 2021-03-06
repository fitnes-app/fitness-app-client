package com.fitnessapp.client.Fragments;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fitnessapp.client.R;

import java.util.Calendar;

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private TextView mTv;
    private Button mBtn;
    private Calendar c;
    private DatePickerDialog dpd;
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;
    public SettingsFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Settings");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_settings, container, false);
        load(RootView);
        sp = PreferenceManager.getDefaultSharedPreferences(RootView.getContext());
        spEditor = sp.edit();
        return RootView;
    }

    private void load(View rootView){
        mTv = rootView.findViewById(R.id.textView4);
        mBtn = rootView.findViewById(R.id.buttonPick);

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        mTv.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
                    }
                }, day, month, year);
                dpd.show();
            }
        });

        Spinner spinner = rootView.findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        spEditor.putString("LANGUAJE", text);
        //String loadedString = prefs.getString("LANGUAJE", null);
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
