package com.nehvedovich.vladimir.pogoda.screens.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.nehvedovich.vladimir.pogoda.R;

public class SettingsActivity extends AppCompatActivity {

    private  CheckBox darkThemeCheckbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageButton toHome = findViewById(R.id.toHome);
        toHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        darkThemeCheckbox = (CheckBox)findViewById(R.id.darkThemeCheckBox);

    }
}
