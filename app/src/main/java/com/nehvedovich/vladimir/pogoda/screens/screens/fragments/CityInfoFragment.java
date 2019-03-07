package com.nehvedovich.vladimir.pogoda.screens.screens.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
import java.util.Date;

public class CityInfoFragment extends Fragment {

    public static final String CITY_NAME_EXSTRA = "cityLookingFor";

    private static final String FONT_FILENAME = "fonts/weathericons.ttf";

    private final Handler handler = new Handler();
    private Typeface weatherFont;
    private TextView cityTextView;
    //    private TextView updatedTextView;
//    private TextView detailsTextView;
    private TextView sunriseTextView;
    private TextView sunsetTextView;
    private TextView weatherConditions;
    private TextView currentTemperatureTextView;
    private TextView weatherIcon;
    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_info, container, false);

        Bundle bundle = getArguments();

        boolean wind = false;
        boolean humidity = false;
        boolean pressure = false;
        boolean feelLike = false;


        if (bundle != null) {
            TextView cityName = layout.findViewById(R.id.cityNameInfo);
            cityName.setText(bundle.getString(CITY_NAME_EXSTRA));
//загружаем данные погоды
            updateWeatherData(bundle.getString(CITY_NAME_EXSTRA), getString(R.string.location));

            wind = bundle.getBoolean(CitiesFragment.CHECK_BOX_WIND);
            humidity = bundle.getBoolean(CitiesFragment.CHECK_BOX_HUMIDITY);
            pressure = bundle.getBoolean(CitiesFragment.CHECK_BOX_PRESSURE);
            feelLike = bundle.getBoolean(CitiesFragment.CHECK_BOX_FEEL_LIKE);
        }

//        Обработка CheckBox Wind
        TextView textWind = (TextView) layout.findViewById(R.id.textWind);
        TextView textDirection = (TextView) layout.findViewById(R.id.directionWind);

        if (wind == true) {
            textWind.setVisibility(View.VISIBLE);
            textDirection.setVisibility(View.VISIBLE);
        } else {
            textWind.setVisibility(View.GONE);
            textDirection.setVisibility(View.GONE);
        }

        //Обработка CheckBox humidity
        TextView textHumidity = (TextView) layout.findViewById(R.id.textHumidity);

        if (humidity == true) {
            textHumidity.setVisibility(View.VISIBLE);
        } else {
            textHumidity.setVisibility(View.GONE);
        }

        //Обработка CheckBox Pressure
        TextView textPressure = (TextView) layout.findViewById(R.id.textPressure);

        if (pressure == true) {
            textPressure.setVisibility(View.VISIBLE);
        } else {
            textPressure.setVisibility(View.GONE);
        }

        //Обработка CheckBox FeelLike
        TextView textFeelLike = (TextView) layout.findViewById(R.id.textFeelsLike);
        TextView textFeelLikeT = (TextView) layout.findViewById(R.id.textFeelsLikeT);

        if (feelLike == true) {
            textFeelLike.setVisibility(View.VISIBLE);
            textFeelLikeT.setVisibility(View.VISIBLE);
        } else {
            textFeelLike.setVisibility(View.GONE);
            textFeelLikeT.setVisibility(View.GONE);
        }

        currentTemperatureTextView = layout.findViewById(R.id.textTemperature);
        weatherIcon = layout.findViewById(R.id.weather_icon);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), FONT_FILENAME);
        weatherIcon.setTypeface(weatherFont);
        weatherConditions = layout.findViewById(R.id.weather_conditions);
        progressBar = layout.findViewById(R.id.progressBar);
        sunriseTextView = layout.findViewById(R.id.textSunrise);
        sunsetTextView = layout.findViewById(R.id.textSunset);
        return layout;
    }


//    private void setViews() {
////        cityTextView = getActivity().findViewById(R.id.cityNameInfo);
//////            updatedTextView = findViewById(R.id.updated_field);
////            detailsTextView = findViewById(R.id.details_field);
//        currentTemperatureTextView = getActivity().findViewById(R.id.textTemperature);
////            weatherIcon = findViewById(R.id.weather_icon);
////            weatherIcon.setTypeface(weatherFont);
//    }

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
//            cityTextView.setText(json.getString("name").toUpperCase(Locale.US) +
//                    ", " +
//                    json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            weatherConditions.setText(details.getString("description"));

//            detailsTextView.setText(details.getString("description").toUpperCase(Locale.US) +
//                    "\n" +
//                    "Humidity: " +
//                    main.getString("humidity")
//                    "%" +
//                    "\n" +
//                    "Pressure: " +
//                    main.getString("pressure")
//                    + " hPa");

            currentTemperatureTextView.setText(String.format("%.0f", main.getDouble("temp")) + " ℃");

            //отображаем время рассвета
//            DateFormat sunrise = DateFormat.getDateTimeInstance();  //отображение даты полностью
            DateFormat sunrise = new SimpleDateFormat("HH:mm"); //отображение только времени часы/минуты
            String sunriseTime = sunrise.format(new Date(json.getJSONObject("sys").getLong("sunrise") * 1000));
//            DateFormat t = new SimpleDateFormat("HH/mm");
//            Date tt = t.parse(t.format(sunriseTime));

            sunriseTextView.setText(getString(R.string.sunrise) + ":  " + sunriseTime);

            //отображаем время заката
            DateFormat sunset = new SimpleDateFormat("HH:mm");
            String sunsetTime = sunset.format(new Date(json.getJSONObject("sys").getLong("sunset") * 1000));
            sunsetTextView.setText(getString(R.string.sunset) + ":  " + sunsetTime);


//            DateFormat df = DateFormat.getDateTimeInstance();
//            String updatedOn = df.format(new Date(json.getLong("dt") * 1000));
//            updatedTextView.setText("Last update: " + updatedOn);

            setWeatherIcon(details.getInt("id"), json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

        } catch (Exception e) {
            Log.d("Log", "One or more fields not found in the JSON data");//FIXME Обработка ошибки
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
}
