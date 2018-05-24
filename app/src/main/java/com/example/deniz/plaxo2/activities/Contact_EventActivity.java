package com.example.deniz.plaxo2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.deniz.plaxo2.R;
import com.example.deniz.plaxo2.adapters.CalendarAdapter;
import com.example.deniz.plaxo2.model.Event;

import java.util.List;

public class Contact_EventActivity extends AppCompatActivity {

    private ListView contact_event_listview;
    private CalendarAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_event);
        contact_event_listview = (ListView) findViewById(R.id.contact_event_listview);

        Intent i = getIntent();
        int userId = i.getIntExtra("userId", 0);

        List<Event> events = Event.find(Event.class, "USER_ID = ?", userId + "");

        mAdapter = new CalendarAdapter(this, events);

        contact_event_listview.setAdapter(mAdapter);
    }
}
