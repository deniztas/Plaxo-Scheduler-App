package com.example.deniz.plaxo2.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.deniz.plaxo2.R;
import com.example.deniz.plaxo2.activities.ContactActivity;
import com.example.deniz.plaxo2.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {
    private Context mContext;
    private List<Contact> contactList = new ArrayList<>();

    public ContactAdapter(@NonNull Context context, ArrayList<Contact> list) {
        super(context, 0, list);
        mContext = context;
        contactList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.contact_row, parent, false);


        Contact currentContact = contactList.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.name);
        name.setText(currentContact.getContactName());

        name.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                TextView clickedText = (TextView) v;
                String name = clickedText.getText().toString();
                Intent in = new Intent(mContext, ContactActivity.class);
                in.putExtra("Object", name);
                mContext.startActivity(in);
            }
        });
        return listItem;
    }
}
