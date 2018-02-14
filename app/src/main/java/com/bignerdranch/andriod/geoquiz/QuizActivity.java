package com.bignerdranch.andriod.geoquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {
    private final String TAG = QuizActivity.class.getSimpleName();
private Button mTrueButton;
private Button mFalseButton;
private ImageButton mNextButton;
private ImageButton mBackButton;
private TextView mQuestionTextView;

//array of question objects from values folder
private Question[] mQuestionBank = new Question[] {
        new Question(R.string.question_australia, true),
        new Question(R.string.question_africa, false),
        new Question(R.string.question_americas, true),
        new Question(R.string.question_oceans, true),
        new Question(R.string.question_asia, true),
        new Question(R.string.question_mideast, false)
};

// record answers
private String[] answers = new String[mQuestionBank.length];

private int mCurrentIndex = 0;
private int answerIndex = 0;
private int numberCorrect = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        updateQuestion();


        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // install Toast statement
               checkAnswer(true, answerIndex);
               mFalseButton.setEnabled(false);
                mTrueButton.setEnabled(false);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // install Toast statement
                checkAnswer(false, answerIndex);
                mTrueButton.setEnabled(false);
                mFalseButton.setEnabled(false);

            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                answerIndex = (answerIndex + 1) % answers.length;
                updateQuestion();
                Log.d(TAG, "onCreate: " + answers[answerIndex]);
                if(answers[answerIndex] == null){
                    mTrueButton.setEnabled(true);
                    mFalseButton.setEnabled(true);
                }else{
                    mTrueButton.setEnabled(false);
                    mFalseButton.setEnabled(false);
                }

            }
        });

        mBackButton = (ImageButton) findViewById(R.id.back_button);
        mBackButton.setOnClickListener((v) -> {
            mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
            answerIndex = (answerIndex - 1) % answers.length;
            if(answerIndex < 0 ){
                answerIndex = answers.length - 1;
            }
            updateQuestion();
            Log.d(TAG, "onCreate: " + answers[answerIndex]);
            if(answers[answerIndex] == null){
                mTrueButton.setEnabled(true);
                mFalseButton.setEnabled(true);
            }else{
                mTrueButton.setEnabled(false);
                mFalseButton.setEnabled(false);
            }


        });



    }

    private void updateQuestion(){
        if(mCurrentIndex < 0){
            mCurrentIndex = mQuestionBank.length - 1;
        }
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);

    }

    private void checkAnswer(boolean userPressedTrue, int answerIndex){
       boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

       int messageResId = 0;

       if(userPressedTrue == answerIsTrue){
           messageResId = R.string.correct_toast;
           answers[answerIndex] = "C";
           numberCorrect++;
           Log.d(TAG, "number corrrect: " + numberCorrect);

       } else {
           messageResId = R.string.incorrect_toast;
           answers[answerIndex] = "I";
       }

       Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

       // iterate over answers array to check if all questions where answered
        // and toast the result
        int check = 0;
       for(int i = 0;i<answers.length;i++){

           if(answers[i] != null){
               check++;
               Log.d(TAG, "checkAnswer: " + check);
           }
           if(check == answers.length){
               double percentage =   ((double) numberCorrect/answers.length) * 100;
               DecimalFormat percentageFormat = new DecimalFormat("00");
               String finalPercentage = percentageFormat.format(percentage);
               String message = "Your score is " + finalPercentage + "%";
               Toast.makeText(this, message, Toast.LENGTH_LONG).show();
           }
       }


    }
}
