package com.bignerdranch.andriod.geoquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = QuizActivity.class.getSimpleName();
    private static final String KEY_INDEX = "index";
    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";
    private int REQUEST_CHEAT_CODE = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;

    //array of question objects from values folder
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_asia, true),
            new Question(R.string.question_mideast, false)
    };

    private int mCurrentIndex = 0;
    private boolean mIsCheater;
    private boolean mIsStillCheater;

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: called");
        Intent intent = getIntent();
        Log.d(TAG, "onStart: Boolean " + mIsCheater);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume: called");
        Log.d(TAG, "onResume: Boolean " + mIsCheater);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(EXTRA_ANSWER_SHOWN, mIsCheater);
        Log.d(TAG, "onSaveInstanceState: Boolean " + mIsCheater);



    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: called");
    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called ");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(EXTRA_ANSWER_SHOWN);
        }


        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        updateQuestion();


        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // install Toast statement
                checkAnswer(true);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // install Toast statement
                checkAnswer(false);

            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                    boolean isAnswerTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                    Context context = QuizActivity.this;
                    Intent cheatIntent = newIntent(context, isAnswerTrue );

                    startActivityForResult(cheatIntent, REQUEST_CHEAT_CODE);


                                            }
                                        }

        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        Log.d(TAG, "onActivityResult: Intent Data is " +  data);
        if(requestCode == REQUEST_CHEAT_CODE){
            if (data == null){
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
//            mIsStillCheater = mIsCheater;
        }
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;
        if(mIsCheater){
            messageResId = R.string.judgement_toast;
        }else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }


        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

    }



}
