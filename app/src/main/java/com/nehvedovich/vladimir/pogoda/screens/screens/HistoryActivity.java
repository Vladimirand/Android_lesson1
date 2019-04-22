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
import com.nehvedovich.vladimir.pogoda.screens.database.Weather;
import com.nehvedovich.vladimir.pogoda.screens.database.WeatherAdapter;
import com.nehvedovich.vladimir.pogoda.screens.database.WeatherDataReader;
import com.nehvedovich.vladimir.pogoda.screens.database.WeatherDataSource;

public class HistoryActivity extends AppCompatActivity {

    private WeatherDataSource notesDataSource;     // Источник данных
    private WeatherDataReader weatherDataReader;   // Читатель данных
    private WeatherAdapter adapter;                // Адаптер для RecyclerView
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
        adapter = new WeatherAdapter(weatherDataReader);
        adapter.setOnMenuItemClickListener(new WeatherAdapter.OnMenuItemClickListener() {
            @Override
            public void onItemDeleteClick(Weather weather) {
                deleteElement(weather);
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
        notesDataSource = new WeatherDataSource(getApplicationContext());
        notesDataSource.open();
        weatherDataReader = notesDataSource.getWeatherDataReader();
    }

    private void clearList() {
        notesDataSource.deleteAll();
        dataUpdated();
    }


    private void deleteElement(Weather weather) {
        notesDataSource.deleteNote(weather);
        dataUpdated();
    }

    private void dataUpdated() {
        weatherDataReader.Refresh();
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
