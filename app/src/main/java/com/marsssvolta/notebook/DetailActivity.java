package com.marsssvolta.notebook;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.marsssvolta.notebook.reply";
    public static final String NOTE_ID = "com.marsssvolta.notebook.noteId";

    private NoteViewModel mNoteViewModel;
    private EditText mEditNoteView;
    private Button mButton;
    private int mNoteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mEditNoteView = findViewById(R.id.edit_note);
        mButton = findViewById(R.id.button_save);
        mButton.setOnClickListener(view -> saveNewNote());

        mNoteId = (int) getIntent().getExtras().get(NOTE_ID);
        if(mNoteId != 0){
            init();
        }
    }

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
}
