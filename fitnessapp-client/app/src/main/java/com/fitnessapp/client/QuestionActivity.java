package com.fitnessapp.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QuestionActivity extends AppCompatActivity {

    private QuestionLibrary mQuestionLibrary = new QuestionLibrary();

    private TextView mQuestionView;
    private Button mButtonChoice1;
    private Button mButtonChoice2;
    private Button mButtonChoice3;

    private String mAnswer1;
    private String mAnswer2;
    private String mAnswer3;
    private int mQuestionNumber = 0;
    private int endoScore = 0;
    private int mesoScore = 0;
    private int ectoScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_questionnaire);

        mQuestionView = (TextView)findViewById(R.id.question);
        mButtonChoice1 = (Button)findViewById(R.id.choice1);
        mButtonChoice2 = (Button)findViewById(R.id.choice2);
        mButtonChoice3 = (Button)findViewById(R.id.choice3);

        updateQuestion();

        //Start of Button Listener for Button1
        mButtonChoice1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if (mButtonChoice1.getText() == mAnswer1){
                    ectoScore = ectoScore + 1;
                    updateQuestion();
                }else if(mButtonChoice1.getText() == mAnswer2){
                    mesoScore = mesoScore + 1;
                    updateQuestion();
                }else if(mButtonChoice1.getText() == mAnswer3){
                    endoScore = endoScore + 1;
                    updateQuestion();
                }
            }
        });

        //End of Button Listener for Button1

        //Start of Button Listener for Button2
        mButtonChoice2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if (mButtonChoice2.getText() == mAnswer1){
                    ectoScore = ectoScore + 1;
                    updateQuestion();
                }else if(mButtonChoice2.getText() == mAnswer2){
                    mesoScore = mesoScore + 1;
                    updateQuestion();
                }else if(mButtonChoice2.getText() == mAnswer3){
                    endoScore = endoScore + 1;
                    updateQuestion();
                }
            }
        });

        //End of Button Listener for Button2


        //Start of Button Listener for Button3
        mButtonChoice3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //My logic for Button goes in here

                if (mButtonChoice3.getText() == mAnswer1){
                    ectoScore = ectoScore + 1;
                    updateQuestion();
                }else if(mButtonChoice3.getText() == mAnswer2){
                    mesoScore = mesoScore + 1;
                    updateQuestion();
                }else if(mButtonChoice3.getText() == mAnswer3){
                    endoScore = endoScore + 1;
                    updateQuestion();
                }
            }
        });

        //End of Button Listener for Button3


        //if(mesoScore + endoScore + ectoScore == 7){
            //Transicionar a la layout de resultat (login_questionnaire_reuslt)
            // a on posi quin tipus de cos té més punts
        //}
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
}