package com.marsssvolta.notebook.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "title")
    private String mTitle;

    @NonNull
    @ColumnInfo(name = "note")
    private String mNote;

    public Note(@NonNull String title, @NonNull String note) {
        this.mNote = note;
        this.mTitle = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getNote() {
        return this.mNote;
    }

    @NonNull
    public String getTitle() {
        return this.mTitle;
    }
}
