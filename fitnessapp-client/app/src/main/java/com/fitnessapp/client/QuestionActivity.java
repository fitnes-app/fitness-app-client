package com.fitnessapp.client;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fitnessapp.client.Utils.QuestionLibrary;
import com.fitnessapp.client.Utils.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private QuestionLibrary mQuestionLibrary = new QuestionLibrary();

    private TextView mQuestionView;
    private Button mButtonChoice1;
    private Button mButtonChoice2;
    private Button mButtonChoice3;
    private Button mButtonTransient;
    private String ipserver = "http://localhost:8080/fitness-app-api-web/api";
    private User user;
    private LinearLayout questionary, lastData;
    private EditText weightET, heightET;
    private String mAnswer1;
    private String mAnswer2;
    private String mAnswer3;
    private int mQuestionNumber = 0;
    private int endoScore = 0;
    private int mesoScore = 0;
    private int ectoScore = 0;
    private int bodyTypeId = 0;
    private int questionsAnswered = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_questionnaire);
        Intent i = getIntent();
        if(i!=null){
            Bundle b = i.getBundleExtra("b");
            user = (User) b.getSerializable("user");
        }
        questionary = findViewById(R.id.questionaryLayout);
        lastData = findViewById(R.id.inputData);
        mQuestionView = findViewById(R.id.question);
        mButtonChoice1 = findViewById(R.id.choice1);
        mButtonChoice2 = findViewById(R.id.choice2);
        mButtonChoice3 = findViewById(R.id.choice3);
        mButtonTransient = findViewById(R.id.buttonToQuestionary);
        weightET = findViewById(R.id.inputWeight);
        heightET = findViewById(R.id.inputHeigh);
        updateQuestion();
        mButtonChoice1.setOnClickListener(this);
        mButtonChoice2.setOnClickListener(this);
        mButtonChoice3.setOnClickListener(this);
        mButtonTransient.setOnClickListener(this);
        questionary.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {}

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if ( questionsAnswered < 6) {
            if (id == mButtonChoice1.getId()) {
                updateScores(mButtonChoice1);
                questionsAnswered++;

            } else if (id == mButtonChoice2.getId()) {
                updateScores(mButtonChoice2);
                questionsAnswered++;

            } else if (id == mButtonChoice3.getId()) {
                updateScores(mButtonChoice3);
                questionsAnswered++;

            } else if (id == mButtonTransient.getId()) {
                String height = heightET.getText().toString();
                String weight = weightET.getText().toString();
                if (height != null && weight != null) {
                    lastData.setVisibility(View.GONE);
                    questionary.setVisibility(View.VISIBLE);
                    user.setWeigth(Float.parseFloat(weight));
                    user.setHeight(Float.parseFloat(height));
                }
            }
        }else{
            List<Integer> bdyTypes = new ArrayList<>();
            bdyTypes.add(mesoScore);
            bdyTypes.add(endoScore);
            bdyTypes.add(ectoScore);
            Integer maxVal = Collections.max(bdyTypes);
            bodyTypeId = bdyTypes.indexOf(maxVal);
            UrlConnectorCreateClient uccc = new UrlConnectorCreateClient();
            uccc.execute();
            Intent i = new Intent(this,BaseDrawerActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void updateScores(Button mButtonChoice) {
        if (mButtonChoice.getText().toString() == mAnswer1) {
            ectoScore = ectoScore + 1;
            updateQuestion();
        } else if (mButtonChoice.getText().toString() == mAnswer2) {
            mesoScore = mesoScore + 1;
            updateQuestion();
        } else if (mButtonChoice.getText().toString() == mAnswer3) {
            endoScore = endoScore + 1;
            updateQuestion();
        }
    }

    private void updateQuestion(){
        mQuestionView.setText(mQuestionLibrary.getQuestion(mQuestionNumber));
        mButtonChoice1.setText(mQuestionLibrary.getChoice1(mQuestionNumber));
        mButtonChoice2.setText(mQuestionLibrary.getChoice2(mQuestionNumber));
        mButtonChoice3.setText(mQuestionLibrary.getChoice3(mQuestionNumber));

        mAnswer1 = mQuestionLibrary.getAnswerEcto(mQuestionNumber);
        mAnswer2 = mQuestionLibrary.getAnswerMeso(mQuestionNumber);
        mAnswer3 = mQuestionLibrary.getAnswerEndo(mQuestionNumber);
        mQuestionNumber++;
    }


    private class UrlConnectorCreateClient extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... params) {
            try {
                //CREATE CLIENT IN DB
                URL url = new URL(ipserver  + "/client/");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);


                JSONObject subjson = new JSONObject()
                        .put("id", bodyTypeId)
                        .put("body_type_value", bodyTypeId);
/*{"userName":"asdf3", "userPassword":"asdf2", "mail":"asdf@asdf.com","weight":2,"height":2,"bodyTypeId":{"id":1,"body_type_value":1},"telephone":"asdf", "address":"asdfdsd"}*/
                String jsonString = new JSONObject()
                        .put("userName", user.getName())
                        .put("userPassword", user.getPassword())
                        .put("mail", user.getEmail())
                        .put("weight", user.getWeigth())
                        .put("height", user.getHeight())
                        .put("bodyTypeId", subjson)
                        .put("telephone", user.getTelNum())
                        .put("address", user.getAddress())
                        .toString();
                System.out.println(jsonString);
                OutputStream os = conn.getOutputStream();
                os.write(jsonString.getBytes());
                os.flush();
                os.close();
                System.out.println("CONNECTION CODE: " + conn.getResponseCode());
                conn.disconnect();
            } catch (Exception e) {
                System.out.println("User could not be created:" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}