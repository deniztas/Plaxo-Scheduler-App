package com.example.deniz.plaxo2.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deniz.plaxo2.R;
import com.example.deniz.plaxo2.activities.AddNoteActivity;
import com.example.deniz.plaxo2.adapters.NotesAdapter;
import com.example.deniz.plaxo2.model.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deniz on 17.03.2018.
 */
public class NotePage extends Fragment {

    RecyclerView recyclerView;
    FloatingActionButton fab;

    NotesAdapter adapter;
    List<Note> notes = new ArrayList<>();

    long initialCount;

    int modifyPos = -1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.notepage_layout, container, false);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.main_list);


        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        recyclerView.setLayoutManager(gridLayoutManager);

        initialCount = Note.count(Note.class);

        if (savedInstanceState != null)
            modifyPos = savedInstanceState.getInt("modify");


        if (initialCount >= 0) {

            notes = Note.listAll(Note.class);

            adapter = new NotesAdapter(getActivity().getBaseContext(), notes);
            recyclerView.setAdapter(adapter);

            if (notes.isEmpty())
                Snackbar.make(recyclerView, "No notes added.", Snackbar.LENGTH_LONG).show();

        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity().getBaseContext(), AddNoteActivity.class);
                startActivity(intent);

            }
        });


        // tinting FAB icon
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            Drawable drawable = ContextCompat.getDrawable(getActivity().getBaseContext(), R.drawable.ic_add_24dp);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, Color.WHITE);
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);

            fab.setImageDrawable(drawable);

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

                Intent i = new Intent(getActivity().getBaseContext(), AddNoteActivity.class);
                i.putExtra("isEditing", true);
                i.putExtra("note_title", notes.get(position).getTitle());
                i.putExtra("note", notes.get(position).getNote());
                i.putExtra("note_time", notes.get(position).getDate());

                modifyPos = position;

                startActivity(i);
            }
        });


        return rootView;
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

