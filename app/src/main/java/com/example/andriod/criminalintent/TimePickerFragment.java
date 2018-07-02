package com.example.andriod.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Angel on 4/5/18.
 */

public class TimePickerFragment extends DialogFragment {

    //Tags for Bundle and Intent
    public static final String ARG_TIME = "time";
    public static final String EXTRA_TIME = "com.example.andriod.criminalintent.time";

    private TimePicker mTimePicker;

    //varibles for the time picked
    int hourPicked;
    int minutePicked;

    public static TimePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, date);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable(ARG_TIME);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        //keep the same date only want to change the time
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int amPm = calendar.get(Calendar.AM_PM);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time, null);

        int id = R.id.crime_time;

        mTimePicker = new TimePicker(getContext());
        mTimePicker.setIs24HourView(false);

        //method names changed after 23
        if (Build.VERSION.SDK_INT >= 23) {
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minute);

        } else {
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(minute);
        }



        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //method names changed after 23
                        if (Build.VERSION.SDK_INT >= 23) {
                            hourPicked = mTimePicker.getHour();
                            minutePicked = mTimePicker.getMinute();

                        } else {
                            hourPicked = mTimePicker.getCurrentHour();
                            minutePicked = mTimePicker.getCurrentMinute();
                        }

                        Date date = new GregorianCalendar(year, month, day, hourPicked, minutePicked).getTime();
                        sendResult(Activity.RESULT_OK, date);


                    }
                }).create();

    }

    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
