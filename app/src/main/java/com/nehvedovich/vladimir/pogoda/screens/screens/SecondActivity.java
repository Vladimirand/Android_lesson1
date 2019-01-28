package com.nehvedovich.vladimir.pogoda.screens.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.nehvedovich.vladimir.pogoda.R;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
//        ViewUtilities.makeToast(getApplicationContext(), "onCreate");
        ImageButton toHome = findViewById(R.id.toHome);
        toHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

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
//    }
}
