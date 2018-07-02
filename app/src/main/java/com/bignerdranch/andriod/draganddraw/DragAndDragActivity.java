package com.bignerdranch.andriod.draganddraw;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DragAndDragActivity extends SingleFragmentActivity {
    /*
    * This project illustrates how to make a custom
    * view. There are two types: Simple and composite.
    * Simple does it's own rendering since it doesn't have
    * any children vs custom the children do their own layering.
    *
    * 3 steps
    * 1. Pick a superclass. View = simple FrameLayout = custom
    * 2. Subclass this class and override the constructors from the superclass.
    * 3. Override other key methods to customize behavior.
    * */

    @Override
    protected Fragment createFragment() {
        return DragAndDrawFragment.newInstance();
    }
}
