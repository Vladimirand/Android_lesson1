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

    public static final String CHECK_BOX_DARK = "dark";
//    public static boolean NIGHT;

    public CheckBox darkThemeCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);

        darkThemeCheckbox = (CheckBox) findViewById(R.id.darkThemeCheckBox);
        final ImageView darkBackground = (ImageView) findViewById(R.id.backgroundSettings);

        darkThemeCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    MainActivity.NIGHT = true;
//                    getPreferences(MODE_PRIVATE).edit().putBoolean("night", true).apply();
                    darkBackground.setVisibility(View.VISIBLE);

                } else {
                    darkBackground.setVisibility(View.GONE);
                    MainActivity.NIGHT = false;
                }
//                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
//                intent.putExtra(CHECK_BOX_DARK, darkThemeCheckbox.isChecked());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        save(darkThemeCheckbox.isChecked());
    }

    @Override
    public void onResume() {
        super.onResume();
        darkThemeCheckbox.setChecked(load());
    }

    private void save(final boolean isChecked) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("check", isChecked);
        editor.commit();
    }

    private boolean load() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("check", false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onBackPressed() {
//                SharedPreferences sp = getSharedPreferences(CHECK_BOX_DARK, 0);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString(CHECK_BOX_DARK, "dark");
//        editor.commit();
//    }

//    @Overrid
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
//        intent.putExtra(CHECK_BOX_DARK, darkThemeCheckbox.isChecked());
//    }
}


