/*
 * Copyright (c) 2018. Krzysztof Nowak "knowaknet"
 */

package com.bignerdranch.android.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_HAS_BEEN_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";
    private static final String STATE_KEY_ANSWER_HAS_BEEN_SHOWN = "shown";

    private boolean mAnswerIsTrue;
    private Button mShowAnswer;
    private TextView mAnswer;

    private boolean mAnswerHasBeenShown = false;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Intent cheatIntent = new Intent(packageContext, CheatActivity.class);
        cheatIntent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return cheatIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        loadState(savedInstanceState);
        setupWidgets();
        setupWidgetEvents();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(STATE_KEY_ANSWER_HAS_BEEN_SHOWN, mAnswerHasBeenShown);
    }

    private void loadState(Bundle savedInstanceState) {
        if(savedInstanceState == null)
            return;

        mAnswerHasBeenShown = savedInstanceState.getBoolean(STATE_KEY_ANSWER_HAS_BEEN_SHOWN, false);
        setAnswerShownResult(mAnswerHasBeenShown);
    }

    private void setupWidgets() {
        mShowAnswer = findViewById(R.id.btn_show_answer);
        mAnswer = findViewById(R.id.answer_view_text);
    }

    private void setupWidgetEvents(){
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnswer.setText( mAnswerIsTrue ? R.string.btn_label_true : R.string.btn_label_false );
                setAnswerShownResult(true);

                // animation test; require API 21
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswer.getWidth() / 2;
                    int cy = mShowAnswer.getHeight() / 2;
                    float radius = mShowAnswer.getWidth();

                    Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswer, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswer.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    mShowAnswer.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void setAnswerShownResult(boolean whetherUserShownAnswer) {
        if(whetherUserShownAnswer) {
            mAnswerHasBeenShown = true;
            Intent intent = new Intent();
            //noinspection ConstantConditions
            intent.putExtra(EXTRA_ANSWER_HAS_BEEN_SHOWN, whetherUserShownAnswer);
            setResult(RESULT_OK, intent);
        }
    }

    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_HAS_BEEN_SHOWN, false);
    }
}
