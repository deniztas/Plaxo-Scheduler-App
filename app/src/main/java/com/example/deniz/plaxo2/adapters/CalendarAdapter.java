package com.example.deniz.plaxo2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.deniz.plaxo2.model.Contact;
import com.example.deniz.plaxo2.model.Event;

import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends ArrayAdapter <Event> {
    private Context mContext;
    private List<Event> eventList = new ArrayList<>();
    Button permissionButton;
    TextView permissionText;
    ListView listView;

    public CalendarAdapter(@NonNull Context context, ArrayList<Event> list) {
        super(context, 0 , list);
        mContext = context;
        eventList = list;
    }
}
