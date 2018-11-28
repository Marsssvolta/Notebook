package com.marsssvolta.notebook;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
