package com.marsssvolta.notebook;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        mNoteId = (int) getIntent().getExtras().get(NOTE_ID);
        if(mNoteId != 0){
            init();
        }
    }

    public void init(){
        Note note = mNoteViewModel.getNote(mNoteId);
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
