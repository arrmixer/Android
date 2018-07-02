package com.bignerdranch.andriod.draganddraw;

import android.graphics.PointF;

/*
* container class for the Box object
*
* */
public class Box {

    private PointF mOrigin;
    private PointF mCurrent;

    //in constructor both points initially the same
    public Box(PointF origin) {
        mOrigin = origin;
        mCurrent = origin;
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }
}
