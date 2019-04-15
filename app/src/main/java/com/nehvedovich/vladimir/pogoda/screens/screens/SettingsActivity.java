package com.nehvedovich.vladimir.pogoda.screens.screens;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.nehvedovich.vladimir.pogoda.R;

public class SettingsActivity extends AppCompatActivity {

    public CheckBox darkThemeCheckbox;

    private final String darkCBKey = "check";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initActionBar();

        darkThemeCheckbox = findViewById(R.id.darkThemeCheckBox);
        final ImageView darkBackground = findViewById(R.id.backgroundSettings);

        darkThemeCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    MainActivity.night = true;
                    darkBackground.setVisibility(View.VISIBLE);

                } else {
                    darkBackground.setVisibility(View.GONE);
                    MainActivity.night = false;
                }
            }
        });
    }

    private void initActionBar() {
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setHomeButtonEnabled(true);
        }
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        darkThemeCheckbox.setChecked(loadCheckBoxDarkTheme());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveCheckBoxDarkTheme(darkThemeCheckbox.isChecked());
    }

    private void saveCheckBoxDarkTheme(final boolean isChecked) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(darkCBKey, isChecked);
        editor.apply();
    }

    private boolean loadCheckBoxDarkTheme() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(darkCBKey, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


