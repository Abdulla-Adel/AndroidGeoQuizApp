package com.example.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_ANSWERED = "answered";
    private static final String KEY_COLOR = "color";
    private static final String KEY_SCORE = "Score";
    private static final String KEY_VISIBLE = "visible";


    //add two member variables (to access the button widgets and store their references)
    private Button mTrueButton;
    private ImageButton mNextButton;
    private Button mFalseButton;
    private Button mSubmitButton;
    private boolean isVisible;
    private TextView mQuestionTextView;
    private ArrayList <Integer> mAnsweredQuestions= new ArrayList<Integer>();
    private char [] mButtonBackgroundColor = new char[6];


   //declare and instantiate the array of question objects
    private Question[] mQuestionBank = new Question[]

           //create & initialize actual question objects using constructor
           {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
           };

     private int mCurrentIndex = 0;
     private int mNumberOfCorrectAnswers = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate(Bundle) called");
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null)
        {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
            mAnsweredQuestions = savedInstanceState.getIntegerArrayList(KEY_ANSWERED);
            mButtonBackgroundColor = savedInstanceState.getCharArray(KEY_COLOR);
            mNumberOfCorrectAnswers = savedInstanceState.getInt(KEY_SCORE);
            isVisible = savedInstanceState.getBoolean(KEY_VISIBLE);
        }

        //get a reference for the TextView and set its text to the question at the current index
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);


        //Getting references to widgets
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener()
            {
                 @Override
                 public void onClick(View view)
                {
                    mTrueButton.setEnabled(false);
                    mFalseButton.setEnabled(false);
                    checkAnswer(true);
                    mAnsweredQuestions.add(mCurrentIndex);
                    updateButtonColor(true);
                }
            }
        );
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        mTrueButton.setEnabled(false);
                        mFalseButton.setEnabled(false);
                        checkAnswer(false);
                        mAnsweredQuestions.add(mCurrentIndex);
                        updateButtonColor(false);
                    }
                }
        );
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update the text view question
                mCurrentIndex = (mCurrentIndex+1) % mQuestionBank.length;
                UpdateQuestion();
            }
        });

        mSubmitButton = (Button) findViewById(R.id.submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        UpdateQuestion();
    }
    private void UpdateQuestion ()
    {

        if (mCurrentIndex == mQuestionBank.length-1)
        {
         mNextButton.setVisibility(View.INVISIBLE);
        }

        int question = mQuestionBank [mCurrentIndex].getTextResourceId();
        mQuestionTextView.setText(question);
        if (mAnsweredQuestions.contains(mCurrentIndex))
        {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);

            char bgColor = mButtonBackgroundColor[mCurrentIndex];
            if (bgColor == 'R' && mQuestionBank[mCurrentIndex].getAnswer()== true)
            {
                mFalseButton.setBackgroundColor(getResources().getColor(R.color.red));

            }
            else if (bgColor == 'R' && mQuestionBank[mCurrentIndex].getAnswer()== false)
            {
                mTrueButton.setBackgroundColor(getResources().getColor(R.color.red));
            }
            else if (bgColor == 'G' && mQuestionBank[mCurrentIndex].getAnswer()== true)
            {
                mTrueButton.setBackgroundColor(getResources().getColor(R.color.green));
            }
            else if (bgColor == 'G' && mQuestionBank[mCurrentIndex].getAnswer()== false)
            {
                mFalseButton.setBackgroundColor(getResources().getColor(R.color.green));
            }

            if (mCurrentIndex == mQuestionBank.length-1 && isVisible)
            {
                    mSubmitButton.setVisibility(View.VISIBLE);
            }

        }
        else
        {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);

            mTrueButton.setBackgroundColor(getResources().getColor(R.color.purple_500));
            mFalseButton.setBackgroundColor(getResources().getColor(R.color.purple_500));
        }


    }

    private void checkAnswer (Boolean UserPressed)
    {
        if (UserPressed == mQuestionBank[mCurrentIndex].getAnswer())
        {
            mNumberOfCorrectAnswers += 1;
            Toast.makeText(MainActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(MainActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
        }
        if (mCurrentIndex == mQuestionBank.length - 1)
        {
            mNextButton.setVisibility(View.INVISIBLE);
            showToast();

            //display the submit button
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    mSubmitButton.setVisibility(View.VISIBLE);
                }
            }, 5000);

            isVisible = true;
        }
    }
    private void showToast()
    {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,(ViewGroup) findViewById(R.id.toast_root));

        TextView toastText = layout.findViewById(R.id.toast_text);
        double percentageScore = ((double)mNumberOfCorrectAnswers)/mQuestionBank.length;
        percentageScore = percentageScore*100;
        String display = "your percentage Score is " + String.format("%.2f",percentageScore) + " %";
        toastText.setText(display);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity((Gravity.TOP|Gravity.CENTER_HORIZONTAL),0, 300);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
    private void updateButtonColor(Boolean UserPressed)
    {
        if (UserPressed == mQuestionBank[mCurrentIndex].getAnswer() )
        {
             if (UserPressed== true) {
                 mTrueButton.setBackgroundColor(getResources().getColor(R.color.green));
                 mFalseButton.setBackgroundColor(getResources().getColor(R.color.light));
                 mButtonBackgroundColor[mCurrentIndex] = 'G';
             }
             else
             {
                 mFalseButton.setBackgroundColor(getResources().getColor(R.color.green));
                 mTrueButton.setBackgroundColor(getResources().getColor(R.color.light));
                 mButtonBackgroundColor[mCurrentIndex] = 'G';
             }
        }
        else
            {
                if (UserPressed== true) {
                    mTrueButton.setBackgroundColor(getResources().getColor(R.color.red));
                    mFalseButton.setBackgroundColor(getResources().getColor(R.color.light));
                    mButtonBackgroundColor[mCurrentIndex] = 'R';
                }
                else
                {
                    mFalseButton.setBackgroundColor(getResources().getColor(R.color.red));
                    mTrueButton.setBackgroundColor(getResources().getColor(R.color.light));
                    mButtonBackgroundColor[mCurrentIndex] = 'R';
                }
            }
    }


    @Override
    public void onStart()
    {
        super.onStart();
        Log.d(TAG,"onStart() called");
    }
    @Override
    public void onResume()
    {
        super.onResume();
        Log.d(TAG,"onResume() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG,"onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX,mCurrentIndex);
        savedInstanceState.putIntegerArrayList(KEY_ANSWERED, mAnsweredQuestions);
        savedInstanceState.putCharArray(KEY_COLOR,mButtonBackgroundColor);
        savedInstanceState.putInt(KEY_SCORE,mNumberOfCorrectAnswers);
        savedInstanceState.putBoolean(KEY_VISIBLE,isVisible);
    }


    @Override
    public void onStop () {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }


}