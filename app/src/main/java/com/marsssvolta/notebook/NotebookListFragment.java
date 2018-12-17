package com.marsssvolta.notebook;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class NotebookListFragment extends Fragment {

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    private RecyclerView mRecyclerView;
    private NoteViewModel mNoteViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        final NoteListAdapter adapter = new NoteListAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        mNoteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable final List<Note> notes) {
                // Update the cached copy of the words in the adapter.
                adapter.setNotes(notes);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        FloatingActionButton fab = view.findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
                //startActivity(intent);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Note note = new Note(data.getStringExtra(NotebookDetailFragment.EXTRA_REPLY));
            mNoteViewModel.insert(note);
        } else {
            Toast.makeText(
                    getActivity(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
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

                Button button = itemView.findViewById(R.id.action_button);
                button.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        String strId = noteId.getText().toString();
                        int intId = Integer.parseInt(strId);
                        /*Toast toast = Toast.makeText(getContext(),
                                strId, Toast.LENGTH_SHORT);
                        toast.show();*/
                        mNoteViewModel.deleteNote(intId);
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
