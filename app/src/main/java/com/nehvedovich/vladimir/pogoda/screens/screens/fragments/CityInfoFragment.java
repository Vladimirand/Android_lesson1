package com.nehvedovich.vladimir.pogoda.screens.screens.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nehvedovich.vladimir.pogoda.R;
import com.nehvedovich.vladimir.pogoda.screens.utils.ForecastLoader;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class CityInfoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String CITY_NAME_EXSTRA = "cityLookingFor";
    private static final String FONT_FILENAME = "fonts/weathericons.ttf";

    private final String msgException = "One or more fields not found in the JSON data";

    private SwipeRefreshLayout swipeRefreshLayout;

    private final Handler handler = new Handler();
    private Typeface weatherFont;
    private TextView cityTextView;
    private TextView sunriseTextView;
    private TextView sunsetTextView;
    private TextView weatherConditions;
    private TextView weatherExpected;
    private TextView currentTemperatureTextView;
    private TextView humidityTextView;
    private TextView humidityIcon;
    private TextView windTextView;
    private TextView windIcon;
    private TextView directionWindIcon;
    private TextView pressureTextView;
    private TextView pressureIcon;
    private TextView weatherIcon;
    private TextView updatedTextView;
    private ProgressBar progressBar;

    String currentCityName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_info, container, false);

        Bundle bundle = getArguments();

        boolean pressure = false;
        boolean feelLike = false;
        boolean sunriseSunset = false;

        if (bundle != null) {
            TextView cityName = layout.findViewById(R.id.cityNameInfo);
            cityName.setText(bundle.getString(CITY_NAME_EXSTRA));
            currentCityName = (bundle.getString(CITY_NAME_EXSTRA));

            pressure = bundle.getBoolean(CitiesFragment.CHECK_BOX_PRESSURE);
            feelLike = bundle.getBoolean(CitiesFragment.CHECK_BOX_FEEL_LIKE);
            sunriseSunset = bundle.getBoolean(CitiesFragment.CHECK_BOX_SUNRISE_AND_SUNSET);
        }

        //загружаем данные погоды
        updateWeatherData(currentCityName, getString(R.string.location));

        swipeRefreshLayout = layout.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        //Обработка CheckBox SunriseAndSunset
        TextView textSunrise = layout.findViewById(R.id.textSunrise);
        TextView textSunset = layout.findViewById(R.id.textSunset);
        if (sunriseSunset) {
            textSunrise.setVisibility(View.VISIBLE);
            textSunset.setVisibility(View.VISIBLE);
        } else {
            textSunrise.setVisibility(View.GONE);
            textSunset.setVisibility(View.GONE);
        }

        //Обработка CheckBox Pressure
        View textPressure = layout.findViewById(R.id.viewPressure);
        if (pressure) {
            textPressure.setVisibility(View.VISIBLE);
        } else {
            textPressure.setVisibility(View.GONE);
        }

        //Обработка CheckBox FeelLike
        TextView textFeelLike = layout.findViewById(R.id.textFeelsLike);
        TextView textFeelLikeT = layout.findViewById(R.id.textFeelsLikeT);
        if (feelLike) {
            textFeelLike.setVisibility(View.VISIBLE);
            textFeelLikeT.setVisibility(View.VISIBLE);
        } else {
            textFeelLike.setVisibility(View.GONE);
            textFeelLikeT.setVisibility(View.GONE);
        }
        cityTextView = layout.findViewById(R.id.cityName);

        currentTemperatureTextView = layout.findViewById(R.id.textTemperature);
        weatherIcon = layout.findViewById(R.id.weather_icon);
        weatherFont = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), FONT_FILENAME);
        weatherIcon.setTypeface(weatherFont);
        updatedTextView = layout.findViewById(R.id.data);

        weatherConditions = layout.findViewById(R.id.weather_conditions);
        weatherExpected = layout.findViewById(R.id.weather_expected);
        progressBar = layout.findViewById(R.id.progressBar);
        sunriseTextView = textSunrise;
        sunsetTextView = textSunset;
        humidityTextView = layout.findViewById(R.id.textHumidity);
        humidityIcon = layout.findViewById(R.id.iconHumidity);
        humidityIcon.setTypeface(weatherFont);

        windTextView = layout.findViewById(R.id.textWind);
        windIcon = layout.findViewById(R.id.iconWind);
        windIcon.setTypeface(weatherFont);
        directionWindIcon = layout.findViewById(R.id.directionWind);
        directionWindIcon.setTypeface(weatherFont);

        pressureTextView = layout.findViewById(R.id.textPressure);
        pressureIcon = layout.findViewById(R.id.iconPressure);
        pressureIcon.setTypeface(weatherFont);
        return layout;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        refreshList();
    }

    private void refreshList() {
        updateWeatherData(currentCityName, getString(R.string.location));
        swipeRefreshLayout.setRefreshing(false);
    }

    //Обновление/загрузка погодных данных
    private void updateWeatherData(final String city, final String location) {
        new Thread() {//Отдельный поток для запроса на сервер
            public void run() {
                final JSONObject json = ForecastLoader.getJsonData(city, location);
                // Вызов методов напрямую может вызвать runtime error
                if (json == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getContext(), getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    //Обработка загруженных данных и обновление UI
    private void renderWeather(JSONObject json) {
        Log.d("Log", "json " + json.toString());

        try {
            cityTextView.setText(String.format("%s, %s", json.getString("name"), json.getJSONObject("sys").getString("country")));

            JSONObject main = json.getJSONObject("main");

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            weatherConditions.setText(details.getString("description").toUpperCase());
            setWeatherIcon(details.getInt("id"), json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

            try {
                JSONObject detailsExpected = json.getJSONArray("weather").getJSONObject(1);
                weatherExpected.setText(String.format("(%s)", detailsExpected.getString("description")));
                weatherExpected.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                Log.d("Log", msgException + "(in 'description')");//FIXME Обработка ошибки
            }

            try {
                JSONObject wind = json.getJSONObject("wind");
                windIcon.setText(getString(R.string.wind_icon));
                windTextView.setText(String.format("%s %s", wind.getString("speed"), getString(R.string.wind_speed_m_s)));

                //получаем направление ветра

                setWindDirecrionIcon(wind.getInt("deg"));
            } catch (Exception e) {
                Log.d("Log", msgException + "(in 'wind')");//FIXME Обработка ошибки
            }
            humidityIcon.setText(getString(R.string.humidity_icon));
            humidityTextView.setText(String.format("%s%%", main.getString("humidity")));

            pressureIcon.setText(getString(R.string.pressure_icon));

//          pressureTextView.setText(main.getString("pressure") + "hPa");

            //переводим значение hPa в мм.рт.ст
            String s = (main.getString("pressure"));
            double i = Double.valueOf(s) * 0.750062;
            String si = String.format("%.0f", i); //отображаем только значение до запятой
            pressureTextView.setText(String.format("%s %s", si, getString(R.string.pressure_mmHg)));


            currentTemperatureTextView.setText(String.format("%.0f", main.getDouble("temp")) + " ℃");

            //отображаем время рассвета
//            DateFormat sunrise = DateFormat.getDateTimeInstance();  //отображение даты полностью
            DateFormat df; //отображение только времени часы/минуты
            df = new SimpleDateFormat("HH:mm");
            String sunriseTime = df.format(new Date(json.getJSONObject("sys").getLong("sunrise") * 1000));

            sunriseTextView.setText(String.format("%s:  %s", getString(R.string.sunrise), sunriseTime));

            //отображаем время заката
            String sunsetTime = df.format(new Date(json.getJSONObject("sys").getLong("sunset") * 1000));
            sunsetTextView.setText(String.format("%s:  %s", getString(R.string.sunset), sunsetTime));

            Date currentTime = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//            String updatedOn = dateFormat.format(new Date(json.getLong("dt") * 1000)); //время последнего обновления полученных данных
            String updatedOn = dateFormat.format(new Date(currentTime.getTime())); //время последнего запроса данных (отображаем время устройства на момент запроса)
            updatedTextView.setText(String.format("%s %s", getString(R.string.last_update), updatedOn));


        } catch (Exception e) {
            Log.d("Log", msgException);//FIXME Обработка ошибки
        }
        progressBar.setVisibility(View.GONE);
    }

    // Подстановка нужной иконки
    // Парсим коды http://openweathermap.org/weather-conditions
    private void setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100; // Упрощение кодов (int оставляет только целочисленное значение)
        String icon = "";
        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = getString(R.string.weather_sunny);
            } else {
                icon = getString(R.string.weather_clear_night);
            }
        } else {
            switch (id) {
                case 2:
                    icon = getString(R.string.weather_thunder);
                    break;
                case 3:
                    icon = getString(R.string.weather_drizzle);
                    break;
                case 5:
                    icon = getString(R.string.weather_rainy);
                    break;
                case 6:
                    icon = getString(R.string.weather_snowy);
                    break;
                case 7:
                    icon = getString(R.string.weather_foggy);
                    break;
                case 8:
                    icon = getString(R.string.weather_cloudy);
                    break;
                // Можете доработать приложение, найдя все иконки и распарсив все значения
                default:
                    break;
            }
        }
        weatherIcon.setText(icon);
    }

    //обработка данных для получения направления ветра
    private void setWindDirecrionIcon(int deg) {
        String icon = "";
        if (deg >= 23 & deg <= 67) {
            icon = getString(R.string.north_east_wind_icon);
        } else if (deg >= 68 & deg <= 112) {
            icon = getString(R.string.east_wind_icon);
        } else if (deg >= 113 & deg <= 157) {
            icon = getString(R.string.south_east_wind_icon);
        } else if (deg >= 158 & deg <= 202) {
            icon = getString(R.string.south_wind_icon);
        } else if (deg >= 203 & deg <= 247) {
            icon = getString(R.string.south_west_wind_icon);
        } else if (deg >= 248 & deg <= 292) {
            icon = getString(R.string.west_wind_icon);
        } else if (deg >= 293 & deg <= 337) {
            icon = getString(R.string.north_west_wind_icon);
        } else {
            icon = getString(R.string.north_wind_icon);
        }
        directionWindIcon.setText(String.format(", %s", icon));
    }
}
