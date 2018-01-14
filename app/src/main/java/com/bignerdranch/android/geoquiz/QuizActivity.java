/*
 * Copyright (c) 2017. Krzysztof Nowak "knowaknet"
 */

package com.bignerdranch.android.geoquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    private static final String C = QuizActivity.class.getName();
    private static final String KEY_CURRENT_INDEX = "index";

    private Button mBtnTrue;
    private Button mBtnFalse;
    private ImageButton mBtnNext;
    private ImageButton mBtnPrev;
    private TextView mQuestionTextView;
    private Question[] mQuestions;
    private int mCurrentIndexQuestion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(C, ">> ON CREATE");

        setContentView(R.layout.activity_quiz);

        setupWidgets();
        setupWidgetsEvents();
        loadState(savedInstanceState);
        loadQuestions();
    }

    /* I don't need it, just leave it here as it is in book; I'm not sure whether I will need it in app/next chapters in book.
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(C, ">> ON START");
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Log.d(C, ">> ON RESUME FRAGMENTS");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(C, ">> ON POST RESUME");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(C, ">> ON PAUSE");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(C, ">> ON STOP");
    }
    */

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(C, ">> ON SAVE INSTANCE");

        outState.putInt(KEY_CURRENT_INDEX, mCurrentIndexQuestion);
    }

    private void loadState(Bundle savedInstanceState){
        if(savedInstanceState == null)
            return;

        mCurrentIndexQuestion = savedInstanceState.getInt(KEY_CURRENT_INDEX, 0);
    }

    /*
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(C, ">> ON DESTROY");
    }

    */

    private void setupWidgets(){
        mBtnTrue = findViewById(R.id.btn_true);
        mBtnFalse = findViewById(R.id.btn_false);
        mBtnNext = findViewById(R.id.btn_next);
        mBtnPrev = findViewById(R.id.btn_previous);
        mQuestionTextView = findViewById(R.id.question_text_view);
    }

    private void setupWidgetsEvents(){
        mBtnTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mBtnFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        View.OnClickListener nextQuestionListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mCurrentIndexQuestion = (mCurrentIndexQuestion + 1) %  mQuestions.length;
                reloadActiveQuestion();
            }
        };

        mBtnNext.setOnClickListener(nextQuestionListener);
        mQuestionTextView.setOnClickListener(nextQuestionListener);

        mBtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndexQuestion = (mCurrentIndexQuestion - 1) % mQuestions.length;
                reloadActiveQuestion();
            }
        });
    }

    private void checkAnswer(boolean userPressedTrue){
        boolean validAnswer = mQuestions[mCurrentIndexQuestion].isAnswerTrue();
        if(userPressedTrue == validAnswer){
            showAnswerResult(R.string.msg_correct_answer);
        } else {
            showAnswerResult(R.string.msg_incorrect_answer);
        }
    }

    private void showAnswerResult(int resId){
        Toast msg = Toast.makeText(QuizActivity.this, resId, Toast.LENGTH_SHORT);
        msg.setGravity(Gravity.TOP, 0, 100);
        msg.show();
    }

    private void loadQuestions(){
        mQuestions = new Question[]{
                new Question(R.string.australia, true),
                new Question(R.string.oceans, true),
                new Question(R.string.mideast, false),
                new Question(R.string.africa, false),
                new Question(R.string.americas, true),
                new Question(R.string.asia, true)
        };
        reloadActiveQuestion();
    }

    private void reloadActiveQuestion(){
        int activeQuestionRId = mQuestions[mCurrentIndexQuestion].getTextResId();
        mQuestionTextView.setText(activeQuestionRId);
    }
}
