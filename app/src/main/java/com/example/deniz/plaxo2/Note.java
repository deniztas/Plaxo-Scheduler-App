package com.example.deniz.plaxo2;

import com.orm.SugarRecord;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

/**
 * Created by Deniz on 13.03.2018.
 */

public class Note extends SugarRecord {
    @Unique
    int noteId;
    String title, note;
    long noteTime;

    // Default constructor is important!
    public Note(){

    }

    public Note(String title, String note, long noteTime) {
        this.title = title;
        this.note = note;
        this.noteTime = noteTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getNoteTime() {
        return noteTime;
    }

    public void setNoteTime(long noteTime) {
        this.noteTime = noteTime;
    }

}

