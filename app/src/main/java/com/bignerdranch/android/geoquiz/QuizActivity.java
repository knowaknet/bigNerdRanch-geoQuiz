/*
 * Copyright (c) 2017. Krzysztof Nowak "knowaknet"
 */

package com.bignerdranch.android.geoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.geoquiz.dto.Question;

public class QuizActivity extends AppCompatActivity {
    private static final String C = QuizActivity.class.getName();
    private static final String KEY_CURRENT_INDEX = "index";
    private static final String KEY_CURRENT_USER_CHEATING_STATE = "cheater";
    private static final int CHEAT_ACTIVITY_REQUEST_CODE = 0;

    private Button mBtnTrue;
    private Button mBtnFalse;
    private Button mBtnGoCheat;
    private ImageButton mBtnNext;
    private ImageButton mBtnPrev;
    private TextView mQuestionTextView;
    private Question[] mQuestions;
    private Boolean[] mCheatingDiary;
    private int mCurrentIndexQuestion = 0;
    private boolean mUserIsCheater = false;

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
        outState.putBoolean(KEY_CURRENT_USER_CHEATING_STATE, mUserIsCheater);
    }

    private void loadState(Bundle savedInstanceState){
        if(savedInstanceState == null)
            return;

        mCurrentIndexQuestion = savedInstanceState.getInt(KEY_CURRENT_INDEX, 0);
        mUserIsCheater = savedInstanceState.getBoolean(KEY_CURRENT_USER_CHEATING_STATE, false);
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
        mBtnGoCheat = findViewById(R.id.btn_go_cheat);
        mBtnNext = findViewById(R.id.btn_next);
        mBtnPrev = findViewById(R.id.btn_previous);
        mQuestionTextView = findViewById(R.id.question_text_view);
    }

    private void setupWidgetsEvents(){
        mBtnTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                updateLockAnswerState();
            }
        });

        mBtnFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                updateLockAnswerState();
            }
        });

        View.OnClickListener nextQuestionListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mCurrentIndexQuestion = (mCurrentIndexQuestion + 1) %  mQuestions.length;
                reloadActiveQuestion();
                updateLockAnswerState();
                mUserIsCheater = false;
            }
        };

        mBtnNext.setOnClickListener(nextQuestionListener);
        mQuestionTextView.setOnClickListener(nextQuestionListener);

        mBtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndexQuestion =  (mCurrentIndexQuestion - 1) % mQuestions.length;
                if(mCurrentIndexQuestion < 0)
                    mCurrentIndexQuestion = mQuestions.length -1;
                reloadActiveQuestion();
                updateLockAnswerState();
                mUserIsCheater = false;
            }
        });

        mBtnGoCheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cheatIntent = CheatActivity.newIntent(
                        QuizActivity.this,
                        mQuestions[mCurrentIndexQuestion].isAnswerTrue());
                startActivityForResult(cheatIntent, CHEAT_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    private void checkAnswer(boolean userPressedTrue){
        Question question = mQuestions[mCurrentIndexQuestion];
        boolean validAnswer = question.isAnswerTrue();
        if(mUserIsCheater || isActiveQuestionHasBeenCheated(mCurrentIndexQuestion)){
            showAnswerResult(R.string.judgment_toast);
        } else if(userPressedTrue == validAnswer){
            showAnswerResult(R.string.msg_correct_answer);
            question.setNoticedValidAnswer(true);
        } else {
            showAnswerResult(R.string.msg_incorrect_answer);
            question.setNoticedValidAnswer(false);
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
        mCheatingDiary = new Boolean[mQuestions.length];
        reloadActiveQuestion();
    }

    private void reloadActiveQuestion(){
        int activeQuestionRId = mQuestions[mCurrentIndexQuestion].getTextResId();
        mQuestionTextView.setText(activeQuestionRId);
    }

    private void updateLockAnswerState(){
        if(mQuestions[mCurrentIndexQuestion].getNoticedValidAnswer() != null) {
            mBtnTrue.setEnabled(false);
            mBtnFalse.setEnabled(false);
        } else {
            mBtnTrue.setEnabled(true);
            mBtnFalse.setEnabled(true);
        }

        summarizeQuiz();
    }

    private void summarizeQuiz(){
        int correctAnswers = 0;
        for (Question q : mQuestions) {
            if (q.getNoticedValidAnswer() == null)
                return; // break simmarize, quiz is not completed

            correctAnswers += q.getNoticedValidAnswer() ? 1 : 0;
        }

        int percentageResult = correctAnswers * 100 / mQuestions.length;
        String messageFormat = this.getString(R.string.msg_quiz_result);
        String quizResultMsg = String.format(messageFormat, percentageResult);
        Toast msg = Toast.makeText(QuizActivity.this, quizResultMsg, Toast.LENGTH_LONG);
        msg.setGravity(Gravity.BOTTOM, 0, 100);
        msg.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK)
            return;

        if(requestCode == CHEAT_ACTIVITY_REQUEST_CODE){
            if(data != null) {
                mUserIsCheater = CheatActivity.wasAnswerShown(data);
                mCheatingDiary[mCurrentIndexQuestion] = mUserIsCheater;
            }
        }
    }

    private boolean isActiveQuestionHasBeenCheated(int index){
        Boolean hasBeenCheated = mCheatingDiary[index];
        return hasBeenCheated != null ? hasBeenCheated : false;
    }
}
