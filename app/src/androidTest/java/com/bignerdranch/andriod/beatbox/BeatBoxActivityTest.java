package com.bignerdranch.andriod.beatbox;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.junit.Assert.*;

/**
 * Created by Angel on 4/10/18.
 */

@RunWith(AndroidJUnit4.class)
public class BeatBoxActivityTest {
   @Rule
    public ActivityTestRule<BeatBoxActivity> mBeatBoxActivityActivityTestRule =
           new ActivityTestRule<>(BeatBoxActivity.class);

   @Test
    public void showsFirstFileName(){
       onView(withText("65_cjipie"))
               .check(matches(anything()));
   }

   @Test
    public void onClickFileName(){
        onView(withText("65_cjipie"))
                .perform(click());
   }


}