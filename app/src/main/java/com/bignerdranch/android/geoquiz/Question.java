/*
 * Copyright (c) 2017. Krzysztof Nowak "knowaknet"
 */

package com.bignerdranch.android.geoquiz;

/**
 * Created by knowaknet on 30.12.2017.
 */

public class Question {
    private int mTextResId;
    private boolean mIsAnswerTrue;
    private Boolean mNoticedValidAnswer;

    public Question(int textResId, boolean isAnswerTrue) {
        this.mTextResId = textResId;
        this.mIsAnswerTrue = isAnswerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mIsAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mIsAnswerTrue = answerTrue;
    }

    public Boolean getNoticedValidAnswer() {
        return mNoticedValidAnswer;
    }

    public void setNoticedValidAnswer(Boolean noticedValidAnswer) {
        mNoticedValidAnswer = noticedValidAnswer;
    }
}
