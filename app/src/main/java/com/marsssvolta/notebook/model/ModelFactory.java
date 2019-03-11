package com.marsssvolta.notebook.model;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class ModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private int mNoteId;

    public ModelFactory(Application application, int id) {
        mApplication = application;
        mNoteId = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new NoteViewModel(mApplication, mNoteId);
    }
}
