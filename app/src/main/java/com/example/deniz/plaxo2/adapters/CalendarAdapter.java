package com.example.deniz.plaxo2.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.deniz.plaxo2.R;
import com.example.deniz.plaxo2.model.Contact;
import com.example.deniz.plaxo2.model.Event;

import java.util.List;

public class CalendarAdapter extends ArrayAdapter<Event> {
    private Context mContext;
    private List<Event> eventList;

    public CalendarAdapter(@NonNull Context context, List<Event> list) {
        super(context, 0, list);
        mContext = context;
        eventList = list;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.event_row, parent, false);

        final Event currenctEvent = eventList.get(position);

        TextView event_name = (TextView) listItem.findViewById(R.id.event_name);
        event_name.setText(currenctEvent.getTitle());

        TextView event_start_date = (TextView) listItem.findViewById(R.id.event_start_date);
        String text = "Start Time " + currenctEvent.getStartHour() + ":" + currenctEvent.getStartMinute();
        event_start_date.setText(text);

        TextView event_end_date = (TextView) listItem.findViewById(R.id.event_end_date);
        text = "End Time " + currenctEvent.getEndHour() + ":" + currenctEvent.getEndMinute();
        event_end_date.setText(text);


        int userId = currenctEvent.getUserId();
        List<Contact> contacts = Contact.find(Contact.class, "CONTACT_ID = ?", userId + "");

        if (!contacts.isEmpty() && contacts.get(0).getContactId() > 0) {
            TextView event_user_name = (TextView) listItem.findViewById(R.id.event_user_name);
            event_user_name.setText(contacts.get(0).getContactName());
        }

        event_name.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //TODO
                //List<Contact> contacts = Contact.find(Contact.class, "CONTACT_NAME = ?", name);
                //System.out.print(contacts);
            }
        });

        event_name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String name = currenctEvent.getTitle();
                deleteEvent(name,v,position);
                return true;
            }
        });

        event_start_date.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String name = currenctEvent.getTitle();
                deleteEvent(name,v,position);
                return true;
            }
        });

        event_end_date.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String name = currenctEvent.getTitle();
                deleteEvent(name,v,position);
                return true;
            }
        });

        return listItem;
    }

    private void deleteEvent(String name, View v,final int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create(); //Read Update
        alertDialog.setTitle("Delete " + name);

        alertDialog.setButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Event currenctEvent = eventList.get(position);
                eventList.remove(position);
                int id = currenctEvent.getEventId();
                Event.executeQuery("DELETE FROM EVENT WHERE EVENT_ID = '" + id + "'");
                notifyDataSetChanged();
            }
        });

        alertDialog.show();  //<-- See This!

    }


}
