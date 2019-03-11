package com.marsssvolta.notebook.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageButton;
import android.widget.TextView;

import com.marsssvolta.notebook.model.Note;
import com.marsssvolta.notebook.model.NoteViewModel;
import com.marsssvolta.notebook.R;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_NOTE_ACTIVITY_REQUEST_CODE = 1;

    private NoteViewModel mNoteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        NoteListAdapter adapter = new NoteListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        // Установка списка
        mNoteViewModel.getAllNotes().observe(this, adapter::setNotes);

        // Кнопка добавления записи
        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(view -> addNewNote());
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
                .setPositiveButton(R.string.delete, (dialog, whichButton) -> {
                    mNoteViewModel.deleteAll();
                    Snackbar.make(MainActivity.this.findViewById(R.id.mainCoordinatorLayout), R.string.toast_delete_notes, Snackbar.LENGTH_LONG).show();
                }).setNegativeButton(R.string.cancel, null).show();
    }

    // Диалог удаления записи
    public void deleteNoteDialog(int id) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_delete_one_note)
                .setPositiveButton(R.string.delete, (dialog, whichButton) -> {
                    mNoteViewModel.deleteNote(id);
                    Snackbar.make(findViewById(R.id.mainCoordinatorLayout),
                            R.string.toast_delete_note, Snackbar.LENGTH_LONG).show();
                }).setNegativeButton(R.string.cancel, null).show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Сохранение данных, если запись не пустая
        if (requestCode == NEW_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Note note = new Note(data.getStringExtra(DetailActivity.EXTRA_TITLE),
                    data.getStringExtra(DetailActivity.EXTRA_NOTE));
            mNoteViewModel.insert(note);
        } else {
            Snackbar.make(findViewById(R.id.mainCoordinatorLayout), R.string.empty_not_saved,
                    Snackbar.LENGTH_LONG).show();
        }
    }

    public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteViewHolder>{

        private final LayoutInflater mInflater;
        private List<Note> mNotes = Collections.emptyList();
        private View itemView;

        NoteListAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        class NoteViewHolder extends RecyclerView.ViewHolder {
            private final TextView noteTitle;
            private final TextView noteText;
            private final ImageButton deleteButton;

            private NoteViewHolder(View itemView) {
                super(itemView);
                noteTitle = itemView.findViewById(R.id.text_title);
                noteText = itemView.findViewById(R.id.text_note);
                deleteButton = itemView.findViewById(R.id.delete_button);
            }
        }

        @NonNull
        @Override
        public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            itemView = mInflater.inflate(R.layout.item_card, parent, false);
            return new NoteViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
            Note current = mNotes.get(position);
            int noteId = current.getId();
            holder.noteTitle.setText(current.getTitle());
            holder.noteText.setText(current.getNote());

            // Переход к редактированию
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.NOTE_ID, noteId);
                startActivity(intent);
            });

            // Кнопка удаления записи
            holder.deleteButton.setOnClickListener(v -> deleteNoteDialog(noteId));
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
