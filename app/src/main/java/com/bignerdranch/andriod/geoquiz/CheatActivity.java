package com.bignerdranch.andriod.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    private Button mShowAnswerButton;
    private TextView mTextAnswer;
    private boolean mAnswerIsTrue;
    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.answer_shown";


    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mTextAnswer = (TextView) findViewById(R.id.answer_text_view);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(mAnswerIsTrue){
                mTextAnswer.setText(R.string.true_button);
            }else{
                mTextAnswer.setText(R.string.false_button);
            }
                setExtraAnswerShown(true);

            int cx = mShowAnswerButton.getWidth() / 2;
            int cy = mShowAnswerButton.getHeight() / 2;
            float radius = mShowAnswerButton.getWidth();

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation, boolean isReverse) {
                        super.onAnimationEnd(animation);
                        mShowAnswerButton.setVisibility(View.INVISIBLE);
                    }
                });
                anim.start();
            }else{
                mShowAnswerButton.setVisibility(View.INVISIBLE);
            }

            }

        });


    }



    private void setExtraAnswerShown(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }
}
