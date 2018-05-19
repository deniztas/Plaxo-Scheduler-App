package com.example.deniz.plaxo2.adapters;

import android.content.Context;
import android.graphics.Movie;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.deniz.plaxo2.R;
import com.example.deniz.plaxo2.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {
    private Context mContext;
    private List<Contact> contactList = new ArrayList<>();
    Button permissionButton;
    TextView permissionText;
    ListView listView;


    public ContactAdapter(@NonNull Context context, ArrayList<Contact> list) {
        super(context, 0 , list);
        mContext = context;
        contactList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.contactpage_layout,parent,false);


        permissionButton = (Button) listItem.findViewById(R.id.permissionButton);
        permissionText = (TextView) listItem.findViewById(R.id.permissionText);
        listView = (ListView) listItem.findViewById(R.id.contactListview);
        permissionButton.setVisibility(View.GONE);
        permissionText.setVisibility(View.GONE);

        Contact currentContact = contactList.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.name);
        name.setText(currentContact.getContactName());

        name.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                TextView clickedText = (TextView)v;
                String name = clickedText.getText().toString();

                List<Contact> contacts = Contact.find(Contact.class, "CONTACT_NAME = ?", name);
                System.out.print(contacts);
            }
        });



        return listItem;
    }
}
