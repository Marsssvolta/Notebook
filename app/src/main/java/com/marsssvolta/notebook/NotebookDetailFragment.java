package com.marsssvolta.notebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class NotebookDetailFragment extends Fragment {

    public static final String EXTRA_REPLY = "com.marsssvolta.notebook.REPLY";

    private EditText mEditNoteView;
    private Button mButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        mEditNoteView = view.findViewById(R.id.edit_note);
        mButton = view.findViewById(R.id.button_save);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditNoteView.getText())) {
                    getActivity().setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String note = mEditNoteView.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY, note);
                    getActivity().setResult(RESULT_OK, replyIntent);
                }
                getActivity().finish();
            }
        });
    }
}
