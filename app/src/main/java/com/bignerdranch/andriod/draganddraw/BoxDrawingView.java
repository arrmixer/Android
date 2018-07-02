package com.bignerdranch.andriod.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/*
 * Making super class for custom view
 * it is good practice to include both
 * constructors just in case
 *
 *
 * This custom view with have data of
 * Box objects stored in an Arraylist
 * */

public class BoxDrawingView extends View {

    private static final String TAG = BoxDrawingView.class.getSimpleName();

    //reference to Box objects
    private Box mCurrentBox;

    //Data structure to store Box objects 
    private List<Box> mBoxes = new ArrayList<>();

    /*
    *  Paint class used to determine how
    *   Canvas operations are done...
    *  fill in, font, etc
     */

    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    // Used when creating the view in code
    public BoxDrawingView(Context context) {
        this(context, null);
    }

    // Used when inflating the view from XML
    public BoxDrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        /*
        * Since using XML will initialize the Paint
        * objects in this constructor
        * */

        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //container class to move your x and y coordinates around
        //your app
        PointF current = new PointF(event.getX(), event.getY());
        String action = "";

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";

                // Reset drawing state
                mCurrentBox = new Box(current);
                mBoxes.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";

                //if touching screen change second point's direction
                //to create the box image
                if(mCurrentBox != null)
                    mCurrentBox.setCurrent(current);

                //this metohod forces BoxDrawingView to redraw
                //itself so that the user can see the box while
                //drawing across the screen.
                    invalidate();

                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";

                //if finger is removed, make current box null
                //to complete the box
                mCurrentBox = null;

                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";

                //if touch is cancel make current box null
                //to complete or cancel making the box
                mCurrentBox = null;

                break;
        }

        Log.i(TAG, action + " at x= " + current.x +
                " at y= " + current.y);

        return true;
    }

    /*
    * Use View onDraw method to access
    * the Canvas object to draw
    * lines, shapes, points, etc...
    * */

    @Override
    protected void onDraw(Canvas canvas) {
      //Fill in the background
        canvas.drawPaint(mBackgroundPaint);

       /*
       * iterate through each of the Box objects
       * that contain its coordinates (represented by PointF objects)
       * and record the relative lengths for the four sides.
       * left and right sides get the min value
       * top and down get the max values
       * then draw the rectangle using Canvas's drawRect method
       * */

        for(Box box : mBoxes){
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);

            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }


    }
}
