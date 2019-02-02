package com.nehvedovich.vladimir.pogoda.screens.screens;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nehvedovich.vladimir.pogoda.R;

public class SecondActivity extends AppCompatActivity {

    public static final String CITY_NAME_EXSTRA = "cityLookingFor";

//    public static  final Boolean WIND = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            TextView cityName = findViewById(R.id.cityNameInfo);
            cityName.setText(bundle.getString(CITY_NAME_EXSTRA));
        }


//        ViewUtilities.makeToast(getApplicationContext(), "onCreate");

        ImageButton toHome = findViewById(R.id.toHome);
        toHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


//Обработка CheckBox Wind
//        final TextView textWind = (TextView) findViewById(R.id.textWind);
//        final TextView textDirection = (TextView) findViewById(R.id.directionWind);
//
//        if (WIND == true) {
//            textWind.setVisibility(View.VISIBLE);
//            textDirection.setVisibility(View.VISIBLE);
//        } else {
//            textWind.setVisibility(View.GONE);
//            textDirection.setVisibility(View.GONE);
//        }
    }
}
