package com.nehvedovich.vladimir.pogoda.screens.screens;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nehvedovich.vladimir.pogoda.R;
import com.nehvedovich.vladimir.pogoda.screens.database.City;
import com.nehvedovich.vladimir.pogoda.screens.database.CityRepository;
import com.nehvedovich.vladimir.pogoda.screens.database.CityService;
import com.nehvedovich.vladimir.pogoda.screens.screens.fragments.CitiesFragment;
import com.nehvedovich.vladimir.pogoda.screens.screens.fragments.CityInfoFragment;
import com.nehvedovich.vladimir.pogoda.screens.utils.BackgroundService;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    private static final int PERMISSION_REQUEST_CODE = 10;
    private static final String FONT_FILENAME = "fonts/weather_icons.ttf";

    private final String pressureChBKey = "check_pressure";
    private final String sunriseSunsetChBKey = "check_sunrise_sunset";
    private final String darkThemeKey = "save_night";
    private final static String NOT_SUPPORTED_MESSAGE = "";  //Если сенсора не существует, то ничего не выводим
    public static boolean night;
    public static boolean coordPut = true;

    private TextView humidityIcon;
    private TextView temperatureIcon;
    private TextView temperatureLabel;
    private SensorManager mSensorManager;
    private Sensor mTemperature;
    private TextView humidityLabel;
    private Sensor mHumidity;
    public CheckBox pressure;
    public CheckBox sunriseSunset;
    public TextView coordination;
    public ProgressBar progressBar;
    String latitude;
    String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Проверим на пермиссии, и если их нет, запросим у пользователя
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Запросим координаты
            requestLocation();
        } else {
            // Пермиссии нет, будем запрашивать у пользователя
            requestLocationPermissions();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupNavigationDrawer(toolbar);
        initViews();
        startSensors();
        initWeatherFont();
        serviceInfoStart();

        FloatingActionButton fab = findViewById(R.id.locationButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putCoord();
            }
        });

        final SharedPreferences activityPrefs = getPreferences(Context.MODE_PRIVATE);
        readNightBackground(activityPrefs);
    }


    private void initWeatherFont() {
        Typeface weatherFont = Typeface.createFromAsset(getAssets(), FONT_FILENAME);
        humidityIcon.setTypeface(weatherFont);
        temperatureIcon.setTypeface(weatherFont);
    }

    private void initViews() {
        pressure = findViewById(R.id.checkBoxPressure);
        sunriseSunset = findViewById(R.id.checkBoxSunriseAndSunset);

        humidityIcon = findViewById(R.id.mIconHumidity);
        temperatureIcon = findViewById(R.id.mIconTemperature);

        temperatureLabel = findViewById(R.id.temperature_in);
        humidityLabel = findViewById(R.id.humidity_in);

        coordination = findViewById(R.id.textCoordination);
        progressBar = findViewById(R.id.progressBarCoord);
    }

    private void startSensors() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE); // requires API level 14.

        if (mTemperature == null) {
            temperatureLabel.setText(NOT_SUPPORTED_MESSAGE);
        }
        mHumidity = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        if (mHumidity == null) {
            humidityLabel.setText(NOT_SUPPORTED_MESSAGE);
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            return;
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            // Будем получать геоположение через каждые 3 секунд или каждые 2000 метров
            locationManager.requestLocationUpdates(provider, 3000, 2000, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // Широта
                    latitude = Double.toString(location.getLatitude());
                    // Долгота
                    longitude = Double.toString(location.getLongitude());
                    progressBar.setVisibility(View.GONE);
                    coordination.setVisibility(View.GONE);
                    if (coordPut) {
                        putCoord();
                        coordPut = false;
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            });

        }
    }

    private void putCoord() {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra(CityInfoFragment.COORD_LATITUDE, latitude);
        intent.putExtra(CityInfoFragment.COORD_LONGITUDE, longitude);

        intent.putExtra(CitiesFragment.CHECK_BOX_PRESSURE, pressure.isChecked());
        intent.putExtra(CitiesFragment.CHECK_BOX_SUNRISE_AND_SUNSET, sunriseSunset.isChecked());
        startActivity(intent);
    }

    // Запрос пермиссии для геолокации
    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                },
                PERMISSION_REQUEST_CODE);
    }

    // Это результат запроса у пользователя пермиссии
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Это та самая пермиссия, что мы запрашивали?
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 2 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                // Пермиссия дана
                requestLocation();
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                coordination.setVisibility(View.INVISIBLE);
            }
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

        pressure.setChecked(loadCheckBoxPressure());
        sunriseSunset.setChecked(loadCheckBoxSunriseSunset());

        ImageView imageNight = findViewById(R.id.landscapeNight);


        if (night) {
            imageNight.setVisibility(View.VISIBLE);
        } else {
            imageNight.setVisibility(View.GONE);
        }
    }

    private void serviceInfoStart() {
        Intent intent = new Intent(MainActivity.this, BackgroundService.class);
        startService(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        saveCheckBoxPressure(pressure.isChecked());
        saveCheckBoxSunriseSunset(sunriseSunset.isChecked());
        final SharedPreferences activityPrefs = getPreferences(Context.MODE_PRIVATE);
        saveNightBackground(activityPrefs);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final int type = event.sensor.getType();

        if (type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float ambient_temperature = event.values[0];
            temperatureIcon.setText(getString(R.string.temperature_icon));
            temperatureLabel.setText(String.format("%s ℃", String.format(Locale.US, "%.0f", ambient_temperature)));
        }
        if (type == Sensor.TYPE_RELATIVE_HUMIDITY) {
            float ambient_humidity = event.values[0];
            humidityIcon.setText(getString(R.string.humidity_icon));
            humidityLabel.setText(String.format("%s%%", String.format(Locale.US, "%.0f", ambient_humidity)));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    private void saveNightBackground(SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(darkThemeKey, night);
        editor.apply();
    }

    private void readNightBackground(SharedPreferences preferences) {
        night = preferences.getBoolean(darkThemeKey, false);
    }

    private void saveCheckBoxPressure(final boolean isChecked) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(pressureChBKey, isChecked);
        editor.apply();
    }

    private void saveCheckBoxSunriseSunset(final boolean isChecked) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(sunriseSunsetChBKey, isChecked);
        editor.apply();
    }

    private boolean loadCheckBoxPressure() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(pressureChBKey, false);
    }

    private boolean loadCheckBoxSunriseSunset() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(sunriseSunsetChBKey, false);
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
        coordPut = false;
        if (item.getItemId() == R.id.searchButton) showInputDialog();

        //noinspection SimplifiableIfStatement
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_history) {
            startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //показать диалог выбора города (ПОИСК)
    private void showInputDialog() {
        final AlertDialog.Builder chooseCity = new AlertDialog.Builder(this);
        chooseCity.setIcon(R.mipmap.ic_launcher);
        chooseCity.setTitle(R.string.enter_city_name);
        chooseCity.setMessage(R.string.enter_city_message);
        final TextInputEditText input = new TextInputEditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        chooseCity.setView(input);

        //запускаем активити с информациеей о погоде введенного с клавиатуры города
        chooseCity.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String city = Objects.requireNonNull(input.getText()).toString();
                if (city.length() > 1) {
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    intent.putExtra(CityInfoFragment.CITY_NAME_EXTRA, city);
                    intent.putExtra(CitiesFragment.CHECK_BOX_PRESSURE, pressure.isChecked());
                    intent.putExtra(CitiesFragment.CHECK_BOX_SUNRISE_AND_SUNSET, sunriseSunset.isChecked());
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.incorrect_city_name), Toast.LENGTH_SHORT).show();
                   showInputDialog();
                }
            }
        });
        input.setHint(getString(R.string.hint_city_example));
        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                input.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager inputMethodManager = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
        input.requestFocus();
        chooseCity.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();
        coordPut = false;
        if (id == R.id.nav_add_city) {
            showAddCity();

        } else if (id == R.id.nav_history) {
            startActivity(new Intent(MainActivity.this, HistoryActivity.class));

        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;


        } else if (id == R.id.about_the_app) {
            showAppDialog();
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


    private void showAddCity() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.enter_city_name);
        alert.setMessage(R.string.enter_city_message);
        final TextInputEditText input = new TextInputEditText(this);
        alert.setView(input);
        alert.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Editable editable = input.getText();
                if (editable != null) {
                    String value = editable.toString().trim();
                    if (value.length() > 1) {
                        Activity activity = MainActivity.this;
                        CityRepository.getInstance().add(new City(null, value));
                        Intent intent = new Intent(activity, CityService.class);
                        activity.startService(intent);
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.incorrect_city_name), Toast.LENGTH_SHORT).show();
                        showAddCity();
                    }
                }
            }
        });
        input.setHint(getString(R.string.hint_city_example));
        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                input.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager inputMethodManager = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
        input.requestFocus();
        alert.show();
    }


    //показываем окно с информацией о разработчике
    private void showAppDialog() {
        AlertDialog.Builder byAuthor = new AlertDialog.Builder(this);
        byAuthor.setIcon(R.mipmap.ic_launcher);
        byAuthor.setTitle(R.string.app_name);
        byAuthor.setMessage(R.string.app_version);
        byAuthor.show();
    }
}