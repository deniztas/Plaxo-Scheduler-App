package com.example.deniz.plaxo2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.deniz.plaxo2.R;
import com.example.deniz.plaxo2.activities.AddEventActivity;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Deniz on 17.03.2018.
 */

public class CalendarPage extends Fragment {
    CalendarView calendar;
    FloatingActionButton fabCalendar;
    static long selectedDate;
    int mYear, mMonth, mDay, mHour, mMinute;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.calendarpage_layout, container, false);
        initializeCalendar(rootView);
        fabCalendar = (FloatingActionButton) rootView.findViewById(R.id.fabCalendar);

        fabCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity().getBaseContext(), AddEventActivity.class);
                intent.putExtra("Date", selectedDate);
                startActivity(intent);

            }
        });

        return rootView;
    }
    public void initializeCalendar(View rootView) {


        calendar = (CalendarView) rootView.findViewById(R.id.calendarId);

        calendar.setShowWeekNumber(false);

        calendar.setFirstDayOfWeek(2);

        selectedDate = calendar.getDate();

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            //show the selected date as a toast

            @Override

            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Date date = new GregorianCalendar(year, month, day).getTime();
                selectedDate = date.getTime();
                Toast.makeText(getActivity().getBaseContext(), date+"", Toast.LENGTH_SHORT).show();

            }

        });

    }
}



