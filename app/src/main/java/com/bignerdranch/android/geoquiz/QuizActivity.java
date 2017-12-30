package com.bignerdranch.android.geoquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private Button btnTrue;
    private Button btnFalse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        setupWidgets();
        setupWidgetsEvents();
    }

    private void setupWidgets(){
        btnTrue = findViewById(R.id.btn_true);
        btnFalse = findViewById(R.id.btn_false);
    }

    private void setupWidgetsEvents(){
        btnTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswerResult(R.string.msg_correct_answer);
            }
        });

        btnFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswerResult(R.string.msg_incorrect_answer);
            }
        });
    }

    private void showAnswerResult(int resId){
        Toast msg = Toast.makeText(QuizActivity.this, resId, Toast.LENGTH_SHORT);
        msg.setGravity(Gravity.TOP, 0, 100);
        msg.show();
    }
}
