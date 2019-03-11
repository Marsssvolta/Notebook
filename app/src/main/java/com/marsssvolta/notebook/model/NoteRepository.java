package com.marsssvolta.notebook.model;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class NoteRepository {

    private NoteDao mNoteDao;
    private LiveData<List<Note>> mAllNotes;
    private LiveData<Note> mNote;

    NoteRepository(Application application) {
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        mNoteDao = db.noteDao();
        mAllNotes = mNoteDao.getNotes();
    }

    NoteRepository(Application application, int id) {
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        mNoteDao = db.noteDao();
        mNote = mNoteDao.getNote(id);
    }

    LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    LiveData<Note> getNote() {
        return mNote;
    }

    void insert(Note note) {
        new insertAsyncTask(mNoteDao).execute(note);
    }

    private static class insertAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDao mAsyncTaskDao;

        insertAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    void deleteAll(){
        new DeleteAllAsyncTask(mNoteDao).execute();
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private NoteDao mAsyncTaskDao;

        DeleteAllAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    void deleteNote(int id){
        new deleteNoteAsyncTask(mNoteDao).execute(id);
    }

    private static class deleteNoteAsyncTask extends AsyncTask<Integer, Void, Void> {

        private NoteDao mAsyncTaskDao;

        deleteNoteAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.deleteNote(params[0]);
            return null;
        }
    }

    void updateNote(String upTitle, String upNote, int id){
        new updateNoteAsyncTask(mNoteDao, upTitle, upNote, id).execute();
    }

    private static class updateNoteAsyncTask extends AsyncTask<updateNoteAsyncTask, Void, Void> {

        private NoteDao mAsyncTaskDao;
        private String mUpTitle;
        private String mUpNote;
        private int mNoteId;

        updateNoteAsyncTask(NoteDao dao, String upTitle, String upNote, int id) {
            mAsyncTaskDao = dao;
            mUpTitle = upTitle;
            mUpNote = upNote;
            mNoteId = id;
        }

        @Override
        protected Void doInBackground(updateNoteAsyncTask... params) {
            mAsyncTaskDao.updateNote(mUpTitle, mUpNote, mNoteId);
            return null;
        }
    }
}
