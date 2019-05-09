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
    public CheckBox minimalisticIconsChB;

    private final String darkCBKey = "check";
    private final String minimalCBKey = "checkMinimalistic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initActionBar();

        darkThemeCheckbox = findViewById(R.id.darkThemeCheckBox);
        minimalisticIconsChB = findViewById(R.id.minimalisticIcon);
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

        minimalisticIconsChB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.minimalisticIcons = isChecked;
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
        minimalisticIconsChB.setChecked(loadCheckBoxMinimalisticIcon());
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
        saveCheckBoxMinimalisticIcon(minimalisticIconsChB.isChecked());
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

    private void saveCheckBoxMinimalisticIcon(final boolean isChecked) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(minimalCBKey, isChecked);
        editor.apply();
    }

    private boolean loadCheckBoxMinimalisticIcon() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(minimalCBKey, false);
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


