package com.marsssvolta.notebook;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class NoteRepository {

    private NoteDao mNoteDao;
    private LiveData<List<Note>> mAllNotes;

    NoteRepository(Application application) {
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        mNoteDao = db.noteDao();
        mAllNotes = mNoteDao.getAlphabetizedNotes();
    }

    LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
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
}
