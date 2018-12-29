package com.marsssvolta.notebook.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    @ColumnInfo(name = "note")
    private String mNote;

    public Note(@NonNull String note) {
        this.mNote = note;
    }

    @NonNull
    public String getNote() {
        return this.mNote;
    }
}
