package com.nehvedovich.vladimir.pogoda.screens.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nehvedovich.vladimir.pogoda.R;
import com.nehvedovich.vladimir.pogoda.screens.utils.ViewUtilities;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtilities.makeToast(getApplicationContext(), "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        ViewUtilities.makeToast(getApplicationContext(), "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewUtilities.makeToast(getApplicationContext(), "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewUtilities.makeToast(getApplicationContext(), "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        ViewUtilities.makeToast(getApplicationContext(), "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewUtilities.makeToast(getApplicationContext(), "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ViewUtilities.makeToast(getApplicationContext(), "onRestart");
    }
}
