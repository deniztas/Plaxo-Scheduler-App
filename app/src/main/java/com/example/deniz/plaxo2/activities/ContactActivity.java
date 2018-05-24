package com.example.deniz.plaxo2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.deniz.plaxo2.R;
import com.example.deniz.plaxo2.model.Contact;

import java.util.List;

public class ContactActivity extends AppCompatActivity {

    private EditText name_surname;
    private EditText phone_number;
    private Button add_new_note_button;
    private Button list_notes_button;
    private int contactId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_info_layout);

        name_surname = (EditText) findViewById(R.id.name_surname_field);
        phone_number = (EditText) findViewById(R.id.phone_field);
        add_new_note_button = (Button) findViewById(R.id.add_new_note_button);
        list_notes_button = (Button) findViewById(R.id.list_notes_button);

        Intent intent = getIntent();
        String name = intent.getStringExtra("Object");
        List<Contact> contacts = Contact.find(Contact.class, "CONTACT_NAME = ?", name);
        contactId = contacts.get(0).getContactId();

        name_surname.setText(contacts.get(0).getContactName());
        phone_number.setText(contacts.get(0).getPhoneNumber());
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.add_new_note_button:
                Intent in = new Intent(this,AddNoteActivity.class);
                in.putExtra("id", contactId);
                startActivity(in);
                break;

            case R.id.list_notes_button:
                Intent in2 = new Intent(this,Contact_NoteActivity.class);
                in2.putExtra("id", contactId);
                startActivity(in2);
                break;
        }
    }
}
