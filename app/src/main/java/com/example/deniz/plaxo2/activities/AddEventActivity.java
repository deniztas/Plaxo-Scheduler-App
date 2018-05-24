package com.example.deniz.plaxo2.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.deniz.plaxo2.R;
import com.example.deniz.plaxo2.model.Event;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Deniz on 19.05.2018.
 */

public class AddEventActivity extends AppCompatActivity {

    FloatingActionButton addEvent_fab;
    TextView dateTextview;
    TimePicker startTime;
    TimePicker endTime;
    int start_hour;
    int start_minute;
    int end_hour;
    int end_minute;
    EditText title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addevent_layout);
        Intent intent = getIntent();

        dateTextview = (TextView) findViewById(R.id.selected_date);
        startTime = (TimePicker) findViewById(R.id.start_time_picker);
        endTime = (TimePicker) findViewById(R.id.end_time_picker);
        addEvent_fab = (FloatingActionButton) findViewById(R.id.addEvent_fab);
        title = (EditText) findViewById(R.id.event_title);

        long selectedTime = intent.getLongExtra("Date",0);
        final Date selectedDate = new Date(selectedTime);
        Log.d("selectedTime", selectedDate+"");

        dateTextview.setText(selectedDate+"");

        start_hour = startTime.getCurrentHour();
        start_minute = startTime.getCurrentMinute();
        startTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                start_hour = hourOfDay;
                start_minute = minute;
            }
        });

        end_hour = endTime.getCurrentHour();
        end_minute = endTime.getCurrentMinute();

        endTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                end_hour = hourOfDay;
                end_minute = minute;
            }
        });





        addEvent_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((start_hour == end_hour && end_minute > start_minute) || start_hour < end_hour){
                    Toast.makeText(getBaseContext(), "OK", Toast.LENGTH_SHORT).show();

                    List<Event> allEvents = Event.listAll(Event.class);
                    int size = allEvents.size();

                    Event event = new Event();
                    event.setEventId(size+1);
                    event.setDate(selectedDate);
                    event.setEndHour(end_hour);
                    event.setEndMinute(end_minute);
                    event.setStartHour(start_hour);
                    event.setStartMinute(start_minute);
                    event.setTitle(title.getText()+"");
                    event.save();
                    finish();
                }
                else{
                    Toast.makeText(getBaseContext(), "Cannot choose before", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}