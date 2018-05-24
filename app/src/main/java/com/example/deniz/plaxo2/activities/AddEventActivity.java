package com.example.deniz.plaxo2.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.deniz.plaxo2.R;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Deniz on 19.05.2018.
 */

public class AddEventActivity extends AppCompatActivity {

    FloatingActionButton addEvent_fab;
    TextView dateTextview;
    TimePicker startTime;
    TimePicker endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addevent_layout);
        Intent intent = getIntent();

        dateTextview = (TextView) findViewById(R.id.selected_date);
        startTime = (TimePicker) findViewById(R.id.start_time_picker);
        endTime = (TimePicker) findViewById(R.id.end_time_picker);

        long selectedTime = intent.getLongExtra("Date",0);
        Date selectedDate = new Date(selectedTime);
        Log.d("selectedTime", selectedDate+"");

        dateTextview.setText(selectedDate+"");

        int start_hour = startTime.getCurrentHour();
        int start_minute = startTime.getCurrentMinute();

        int end_hour = endTime.getCurrentHour();
        int end_minute = endTime.getCurrentMinute();

        addEvent_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}