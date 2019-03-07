package com.nehvedovich.vladimir.pogoda.screens.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        //отправляем ссылку о состоянии погоды в городе отображенном на экране
        if (item.getItemId() == R.id.share_link){
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

        //noinspection SimplifiableIfStatement
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    //меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_for_second_activity, menu);
        return true;
    }
}
