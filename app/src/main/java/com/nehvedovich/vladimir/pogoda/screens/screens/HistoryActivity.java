package com.nehvedovich.vladimir.pogoda.screens.screens;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nehvedovich.vladimir.pogoda.R;
import com.nehvedovich.vladimir.pogoda.screens.database.Note;
import com.nehvedovich.vladimir.pogoda.screens.database.NoteAdapter;
import com.nehvedovich.vladimir.pogoda.screens.database.NoteDataReader;
import com.nehvedovich.vladimir.pogoda.screens.database.NoteDataSource;

public class HistoryActivity extends AppCompatActivity {

    private NoteDataSource notesDataSource;     // Источник данных
    private NoteDataReader noteDataReader;      // Читатель данных
    private NoteAdapter adapter;                // Адаптер для RecyclerView
    TextView noItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);

        noItems = findViewById(R.id.no_items);

        initDataSource();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NoteAdapter(noteDataReader);
        adapter.setOnMenuItemClickListener(new NoteAdapter.OnMenuItemClickListener() {
            @Override
            public void onItemDeleteClick(Note note) {
                deleteElement(note);
            }
        });

        recyclerView.setAdapter(adapter);

        if (adapter.getItemCount() == 0) {
            noItems.setVisibility(View.VISIBLE);
        } else {
            noItems.setVisibility(View.GONE);
        }
    }

    private void initDataSource() {
        notesDataSource = new NoteDataSource(getApplicationContext());
        notesDataSource.open();
        noteDataReader = notesDataSource.getNoteDataReader();
    }

    private void clearList() {
        notesDataSource.deleteAll();
        dataUpdated();
    }


    private void deleteElement(Note note) {
        notesDataSource.deleteNote(note);
        dataUpdated();
    }

    private void dataUpdated() {
        noteDataReader.Refresh();
        adapter.notifyDataSetChanged();

    }

    //меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.clear_all) {
            clearList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
