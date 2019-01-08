package com.fitnessapp.client;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fitnessapp.client.Utils.QuestionLibrary;
import com.fitnessapp.client.Utils.StaticStrings;
import com.fitnessapp.client.Utils.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    private FirebaseAuth mAuth;
    private TextView mQuestionView;
    private Button mButtonChoice1;
    private Button mButtonChoice2;
    private Button mButtonChoice3;
    private Button mButtonTransient;
    private UrlConnectorCreateClient uccc;
    private HttpURLConnection conn;
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
    private String bodyTypeValue="";
    private int questionsAnswered = 0;
    private String userEmail="";
    private Boolean isPremium=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_questionnaire);
        mAuth = FirebaseAuth.getInstance();
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
            //bodyTypeId = bdyTypes.indexOf(maxVal);
            if(maxVal==mesoScore){
                bodyTypeId=1;
                bodyTypeValue = "Mesomorph";
            }else if(maxVal==endoScore){
                bodyTypeId=2;
                bodyTypeValue = "Endomorph";
            }else if(maxVal==ectoScore){
                bodyTypeId=3;
                bodyTypeValue = "Ectomorph";
            }
            mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("REGISTER: ", "createUserWithEmail:success");
                                FirebaseUser fbUser = mAuth.getCurrentUser();
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("Users").child(fbUser.getUid()).setValue(user);
                                uccc = new UrlConnectorCreateClient();
                                uccc.execute();
                                toBaseDrawerActivity();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("REGISTER: ", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(QuestionActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void toBaseDrawerActivity() {
        Intent i = new Intent(this, BaseDrawerActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("userType", user.getRole());
        b.putString("userEmail",user.getEmail());
        b.putBoolean("isPremium",isPremium);
        i.putExtra("bundle", b);
        startActivity(i);
        finish();
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
    @Override
    public void onDestroy(){
        if(!uccc.isCancelled()){
            uccc.cancel(true);
        }
        super.onDestroy();
    }

    private class UrlConnectorCreateClient extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... params) {
            try {
                //CREATE CLIENT IN DB
                URL url = new URL(StaticStrings.ipserver + "/client/");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);


                JSONObject subjson = new JSONObject()
                        .put("id", bodyTypeId)
                        .put("body_type_value", bodyTypeValue);
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
                        .put("is_Premium", isPremium)
                        .toString();

                System.out.println(jsonString);
                OutputStream os = conn.getOutputStream();
                os.write(jsonString.getBytes());
                os.flush();
                os.close();
                System.out.println("CONNECTION CODE: " + conn.getResponseCode());
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