package com.marsssvolta.notebook;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_NOTE_ACTIVITY_REQUEST_CODE = 1;

    private NoteViewModel mNoteViewModel;
    private int mNoteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        NoteListAdapter adapter = new NoteListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        mNoteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                adapter.setNotes(notes);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewNote();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteListDialog();
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    // Добавление новой записи
    public void addNewNote(){
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.NOTE_ID, 0);
        startActivityForResult(intent, NEW_NOTE_ACTIVITY_REQUEST_CODE);
    }

    // Диалог очистки списка
    public void deleteListDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_delete_all_notes)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mNoteViewModel.deleteAll();
                        Toast.makeText(MainActivity.this, R.string.toast_delete_notes, Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton(R.string.cancel, null).show();
    }

    // Диалог удаления записи
    public void deleteNoteDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_delete_one_note)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mNoteViewModel.deleteNote(mNoteId);
                        Toast.makeText(MainActivity.this, R.string.toast_delete_note, Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton(R.string.cancel, null).show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Note note = new Note(data.getStringExtra(DetailActivity.EXTRA_REPLY));
            mNoteViewModel.insert(note);
        } else {
            Toast.makeText(this, R.string.empty_not_saved, Toast.LENGTH_LONG).show();
        }
    }

    public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteViewHolder>{

        private final LayoutInflater mInflater;
        private List<Note> mNotes = Collections.emptyList();

        NoteListAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        class NoteViewHolder extends RecyclerView.ViewHolder {
            private final TextView noteItemView;
            private final TextView noteId;

            private NoteViewHolder(View itemView) {
                super(itemView);
                noteItemView = itemView.findViewById(R.id.textTitle);
                noteId = itemView.findViewById(R.id.noteId);

                itemView.setOnClickListener(v -> {
                    String strId = noteId.getText().toString();
                    mNoteId = Integer.parseInt(strId);
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra(DetailActivity.NOTE_ID, mNoteId);
                    startActivity(intent);
                });

                Button button = itemView.findViewById(R.id.action_button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strId = noteId.getText().toString();
                        mNoteId = Integer.parseInt(strId);
                        deleteNoteDialog();
                    }
                });
            }
        }

        @NonNull
        @Override
        public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = mInflater.inflate(R.layout.item_card, parent, false);
            return new NoteViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
            Note current = mNotes.get(position);
            holder.noteItemView.setText(current.getNote());
            holder.noteId.setText(current.getTextId());
        }

        void setNotes(List<Note> notes) {
            mNotes = notes;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mNotes.size();
        }
    }
}
