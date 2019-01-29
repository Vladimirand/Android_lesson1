package com.nehvedovich.vladimir.pogoda.screens.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.nehvedovich.vladimir.pogoda.R;


public class MainActivity extends AppCompatActivity {

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
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

    }
}
