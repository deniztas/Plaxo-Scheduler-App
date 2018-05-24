package com.example.deniz.plaxo2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.deniz.plaxo2.R;
import com.example.deniz.plaxo2.activities.AddEventActivity;
import com.example.deniz.plaxo2.adapters.CalendarAdapter;
import com.example.deniz.plaxo2.model.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Deniz on 17.03.2018.
 */

public class CalendarPage extends Fragment {
    CalendarView calendar;
    FloatingActionButton fabCalendar;
    static String selectedDate;
    private CalendarAdapter mAdapter;
    private ListView calendar_listview;
    private int id = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.calendarpage_layout, container, false);
        id = 0;

        if (getArguments() != null) {
            id = Integer.parseInt(getArguments().getString("id"));
        }

        initializeCalendar(rootView);

        fabCalendar = (FloatingActionButton) rootView.findViewById(R.id.fabCalendar);
        calendar_listview = (ListView) rootView.findViewById(R.id.event_listview);


        fabCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(), AddEventActivity.class);
                intent.putExtra("Date", selectedDate);
                intent.putExtra("userId", id);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<Event> events = Event.find(Event.class, "DATE = ?", selectedDate + "");
        mAdapter = new CalendarAdapter(getActivity(), events);

        calendar_listview.setAdapter(mAdapter);
    }


    public void initializeCalendar(View rootView) {
        calendar = (CalendarView) rootView.findViewById(R.id.calendarId);
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, c.getMinimum(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE, c.getMinimum(Calendar.MINUTE));
        c.set(Calendar.SECOND, c.getMinimum(Calendar.SECOND));
        c.set(Calendar.MILLISECOND, c.getMinimum(Calendar.MILLISECOND));
        calendar.setMinDate(c.getTimeInMillis());
        calendar.setShowWeekNumber(false);
        calendar.setFirstDayOfWeek(2);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        selectedDate = sdf.format(new Date(calendar.getDate()));

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Date date = new GregorianCalendar(year, month, day).getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                selectedDate = sdf.format(new Date(date.getTime()));
                Toast.makeText(getActivity().getBaseContext(), selectedDate + "", Toast.LENGTH_SHORT).show();


                List<Event> events = Event.find(Event.class, "DATE = ?", selectedDate + "");
                mAdapter = new CalendarAdapter(getActivity(), events);

                calendar_listview.setAdapter(mAdapter);
            }
        });

    }
}



