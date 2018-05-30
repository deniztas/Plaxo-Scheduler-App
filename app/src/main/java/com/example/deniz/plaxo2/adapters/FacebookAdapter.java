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
import android.widget.TextView;

import com.example.deniz.plaxo2.R;
import com.example.deniz.plaxo2.model.Contact;
import com.example.deniz.plaxo2.model.Event;
import com.example.deniz.plaxo2.model.FacebookObj;

import java.util.List;

public class FacebookAdapter extends ArrayAdapter<FacebookObj> {
    private Context mContext;
    private List<FacebookObj> facebookList;

    public FacebookAdapter(@NonNull Context context, List<FacebookObj> list) {
        super(context, 0, list);
        mContext = context;
        facebookList = list;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.facebook_row, parent, false);

        FacebookObj currentFacebook = facebookList.get(position);

        TextView event_name = (TextView) listItem.findViewById(R.id.facebook_message);
        event_name.setText(currentFacebook.getMessage());

        TextView event_start_date = (TextView) listItem.findViewById(R.id.facebook_message_time);
        event_start_date.setText(currentFacebook.getCreatedDate().toString());

        return listItem;
    }
}
