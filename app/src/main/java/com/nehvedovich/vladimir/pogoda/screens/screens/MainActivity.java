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
//        ViewUtilities.makeToast(getApplicationContext(), "onCreate");

        Button button = findViewById(R.id.to_city1);
        Button button2 = findViewById(R.id.to_city2);
        Button button3 = findViewById(R.id.to_city3);
        Button button4 = findViewById(R.id.to_city4);

        button.setOnClickListener(onClickListener);
        button2.setOnClickListener(onClickListener);
        button3.setOnClickListener(onClickListener);
        button4.setOnClickListener(onClickListener);

//    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        ViewUtilities.makeToast(getApplicationContext(), "onStart");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        ViewUtilities.makeToast(getApplicationContext(), "onResume");
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        ViewUtilities.makeToast(getApplicationContext(), "onPause");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        ViewUtilities.makeToast(getApplicationContext(), "onStop");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        ViewUtilities.makeToast(getApplicationContext(), "onDestroy");
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        ViewUtilities.makeToast(getApplicationContext(), "onRestart");
    }
}
