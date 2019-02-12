package com.nehvedovich.vladimir.pogoda.screens.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nehvedovich.vladimir.pogoda.R;
import com.nehvedovich.vladimir.pogoda.screens.screens.fragments.CityInfoFragment;

public class SecondActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        if (savedInstanceState == null) {
            CityInfoFragment details = new CityInfoFragment();
            details.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
        }

        Button info = findViewById(R.id.infoTemperature);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this, InfoActivity.class));
            }
        });

        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);

        Button details = findViewById(R.id.moreInformation);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                TextView city = findViewById(R.id.cityNameInfo);
                String cityName = (String) city.getText();
                Uri uri = Uri.parse("http://yandex.ru/pogoda/" + cityName);
                intent.setData(uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(SecondActivity.this, "\n" +
                            "Application does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageButton share = findViewById(R.id.imageButtonShare);
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

        ImageButton toHome = findViewById(R.id.toHome);
        toHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
