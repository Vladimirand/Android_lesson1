package com.nehvedovich.vladimir.pogoda.screens.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nehvedovich.vladimir.pogoda.R;


public class SecondActivity extends AppCompatActivity {

    public static final String CITY_NAME_EXSTRA = "cityLookingFor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        if (savedInstanceState == null) {
            CityInfoFragment details = new CityInfoFragment();
            details.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
        }

        ImageButton toHome = findViewById(R.id.toHome);
        ImageButton share = findViewById(R.id.imageButtonShare);

        toHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);

                TextView city = findViewById(R.id.cityNameInfo);
                String cityName = (String) city.getText();
                String t = ("http://yandex.ru/pogoda/" + cityName);
                intent.putExtra(Intent.EXTRA_TEXT, t);
                intent.setType("text/plain");

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(SecondActivity.this, "\n" +
                            "Application does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
