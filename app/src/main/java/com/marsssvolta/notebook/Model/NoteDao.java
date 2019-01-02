package com.marsssvolta.notebook.Model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM note_table")
    LiveData<List<Note>> getNotes();

    @Insert
    void insert(Note note);

    @Query("DELETE FROM note_table")
    void deleteAll();

    @Query("DELETE FROM note_table WHERE id =:noteId")
    void deleteNote(int noteId);

    @Query("SELECT * FROM note_table WHERE id =:noteId")
    LiveData<Note> getNote(int noteId);

    @Query("UPDATE note_table SET title = :upTitle, note = :upNote WHERE id =:noteId")
    void updateNote(String upTitle, String upNote, int noteId);
}
