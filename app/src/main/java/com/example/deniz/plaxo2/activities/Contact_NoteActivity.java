package com.example.deniz.plaxo2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.example.deniz.plaxo2.R;
import com.example.deniz.plaxo2.adapters.NotesAdapter;
import com.example.deniz.plaxo2.model.Note;

import java.util.ArrayList;
import java.util.List;

/*
 *   TO SHOW CONTACT'S LIST
 *   MAKES THE SAME THING WITH CONTACT FRAGMENT
 *
 *
 *
 *
 * */
public class Contact_NoteActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton fab;

    NotesAdapter adapter;
    List<Note> notes = new ArrayList<>();

    long initialCount;

    int modifyPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepage_layout);

        Intent in = getIntent();
        final int id = in.getIntExtra("id", -1);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        recyclerView = (RecyclerView) findViewById(R.id.main_list);


        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        recyclerView.setLayoutManager(gridLayoutManager);

        initialCount = Note.count(Note.class);

        if (savedInstanceState != null)
            modifyPos = savedInstanceState.getInt("modify");


        if (initialCount >= 0) {

            notes = Note.find(Note.class, "USER_ID = ?", id + "");

            adapter = new NotesAdapter(this.getBaseContext(), notes);
            recyclerView.setAdapter(adapter);

            if (notes.isEmpty())
                Snackbar.make(recyclerView, "No notes added.", Snackbar.LENGTH_LONG).show();

        }

        // Handling swipe to delete
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //Remove swiped item from list and notify the RecyclerView

                final int position = viewHolder.getAdapterPosition();
                final Note note = notes.get(viewHolder.getAdapterPosition());
                notes.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(position);

                note.delete();
                initialCount -= 1;

                Snackbar.make(recyclerView, "Note deleted", Snackbar.LENGTH_SHORT)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                note.save();
                                notes.add(position, note);
                                adapter.notifyItemInserted(position);
                                initialCount += 1;

                            }
                        })
                        .show();
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        adapter.SetOnItemClickListener(new NotesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Log.d("Main", "click");

                Intent i = new Intent(view.getContext(), AddNoteActivity.class);
                i.putExtra("isEditing", true);
                i.putExtra("note_title", notes.get(position).getTitle());
                i.putExtra("note", notes.get(position).getNote());
                i.putExtra("note_time", notes.get(position).getDate());
                i.putExtra("id", notes.get(position).getUserId());

                modifyPos = position;

                startActivity(i);
            }
        });
    }

    public void onResume() {
        super.onResume();

        final long newCount = Note.count(Note.class);

        if (newCount > initialCount) {
            // A note is added
            Log.d("Main", "Adding new note");

            // Just load the last added note (new)
            List<Note> allNotes = Note.listAll(Note.class);
            for (int i = (int) initialCount; i < newCount; i++) {
                Note note = allNotes.get(i);
                notes.add(note);
            }

            adapter.notifyItemInserted((int) newCount);

            initialCount = newCount;
        }

        if (modifyPos != -1) {
            notes.set(modifyPos, Note.listAll(Note.class).get(modifyPos));
            adapter.notifyItemChanged(modifyPos);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
