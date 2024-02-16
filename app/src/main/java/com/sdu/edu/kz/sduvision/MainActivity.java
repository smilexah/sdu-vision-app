package com.sdu.edu.kz.sduvision;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sdu.edu.kz.sduvision.toolbar.fragment_account;
import com.sdu.edu.kz.sduvision.toolbar.fragment_main;
import com.sdu.edu.kz.sduvision.toolbar.fragment_notes;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.main);
    }

    fragment_notes notesFragment = new fragment_notes();
    fragment_main mainFragment = new fragment_main();
    fragment_account accountFragment = new fragment_account();

    private static final int NOTES_ITEM_ID = R.id.notes;
    private static final int MAIN_ITEM_ID = R.id.main;
    private static final int ACCOUNT_ITEM_ID = R.id.account;

    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == NOTES_ITEM_ID) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, notesFragment)
                    .commit();
            return true;
        } else if (itemId == MAIN_ITEM_ID) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, mainFragment)
                    .commit();
            return true;
        } else if (itemId == ACCOUNT_ITEM_ID) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, accountFragment)
                    .commit();
            return true;
        }

        return false;
    }
}
