package com.marsssvolta.notebook;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository mRepository;

    private LiveData<List<Note>> mAllNotes;
    private LiveData<Note> mNote;

    public NoteViewModel(Application application) {
        super(application);
        mRepository = new NoteRepository(application);
        mAllNotes = mRepository.getAllNotes();
    }

    public NoteViewModel(Application application, int id) {
        super(application);
        mRepository = new NoteRepository(application, id);
        mNote = mRepository.getNote();
    }

    LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    LiveData<Note> getNote(){
        return mNote;
    }

    void insert(Note note) {
        mRepository.insert(note);
    }

    void deleteAll() {
        mRepository.deleteAll();
    }

    void deleteNote(int id) {
        mRepository.deleteNote(id);
    }

    void updateNote(String upNote, int id){
        mRepository.updateNote(upNote, id);
    }
}
