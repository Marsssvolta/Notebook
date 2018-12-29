package com.marsssvolta.notebook.UIController;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.marsssvolta.notebook.Model.ModelFactory;
import com.marsssvolta.notebook.Model.Note;
import com.marsssvolta.notebook.Model.NoteViewModel;
import com.marsssvolta.notebook.R;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.marsssvolta.notebook.reply";
    public static final String NOTE_ID = "com.marsssvolta.notebook.noteId";

    private NoteViewModel mNoteViewModel;
    private EditText mEditNoteView;
    private int mNoteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mEditNoteView = findViewById(R.id.edit_note);
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
                mEditNoteView.setText(Objects.requireNonNull(note).getNote());
            }
        });
    }

    // Сохранение записи
    public void saveNewNote(){
        Intent replyIntent = new Intent();
        if (TextUtils.isEmpty(mEditNoteView.getText())) {
            this.setResult(RESULT_CANCELED, replyIntent);
        } else {
            String note = mEditNoteView.getText().toString();
            replyIntent.putExtra(EXTRA_REPLY, note);
            this.setResult(RESULT_OK, replyIntent);
        }
        this.finish();
    }

    // Обновление записи
    public void updateNote(){
        if (TextUtils.isEmpty(mEditNoteView.getText())) {
            Toast.makeText(this, R.string.empty_not_saved, Toast.LENGTH_LONG).show();
        } else {
            String upNote = mEditNoteView.getText().toString();
            mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
            mNoteViewModel.updateNote(upNote, mNoteId);
            this.finish();
        }
    }
}
