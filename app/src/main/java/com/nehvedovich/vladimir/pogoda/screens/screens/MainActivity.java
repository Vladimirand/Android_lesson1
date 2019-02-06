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

    public static final String CHECK_BOX_WIND = "checkBoxWind";
    public static final String CHECK_BOX_HUMIDITY = "checkBoxHumidity";
    public static final String CHECK_BOX_PRESSURE = "checkBoxPressure";


    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            Button btn = findViewById(v.getId());
            String cityName = (String) btn.getText();

            if (cityName != null) {
                intent.putExtra(SecondActivity.CITY_NAME_EXSTRA, cityName);
            }
            intent.putExtra(CHECK_BOX_WIND, getCheckBoxWind());
            intent.putExtra(CHECK_BOX_HUMIDITY, getCheckBoxHumidity());
            intent.putExtra(CHECK_BOX_PRESSURE, getCheckBoxPressure());
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

        setTheme(R.style.SettingsTheme2);

        ImageButton settings = findViewById(R.id.settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
    }

    public Boolean getCheckBoxWind() {
        CheckBox wind = findViewById(R.id.checkBoxWind);
        return wind.isChecked();
    }

    public Boolean getCheckBoxHumidity() {
        CheckBox humidity = findViewById(R.id.checkBoxHumidity);
        return humidity.isChecked();
    }

    public Boolean getCheckBoxPressure() {
        CheckBox pressure = findViewById(R.id.checkBoxPressure);
        return pressure.isChecked();
    }

}
