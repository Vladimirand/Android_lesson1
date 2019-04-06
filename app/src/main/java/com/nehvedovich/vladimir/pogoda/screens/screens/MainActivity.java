package com.nehvedovich.vladimir.pogoda.screens.screens;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.nehvedovich.vladimir.pogoda.R;
import com.nehvedovich.vladimir.pogoda.screens.screens.fragments.CitiesFragment;
import com.nehvedovich.vladimir.pogoda.screens.screens.fragments.CityInfoFragment;
import com.nehvedovich.vladimir.pogoda.screens.utils.BackgroundService;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    static final int GALLERY_REQUEST = 1;
    private static final String FONT_FILENAME = "fonts/weathericons.ttf";

    private Typeface weatherFont;
    private TextView humidityIcon;
    private TextView temperatureIcon;

    private TextView temperaturelabel;
    private SensorManager mSensorManager;
    private Sensor mTemperature;
    private TextView humiditylabel;
    private Sensor mHumidity;
    private final static String NOT_SUPPORTED_MESSAGE = "";  //Если сенсора не существует, то ничего не выводим


    public static boolean night;

    public CheckBox pressure;
    public CheckBox feelsLike;
    public CheckBox sunriseSunset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupNavigationDrawer(toolbar);
        initViews();
        startSensors();

        weatherFont = Typeface.createFromAsset(getAssets(), FONT_FILENAME);
        humidityIcon.setTypeface(weatherFont);
        temperatureIcon.setTypeface(weatherFont);


    }
    private void initViews() {
        pressure = findViewById(R.id.checkBoxPressure);
        feelsLike = findViewById(R.id.checkBoxFeelsLike);
        sunriseSunset = findViewById(R.id.checkBoxSunriseAndSunset);

        humidityIcon = findViewById(R.id.mIconHumidity);
        temperatureIcon = findViewById(R.id.mIconTemperature);

        temperaturelabel = findViewById(R.id.temperature_in);
        humiditylabel = findViewById(R.id.humidity_in);
    }

    private void startSensors() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE); // requires API level 14.
        if (mTemperature == null) {
            temperaturelabel.setText(NOT_SUPPORTED_MESSAGE);
        }
        mHumidity = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if (mHumidity == null) {
            humiditylabel.setText(NOT_SUPPORTED_MESSAGE);
        }
    }

    private void setupNavigationDrawer(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSensorManager.registerListener(this, mTemperature, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mHumidity, SensorManager.SENSOR_DELAY_NORMAL);
        serviceInfoStart();
    }

    private void serviceInfoStart() {
        Intent intent = new Intent(MainActivity.this, BackgroundService.class);
        startService(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mTemperature, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mHumidity, SensorManager.SENSOR_DELAY_NORMAL);

        pressure.setChecked(loadCheckBoxPressure());
        feelsLike.setChecked(loadCheckBoxFeelsLike());
        sunriseSunset.setChecked(loadCheckBoxSunriseSunset());

        ImageView imageNight = findViewById(R.id.landscapeNight);

        if (night) {
            imageNight.setVisibility(View.VISIBLE);
        } else {
            imageNight.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);

        saveCheckBoxPressure(pressure.isChecked());
        saveCheckBoxFeelsLike(feelsLike.isChecked());
        saveCheckBoxSunriseSunset(sunriseSunset.isChecked());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float ambient_temperature = event.values[0];
        temperatureIcon.setText(getString(R.string.temperature_icon));
        temperaturelabel.setText(String.format("%.0f", ambient_temperature) + " ℃");

        float ambient_humidity = event.values[0];
        humidityIcon.setText(getString(R.string.humidity_icon));
        humiditylabel.setText(String.format("%.0f", ambient_humidity) + "%");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    private void saveCheckBoxPressure(final boolean isChecked) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("checkPressure", isChecked);
        editor.commit();
    }

    private void saveCheckBoxFeelsLike(final boolean isChecked) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("checkFeelsLike", isChecked);
        editor.commit();
    }

    private void saveCheckBoxSunriseSunset(final boolean isChecked) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("checkSunriseSunset", isChecked);
        editor.commit();
    }

    private boolean loadCheckBoxPressure() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("checkPressure", false);
    }

    private boolean loadCheckBoxFeelsLike() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("checkFeelsLike", false);
    }

    private boolean loadCheckBoxSunriseSunset() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("checkSunriseSunset", false);
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
                intent.putExtra(CityInfoFragment.CITY_NAME_EXSTRA, city);
                intent.putExtra(CitiesFragment.CHECK_BOX_PRESSURE, pressure.isChecked());
                intent.putExtra(CitiesFragment.CHECK_BOX_FEEL_LIKE, feelsLike.isChecked());
                intent.putExtra(CitiesFragment.CHECK_BOX_SUNRISE_AND_SUNSET, sunriseSunset.isChecked());
                startActivity(intent);
            }
        });
        chooseCity.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.nav_avatar) {
            //пользователь может выбрать аватарку из галерии
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
        } else if (id == R.id.nav_name) {
            showNameDialog();

        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;


        } else if (id == R.id.about_the_developer) {
            showDeveloperDialog();
        } else if (id == R.id.feedback_form) {
            //пользователь может отправить сообщение в техподдержку по email
            Intent i = new Intent(Intent.ACTION_SENDTO);
            i.setData(Uri.parse("mailto:" + getString(R.string.support_email)));
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.message_subject));
            i.putExtra(Intent.EXTRA_TEXT, getString(R.string.problem_message));
            try {
                startActivity(Intent.createChooser(i, getString(R.string.send_mail_title)));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, "\n" +
                        getString(R.string.application_absent), Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // метод для загрузки пользовательской аватарки из галереи
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Bitmap bitmap = null;
        ImageView imageView = findViewById(R.id.avatarView);

        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageBitmap(bitmap);
                }
        }
    }

    //показываем окно с информацией о разработчике
    private void showDeveloperDialog() {
        AlertDialog.Builder byAuthotor = new AlertDialog.Builder(this);
        byAuthotor.setIcon(R.mipmap.ic_launcher);
        byAuthotor.setTitle(R.string.developer_name);

        byAuthotor.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        byAuthotor.show();
    }


    private void showNameDialog() {
        AlertDialog.Builder name = new AlertDialog.Builder(this);
        name.setTitle(R.string.nav_header_title);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        name.setView(input);

        name.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                TextView n = findViewById(R.id.user_name);
                n.setText(name);
            }
        });
        name.show();
    }
}