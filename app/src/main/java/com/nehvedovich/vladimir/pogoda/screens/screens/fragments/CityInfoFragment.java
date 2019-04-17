package com.nehvedovich.vladimir.pogoda.screens.screens.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nehvedovich.vladimir.pogoda.R;
import com.nehvedovich.vladimir.pogoda.screens.rest.OpenWeatherRepo;
import com.nehvedovich.vladimir.pogoda.screens.rest.entites.WeatherRequestRestModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityInfoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String CITY_NAME_EXTRA = "cityLookingFor";
    private static final String FONT_FILENAME = "fonts/weather_icons.ttf";

    private SwipeRefreshLayout swipeRefreshLayout;

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

    WeatherRequestRestModel model = new WeatherRequestRestModel();
    String currentCityName;
    String msgException = "One or more fields not found in the JSON data";
    String apiKey = "bb0856d6336d3c2ca1a809b325fecefa";
    String units = "metric";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_info, container, false);
        Bundle bundle = getArguments();

        boolean pressure = false;
        boolean sunriseSunset = false;

        if (bundle != null) {
            TextView cityName = layout.findViewById(R.id.cityNameInfo);
            cityName.setText(bundle.getString(CITY_NAME_EXTRA));
            currentCityName = (bundle.getString(CITY_NAME_EXTRA));
            pressure = bundle.getBoolean(CitiesFragment.CHECK_BOX_PRESSURE);
            sunriseSunset = bundle.getBoolean(CitiesFragment.CHECK_BOX_SUNRISE_AND_SUNSET);
        }
        requestRetrofit();  //загружаем данные погоды
        getCheckBox(layout, sunriseSunset, pressure);

        return layout;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        refreshList();
    }

    public void getCheckBox(View layout, boolean sunriseSunset, boolean pressure) {
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
        initVew(layout);

        sunriseTextView = textSunrise;
        sunsetTextView = textSunset;

    }
    public void initVew(View layout){
        Typeface weatherFont = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), FONT_FILENAME);

        cityTextView = layout.findViewById(R.id.cityName);
        currentTemperatureTextView = layout.findViewById(R.id.textTemperature);

        weatherIcon = layout.findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);
        updatedTextView = layout.findViewById(R.id.data);
        weatherConditions = layout.findViewById(R.id.weather_conditions);
        weatherExpected = layout.findViewById(R.id.weather_expected);
        progressBar = layout.findViewById(R.id.progressBar);

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

        swipeRefreshLayout = layout.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void refreshList() {
        requestRetrofit();
        setUpdatedOn();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void requestRetrofit() {
        OpenWeatherRepo.getSingleton().getAPI().loadWeather(currentCityName,
                apiKey, units, getString(R.string.location))
                .enqueue(new Callback<WeatherRequestRestModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequestRestModel> call,
                                           @NonNull Response<WeatherRequestRestModel> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            model = response.body();
                            setCityFullName();
                            setWeatherIcon();
                            setTemperature();
                            setWeather();
                            setPressure();
                            setHumidity();
                            setSunriseAndSunset();
                            setWind();
                            progressBar.setVisibility(View.GONE);
                            setUpdatedOn();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherRequestRestModel> call, @NonNull Throwable t) {
                        currentTemperatureTextView.setText(R.string.error);
                    }
                });
    }

    private void setWind() {
        try {
            windIcon.setText(getString(R.string.wind_icon));
            windTextView.setText(String.format("%s %s", model.wind.speed, getString(R.string.wind_speed_m_s)));

            //получаем направление ветра
            int deg = (int) model.wind.deg;
            setWindDirectionIcon(deg);
        } catch (Exception e) {
            Log.d("Log", msgException + "(in 'wind')");//FIXME Обработка ошибки
        }
    }

    private void setSunriseAndSunset() {
        //отображаем время рассвета
        DateFormat df; //отображение только времени часы/минуты
        df = new SimpleDateFormat("HH:mm", Locale.US);

        String sunriseTime = df.format(new Date(model.sys.sunrise * 1000));

        sunriseTextView.setText(String.format("%s:  %s", getString(R.string.sunrise), sunriseTime));

        //отображаем время заката
        String sunsetTime = df.format(new Date(model.sys.sunset * 1000));
        sunsetTextView.setText(String.format("%s:  %s", getString(R.string.sunset), sunsetTime));
    }

    private void setHumidity() {
        humidityIcon.setText(getString(R.string.humidity_icon));
        int humidity = model.main.humidity;
        humidityTextView.setText(String.format("%s%%", humidity));
    }

    private void setPressure() {
        pressureIcon.setText(getString(R.string.pressure_icon));
        int pressure = model.main.pressure;
        //переводим значение hPa в мм.рт.ст
        double i = (double) pressure * 0.750062;
        String si = String.format(Locale.US, "%.0f", i); //отображаем только значение до запятой
        pressureTextView.setText(String.format("%s %s", si, getString(R.string.pressure_mmHg)));
    }

    private void setCityFullName() {
        String city = model.name;
        String country = model.sys.country;
        cityTextView.setText(String.format("%s, %s", city, country));
    }

    private void setWeatherIcon() {
        int id = model.weather[0].id;
        long sunrise = model.sys.sunrise;
        long sunset = model.sys.sunset;
        setWeatherIcon(id, sunrise * 1000,
                sunset * 1000);
    }

    private void setTemperature() {
        Double text = (double) model.main.temp;
        currentTemperatureTextView.setText(String.format("%s ℃", String.format(Locale.US, "%.0f", text)));
    }

    private void setWeather() {
        String text = String.valueOf(model.weather[0].description);
        weatherConditions.setText(text.toUpperCase());
        try {
            String textExpected = String.valueOf(model.weather[1].description);
            weatherExpected.setText(String.format("(%s)", textExpected));
            weatherExpected.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            Log.d("Log", msgException + "(in 'description')");//FIXME Обработка ошибки
        }
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
    private void setWindDirectionIcon(int deg) {
        String icon;
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

    private void setUpdatedOn() {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        String updatedOn = dateFormat.format(new Date(currentTime.getTime())); //время последнего запроса данных (отображаем время устройства на момент запроса)
        updatedTextView.setText(String.format("%s %s", getString(R.string.last_update), updatedOn));
    }
}
