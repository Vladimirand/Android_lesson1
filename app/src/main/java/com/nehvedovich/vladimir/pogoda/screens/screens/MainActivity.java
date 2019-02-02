package com.nehvedovich.vladimir.pogoda.screens.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.nehvedovich.vladimir.pogoda.R;


public class MainActivity extends AppCompatActivity {

    public CheckBox checkBoxWind;


    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            Button btn = findViewById(v.getId());
            String cityName = (String) btn.getText();
            if (cityName != null) {
                intent.putExtra(SecondActivity.CITY_NAME_EXSTRA, cityName);
            }
            startActivity(intent);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.to_city_1);
        Button button2 = findViewById(R.id.to_city_2);
        Button button3 = findViewById(R.id.to_city_3);
        Button button4 = findViewById(R.id.to_city_4);
        button.setOnClickListener(onClickListener);
        button2.setOnClickListener(onClickListener);
        button3.setOnClickListener(onClickListener);
        button4.setOnClickListener(onClickListener);


        ImageButton settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });


//Обработка CheckBox Wind

        checkBoxWind = (CheckBox) findViewById(R.id.checkBoxWind);

        checkBoxWind.findViewById(R.id.checkBoxWind);
//        checkBoxWind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                Intent intent1 = new Intent(MainActivity.this, SecondActivity.class);
//
//                boolean check1;
//                if (isChecked) {
//
//                    check1 = true;
//                } else {
//                    check1 = false;
//                }
////                intent1.putExtra(SecondActivity.WIND, true);
//            }
//        });

    }

}
