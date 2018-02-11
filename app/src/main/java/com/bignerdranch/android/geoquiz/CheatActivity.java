/*
 * Copyright (c) 2018. Krzysztof Nowak "knowaknet"
 */

package com.bignerdranch.android.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";

    private boolean mAnswerIsTrue;
    private Button mShowAnswer;
    private TextView mAnswer;

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

        setupWidgets();
        setupWidgetEvents();
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
            }
        });
    }
}
