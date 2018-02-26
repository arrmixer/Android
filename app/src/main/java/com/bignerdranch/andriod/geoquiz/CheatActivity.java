package com.bignerdranch.andriod.geoquiz;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    private final String TAG = CheatActivity.class.getSimpleName();
    private Button mShowAnswerButton;
    private TextView mTextAnswer;
    private TextView mShowBuild;
    private boolean mAnswerIsTrue;
    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";
    private static final String EXTRA_CHEAT_USED = "com.bignerdranch.android.geoquiz.cheat_used";
    private static final String KEY = "KEY";
    private static final String KEY2 = "KEY2";
    private boolean mBoolean = false;
    private int cheats = 0;


    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    public static int amountOfCheatsIntent(Intent result){
        return result.getIntExtra(EXTRA_CHEAT_USED, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mTextAnswer = (TextView) findViewById(R.id.answer_text_view);

        //show current build
        mShowBuild = (TextView) findViewById(R.id.show_build_text);
        mShowBuild.setText("API Level " + Build.VERSION_CODES.N_MR1);

        if(savedInstanceState != null){
            mBoolean = savedInstanceState.getBoolean(KEY);
            cheats = savedInstanceState.getInt(KEY2);
            Log.d(TAG, "onCreate: mBoolean " + mBoolean);
        }
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(mAnswerIsTrue){
                mTextAnswer.setText(R.string.true_button);
            }else{
                mTextAnswer.setText(R.string.false_button);
            }
                cheats = 1;
                Log.d(TAG, "onClick: Show Answer" + cheats);
                setExtraAnswerShown(true);
            }

        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: " + mBoolean);
        outState.putBoolean(KEY, mBoolean);
        outState.putInt(KEY2, cheats);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: mBoolean " + mBoolean );
        Log.d(TAG, "onResume: cheats " + cheats);

    }

    private void setExtraAnswerShown(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        data.putExtra(EXTRA_CHEAT_USED, cheats);
        setResult(RESULT_OK, data);
        mBoolean = isAnswerShown;


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
