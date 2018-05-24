package com.example.deniz.plaxo2.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.deniz.plaxo2.R;
import com.example.deniz.plaxo2.model.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
    String selectedDate;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addevent_layout);
        boolean permission = checkIfAlreadyhavePermission();
        if (!permission) {
            requestForSpecificPermission();
        }

        Bundle bundle = getIntent().getExtras();

        userId = bundle.getInt("userId", 0);

        dateTextview = (TextView) findViewById(R.id.selected_date);
        startTime = (TimePicker) findViewById(R.id.start_time_picker);
        endTime = (TimePicker) findViewById(R.id.end_time_picker);
        addEvent_fab = (FloatingActionButton) findViewById(R.id.addEvent_fab);
        title = (EditText) findViewById(R.id.event_title);

        selectedDate = bundle.getString("Date");

        dateTextview.setText(selectedDate);

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
                if ((start_hour == end_hour && end_minute > start_minute) || start_hour < end_hour) {
                    boolean permission = checkIfAlreadyhavePermission();

                    Toast.makeText(getBaseContext(), "OK", Toast.LENGTH_SHORT).show();

                    List<Event> allEvents = Event.listAll(Event.class);
                    int size = allEvents.size();

                    Event event = new Event();
                    event.setUserId(userId);
                    event.setEventId(size + 1);
                    event.setDate(selectedDate);
                    event.setEndHour(end_hour);
                    event.setEndMinute(end_minute);
                    event.setStartHour(start_hour);
                    event.setStartMinute(start_minute);
                    event.setTitle(title.getText() + "");
                    event.save();
                    if (permission)
                        addCustomEventToCalendar(size + 1, title.getText() + "");
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "Cannot choose before", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addCustomEventToCalendar(int calendarId, String title) {
        String start_eventDate = selectedDate + " " + start_hour + ":" + start_minute + ":" + "00";
        String end_eventDate = selectedDate + " " + end_hour + ":" + end_minute + ":" + "00";
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        try {
            Date startdate = format.parse(start_eventDate);
            Date enddate = format.parse(end_eventDate);
            long startDate = startdate.getTime();
            long endDate = enddate.getTime();

            ContentValues event = new ContentValues();
            event.put("calendar_id", calendarId);
            event.put("title", title);
            event.put("description", "Plaxo Event");
            event.put("eventLocation", "None");
            event.put("eventTimezone", TimeZone.getDefault().getID());
            event.put("dtstart", startDate);
            event.put("dtend", endDate);

            event.put("allDay", 0); // 0 for false, 1 for true
            event.put("eventStatus", 1);
            event.put("hasAlarm", 1); // 0 for false, 1 for true

            String eventUriString = "content://com.android.calendar/events";
            Uri eventUri = getBaseContext().getApplicationContext()
                    .getContentResolver()
                    .insert(Uri.parse(eventUriString), event);
            long eventID = Long.parseLong(eventUri.getLastPathSegment());

            int minutes = 120;

            // add reminder for the event
            ContentValues reminders = new ContentValues();
            reminders.put("event_id", eventID);
            reminders.put("method", "1");
            reminders.put("minutes", minutes);

            String reminderUriString = "content://com.android.calendar/reminders";
            getBaseContext().getApplicationContext().getContentResolver()
                    .insert(Uri.parse(reminderUriString), reminders);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, 101);
    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
}