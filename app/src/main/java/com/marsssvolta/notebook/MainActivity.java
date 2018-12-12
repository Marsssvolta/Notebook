package com.marsssvolta.notebook;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel mNoteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        // Добавление фрагмента в FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.mainFragmentContainer);

        // Создание нового фрагмента, если предыдущий не был сохранён
        if (fragment == null) {
            fragment = new NotebookListFragment();
            // Транзакция фрагмента
            fm.beginTransaction().add(R.id.mainFragmentContainer, fragment).commit();
        }
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
                mNoteViewModel.deleteAll();
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
