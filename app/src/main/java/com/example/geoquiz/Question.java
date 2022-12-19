package com.example.geoquiz;

public class Question {

    /* mTextResourceId variable will hold the resource ID of a string resource for the question.*/
    private int mTextResourceId;
    private boolean mAnswer;

    public Question(int textResID, boolean answer)
    {
        mTextResourceId = textResID;
        mAnswer = answer;
    }
    //setters
    public void setTextResourceId(int textResourceId) {
        mTextResourceId = textResourceId;
    }
    public void setAnswerTrue(boolean answer) {
        mAnswer = answer;
    }


    //getters
    public int getTextResourceId() {
        return mTextResourceId;
    }

    public boolean getAnswer() {
        return mAnswer;
    }

}
