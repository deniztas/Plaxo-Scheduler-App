package com.example.deniz.plaxo2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.deniz.plaxo2.R;
import com.example.deniz.plaxo2.model.Contact;
import com.example.deniz.plaxo2.model.Note;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddNoteActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton fab;
    EditText etTitle, etDesc;
    TextView userName;
    TextView date;
    String title, note;
    String time;
    boolean editingNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnote_layout);

        Intent in = getIntent();
        final int id = in.getIntExtra("id",-1);
        Contact contact = null;
        if(id != -1){
            contact = Contact.find(Contact.class,"CONTACT_ID = ?", id+"").get(0);
        }


        getSupportActionBar().setTitle("Add new note");

        etTitle = (EditText) findViewById(R.id.addnote_title);
        etDesc = (EditText) findViewById(R.id.addnote_desc);
        userName = (TextView) findViewById(R.id.note_user_textview);
        date = (TextView) findViewById(R.id.note_date_textview);

        fab = (FloatingActionButton) findViewById(R.id.addnote_fab);

        editingNote = getIntent().getBooleanExtra("isEditing", false);
        if (editingNote) {
            title = getIntent().getStringExtra("note_title");
            note = getIntent().getStringExtra("note");
            time = getIntent().getStringExtra("note_time");


            List<Note> queried_notes = Note.find(Note.class, "title = ?", title);
            if(!queried_notes.isEmpty()){
                int userId = queried_notes.get(0).getUserId();


                List<Contact> queried_contacts = Contact.find(Contact.class, "CONTACT_ID = ?", userId+"");

                if(contact != null){
                    userName.setText(contact.getContactName());
                }
                if(!queried_contacts.isEmpty()){
                    Contact queried_contact = queried_contacts.get(0);
                    userName.setText(queried_contact.getContactName());
                }
                else{
                    userName.setText("");
                }
            }




            etTitle.setText(title);
            etDesc.setText(note);


            date.setText(time);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Add note to DB

                String newTitle = etTitle.getText().toString();
                String newDesc = etDesc.getText().toString();
                Date currentTime = Calendar.getInstance().getTime();
                Date date = currentTime;

                Format formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String date_string = formatter.format(date);


                /**
                 * TODO: Check if note exists before saving
                 */
                List<Note> notes = Note.listAll(Note.class);
                int size = notes.size();


                if (!editingNote) {
                    Log.d("Note", "saving");
                    Note note = new Note(newTitle, newDesc, date_string);
                    note.setNoteId(size+1);
                    if(id != -1){
                        note.setUserId(id);
                    }
                    note.save();
                } else {
                    Log.d("Note", "updating");
                    List<Note> queried_notes = Note.find(Note.class, "title = ?", title);
                    if (queried_notes.size() > 0) {
                        Note note = queried_notes.get(0);
                        Log.d("got note", "note: " + note.getTitle());
                        note.setTitle(newTitle);
                        note.setNote(newDesc);
                        note.setDate(date_string);

                        note.save();
                    }

                }
                finish();
            }
        });
    }
}

