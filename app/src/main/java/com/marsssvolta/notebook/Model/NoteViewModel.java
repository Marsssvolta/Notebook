package com.marsssvolta.notebook.Model;

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

    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    public LiveData<Note> getNote(){
        return mNote;
    }

    public void insert(Note note) {
        mRepository.insert(note);
    }

    public NoteRepository getRepository() {
        return mRepository;
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteNote(int id) {
        mRepository.deleteNote(id);
    }

    public void updateNote(String upNote, int id){
        mRepository.updateNote(upNote, id);
    }
}
