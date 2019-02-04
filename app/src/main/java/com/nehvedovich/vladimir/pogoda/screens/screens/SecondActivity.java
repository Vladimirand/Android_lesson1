package com.nehvedovich.vladimir.pogoda.screens.screens;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nehvedovich.vladimir.pogoda.R;

public class SecondActivity extends AppCompatActivity {

    public static final String CITY_NAME_EXSTRA = "cityLookingFor";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Bundle bundle = getIntent().getExtras();
        boolean wind = false;
        boolean humidity = false;
        boolean pressure = false;
        if (bundle != null) {
            TextView cityName = findViewById(R.id.cityNameInfo);
            cityName.setText(bundle.getString(CITY_NAME_EXSTRA));

            wind = bundle.getBoolean(MainActivity.CHECK_BOX_WIND);
            humidity = bundle.getBoolean(MainActivity.CHECK_BOX_HUMIDITY);
            pressure = bundle.getBoolean(MainActivity.CHECK_BOX_PRESSURE);
        }

//      ViewUtilities.makeToast(getApplicationContext(), "onCreate");

        ImageButton toHome = findViewById(R.id.toHome);
        toHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        //Обработка CheckBox Wind
        TextView textWind = (TextView) findViewById(R.id.textWind);
        TextView textDirection = (TextView) findViewById(R.id.directionWind);

        if (wind == true) {
            textWind.setVisibility(View.VISIBLE);
            textDirection.setVisibility(View.VISIBLE);
        } else {
            textWind.setVisibility(View.GONE);
            textDirection.setVisibility(View.GONE);
        }

        //Обработка CheckBox humidity
        TextView textHumidity = (TextView) findViewById(R.id.textHumidity);

        if (humidity == true) {
            textHumidity.setVisibility(View.VISIBLE);
        } else {
            textHumidity.setVisibility(View.GONE);
        }

        //Обработка CheckBox Pressure
        TextView textPressure = (TextView) findViewById(R.id.textPressure);

        if (pressure == true) {
            textPressure.setVisibility(View.VISIBLE);
        } else {
            textPressure.setVisibility(View.GONE);
        }
    }
}
