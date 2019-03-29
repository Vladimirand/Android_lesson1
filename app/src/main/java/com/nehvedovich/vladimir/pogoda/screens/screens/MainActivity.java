package com.nehvedovich.vladimir.pogoda.screens.screens;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.nehvedovich.vladimir.pogoda.R;
import com.nehvedovich.vladimir.pogoda.screens.screens.fragments.CitiesFragment;
import com.nehvedovich.vladimir.pogoda.screens.screens.fragments.CityInfoFragment;

public class MainActivity extends AppCompatActivity {

    public static boolean NIGHT;

//    public CheckBox wind;
//    public CheckBox humidity;
    public CheckBox pressure;
    public CheckBox feelsLike;
    public CheckBox sunriseSunset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        wind = (CheckBox) findViewById(R.id.checkBoxWind);
//        humidity = (CheckBox) findViewById(R.id.checkBoxHumidity);
        pressure = (CheckBox) findViewById(R.id.checkBoxPressure);
        feelsLike = (CheckBox) findViewById(R.id.checkBoxFeelsLike);
        sunriseSunset = (CheckBox) findViewById(R.id.checkBoxSunriseAndSunset);
    }

    @Override
    public void onPause() {
        super.onPause();
//        save1(wind.isChecked());
//        save2(humidity.isChecked());
        save3(pressure.isChecked());
        save4(feelsLike.isChecked());
        save5(sunriseSunset.isChecked());
    }

    @Override
    public void onResume() {
        super.onResume();

//        wind.setChecked(load1());
//        humidity.setChecked(load2());
        pressure.setChecked(load3());
        feelsLike.setChecked(load4());
        sunriseSunset.setChecked(load5());

        ImageView imageNight = (ImageView) findViewById(R.id.landscapeNight);

        if (NIGHT == true) {
            imageNight.setVisibility(View.VISIBLE);
        } else {
            imageNight.setVisibility(View.GONE);
        }
    }

    private void save1(final boolean isChecked) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("check1", isChecked);
        editor.commit();
    }

    private void save2(final boolean isChecked) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("check2", isChecked);
        editor.commit();
    }

    private void save3(final boolean isChecked) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("check3", isChecked);
        editor.commit();
    }

    private void save4(final boolean isChecked) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("check4", isChecked);
        editor.commit();
    }
    private void save5(final boolean isChecked) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("check5", isChecked);
        editor.commit();
    }

    private boolean load1() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("check1", false);
    }

    private boolean load2() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("check2", false);
    }

    private boolean load3() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("check3", false);
    }

    private boolean load4() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("check4", false);
    }
    private boolean load5() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("check5", false);
    }

    //меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


        //обработка нажатия пункта меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (item.getItemId() == R.id.searchButton) showInputDialog();

        //noinspection SimplifiableIfStatement
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //показать диалог выбора города (ПОИСК)
    private void showInputDialog() {
        AlertDialog.Builder chooseCity = new AlertDialog.Builder(this);
        chooseCity.setIcon(R.mipmap.ic_launcher);
        chooseCity.setTitle(R.string.choose_city);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        chooseCity.setView(input);

        //запускае активити с информациеей о погоде введенного с клавиатуры города
        chooseCity.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String city = input.getText().toString();
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                if (city != null) {
                    intent.putExtra(CityInfoFragment.CITY_NAME_EXSTRA, city);
                }
//                intent.putExtra(CitiesFragment.CHECK_BOX_WIND, wind.isChecked());
//                intent.putExtra(CitiesFragment.CHECK_BOX_HUMIDITY, humidity.isChecked());
                intent.putExtra(CitiesFragment.CHECK_BOX_PRESSURE, pressure.isChecked());
                intent.putExtra(CitiesFragment.CHECK_BOX_FEEL_LIKE, feelsLike.isChecked());
                intent.putExtra(CitiesFragment.CHECK_BOX_SUNRISE_AND_SUNSET, sunriseSunset.isChecked());
                startActivity(intent);
            }
        });
        chooseCity.show();
    }
}