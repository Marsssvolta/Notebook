package com.marsssvolta.notebook.UIController;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.marsssvolta.notebook.Model.ModelFactory;
import com.marsssvolta.notebook.Model.Note;
import com.marsssvolta.notebook.Model.NoteViewModel;
import com.marsssvolta.notebook.R;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "com.marsssvolta.notebook.title";
    public static final String EXTRA_NOTE = "com.marsssvolta.notebook.note";
    public static final String NOTE_ID = "com.marsssvolta.notebook.noteId";

    private NoteViewModel mNoteViewModel;
    private EditText mEditTitle;
    private EditText mEditNote;
    private int mNoteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mEditTitle = findViewById(R.id.edit_title);
        mEditNote = findViewById(R.id.edit_note);
        Button button = findViewById(R.id.button_save);

        // Получение id через интент
        mNoteId = (int) Objects.requireNonNull(getIntent().getExtras()).get(NOTE_ID);
        // Установка значений, если id не равен нулю
        if(mNoteId != 0){
            init();
        }

        // Кнопка сохранения/обновления записи
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNoteId == 0) {
                    saveNewNote();
                } else {
                    updateNote();
                }
            }
        });
    }

    // Установка значений
    public void init(){
        mNoteViewModel = ViewModelProviders.of(this,
                new ModelFactory(this.getApplication(), mNoteId)).get(NoteViewModel.class);
        mNoteViewModel.getNote().observe(this, new Observer<Note>() {
            @Override
            public void onChanged(@Nullable Note note) {
                mEditTitle.setText(Objects.requireNonNull(note).getTitle());
                mEditNote.setText(Objects.requireNonNull(note).getNote());
            }
        });
    }

    // Сохранение записи
    public void saveNewNote(){
        Intent replyIntent = new Intent();
        if ((TextUtils.isEmpty(mEditTitle.getText())) & (TextUtils.isEmpty(mEditNote.getText()))) {
            this.setResult(RESULT_CANCELED, replyIntent);
        } else {
            String title = mEditTitle.getText().toString();
            String note = mEditNote.getText().toString();
            replyIntent.putExtra(EXTRA_TITLE, title);
            replyIntent.putExtra(EXTRA_NOTE, note);
            this.setResult(RESULT_OK, replyIntent);
        }
        this.finish();
    }

    // Обновление записи
    public void updateNote(){
        if ((TextUtils.isEmpty(mEditTitle.getText())) & (TextUtils.isEmpty(mEditNote.getText()))) {
            Snackbar.make(findViewById(R.id.detailCoordinatorLayout), R.string.empty_not_saved,
                    Snackbar.LENGTH_LONG).show();
        } else {
            String upTitle = mEditTitle.getText().toString();
            String upNote = mEditNote.getText().toString();
            mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
            mNoteViewModel.updateNote(upTitle, upNote, mNoteId);
            this.finish();
        }
    }
}
