package com.nehvedovich.vladimir.pogoda.screens.screens.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.card.MaterialCardView;
import com.nehvedovich.vladimir.pogoda.R;
import com.nehvedovich.vladimir.pogoda.screens.rest.OpenWeatherRepo;
import com.nehvedovich.vladimir.pogoda.screens.rest.entites.WeatherRequestRestModel;
import com.nehvedovich.vladimir.pogoda.screens.screens.MainActivity;
import com.nehvedovich.vladimir.pogoda.screens.screens.WebViewActivity;
import com.squareup.picasso.Picasso;

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
    public static final String COORD_LATITUDE = "latitude";
    public static final String COORD_LONGITUDE = "longitude";
    private static final String FONT_FILENAME = "fonts/weather_icons.ttf";

    private SwipeRefreshLayout swipeRefreshLayout;

    private LinearLayout forecastWeather;
    private LinearLayout forecastWeatherDaily;

    private LinearLayout windAndHumidity;
    private MaterialCardView pressureInfo;

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
    private ImageView imageView;
    private TextView cityName;

    private TextView dayOfWeek2;
    private TextView dayOfWeek3;
    private TextView weatherNightIcon1;
    private TextView weatherDailyIcon1;
    private TextView weatherNightIcon2;
    private TextView weatherDailyIcon2;
    private TextView weatherNightIcon3;
    private TextView weatherDailyIcon3;

    private TextView temp1;
    private TextView temp2;
    private TextView temp3;
    private TextView temp4;
    private TextView temp5;
    private TextView temp6;
    private TextView temp7;
    private TextView temp8;

    private TextView weatherIcon1;
    private TextView weatherIcon2;
    private TextView weatherIcon3;
    private TextView weatherIcon4;
    private TextView weatherIcon5;
    private TextView weatherIcon6;
    private TextView weatherIcon7;
    private TextView weatherIcon8;

    private TextView windText1;
    private TextView windText2;
    private TextView windText3;
    private TextView windText4;
    private TextView windText5;
    private TextView windText6;
    private TextView windText7;
    private TextView windText8;

    private TextView onMap;

    private WeatherRequestRestModel model = new WeatherRequestRestModel();
    private WeatherRequestRestModel modelH = new WeatherRequestRestModel();

    private String currentCityName;
    private String latitude;
    private String longitude;
    private Boolean internetConnection;
    private Boolean catsHelper = false; //для того, чтобы определять в каких случаях показываем картинку с котами

    private String msgException = "One or more fields not found in the JSON data";
    private String apiKey = "bb0856d6336d3c2ca1a809b325fecefa";
    private String units = "metric";

    private String cityId;

    private static float lon;
    private static float lat;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_info, container, false);
        Bundle bundle = getArguments();

        boolean pressure = false;
        boolean sunriseSunset = false;

//        initDataSource();

        if (bundle != null) {
            cityName = layout.findViewById(R.id.cityNameInfo);
            String city = bundle.getString(CITY_NAME_EXTRA);

            if (city != null && city.contains(",")) {
                city = city.substring(0, city.indexOf(","));
                cityName.setText(city);
            } else {
                cityName.setText(bundle.getString(CITY_NAME_EXTRA));
            }

            latitude = bundle.getString(COORD_LATITUDE);
            longitude = bundle.getString(COORD_LONGITUDE);
            currentCityName = (bundle.getString(CITY_NAME_EXTRA));
            pressure = bundle.getBoolean(CitiesFragment.CHECK_BOX_PRESSURE);
            sunriseSunset = bundle.getBoolean(CitiesFragment.CHECK_BOX_SUNRISE_AND_SUNSET);
        }
        getCheckBox(layout, sunriseSunset, pressure);

        retrofitStart(); //загружаем данные погоды

        detailsButton();

        return layout;
    }

    private void retrofitStart() {
        isOnline(Objects.requireNonNull(getContext())); //Проверяем подключение к интернету
        if (internetConnection) {
            catsHelper = true;
            if (latitude != null & longitude != null) {
                requestRetrofitByCoord();  //загружаем данные погоды, если получили местоположение
            } else {
                if (currentCityName != null) {
                    requestRetrofit();  //загружаем данные погоды выбранного города
                }
            }
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        refreshList();
//        progressBar.setVisibility(View.VISIBLE);
    }

    private void getCheckBox(View layout, boolean sunriseSunset, boolean pressure) {
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

    private void initVew(View layout) {
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

        imageView = layout.findViewById(R.id.imageView);
        forecastWeather = layout.findViewById(R.id.forecastWeather);
        forecastWeatherDaily = layout.findViewById(R.id.forecastWeatherDaily);
        windAndHumidity = layout.findViewById(R.id.windAndHumidityInfo);
        pressureInfo = layout.findViewById(R.id.pressureInfo);

        temp1 = layout.findViewById(R.id.temp1);
        temp2 = layout.findViewById(R.id.temp2);
        temp3 = layout.findViewById(R.id.temp3);
        temp4 = layout.findViewById(R.id.temp4);
        temp5 = layout.findViewById(R.id.temp5);
        temp6 = layout.findViewById(R.id.temp6);
        temp7 = layout.findViewById(R.id.temp7);
        temp8 = layout.findViewById(R.id.temp8);
        weatherIcon1 = layout.findViewById(R.id.weather_icon1);
        weatherIcon2 = layout.findViewById(R.id.weather_icon2);
        weatherIcon3 = layout.findViewById(R.id.weather_icon3);
        weatherIcon4 = layout.findViewById(R.id.weather_icon4);
        weatherIcon5 = layout.findViewById(R.id.weather_icon5);
        weatherIcon6 = layout.findViewById(R.id.weather_icon6);
        weatherIcon7 = layout.findViewById(R.id.weather_icon7);
        weatherIcon8 = layout.findViewById(R.id.weather_icon8);
        windText1 = layout.findViewById(R.id.textWind1);
        windText2 = layout.findViewById(R.id.textWind2);
        windText3 = layout.findViewById(R.id.textWind3);
        windText4 = layout.findViewById(R.id.textWind4);
        windText5 = layout.findViewById(R.id.textWind5);
        windText6 = layout.findViewById(R.id.textWind6);
        windText7 = layout.findViewById(R.id.textWind7);
        windText8 = layout.findViewById(R.id.textWind8);

        windText1.setTypeface(weatherFont);
        windText2.setTypeface(weatherFont);
        windText3.setTypeface(weatherFont);
        windText4.setTypeface(weatherFont);
        windText5.setTypeface(weatherFont);
        windText6.setTypeface(weatherFont);
        windText7.setTypeface(weatherFont);
        windText8.setTypeface(weatherFont);

        weatherIcon1.setTypeface(weatherFont);
        weatherIcon2.setTypeface(weatherFont);
        weatherIcon3.setTypeface(weatherFont);
        weatherIcon4.setTypeface(weatherFont);
        weatherIcon5.setTypeface(weatherFont);
        weatherIcon6.setTypeface(weatherFont);
        weatherIcon7.setTypeface(weatherFont);
        weatherIcon8.setTypeface(weatherFont);

        dayOfWeek2 = layout.findViewById(R.id.day_of_week2);
        dayOfWeek3 = layout.findViewById(R.id.day_of_week3);

        weatherNightIcon1 = layout.findViewById(R.id.weather_night_icon1);
        weatherDailyIcon1 = layout.findViewById(R.id.weather_daily_icon1);
        weatherNightIcon1.setTypeface(weatherFont);
        weatherDailyIcon1.setTypeface(weatherFont);
        weatherNightIcon2 = layout.findViewById(R.id.weather_night_icon2);
        weatherDailyIcon2 = layout.findViewById(R.id.weather_daily_icon2);
        weatherNightIcon2.setTypeface(weatherFont);
        weatherDailyIcon2.setTypeface(weatherFont);
        weatherNightIcon3 = layout.findViewById(R.id.weather_night_icon3);
        weatherDailyIcon3 = layout.findViewById(R.id.weather_daily_icon3);
        weatherNightIcon3.setTypeface(weatherFont);
        weatherDailyIcon3.setTypeface(weatherFont);

        onMap = layout.findViewById(R.id.onMap);
    }

    private void refreshList() {
        retrofitStart();
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
                            setInformation();
                        } else {
                            setInfoError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherRequestRestModel> call, @NonNull Throwable t) {
                        loadImageForError(R.drawable.error);
                    }
                });
    }

    private void requestRetrofitByCoord() {
        OpenWeatherRepo.getSingleton().getAPI().loadWeatherByCoord(latitude, longitude,
                apiKey, units, getString(R.string.location))
                .enqueue(new Callback<WeatherRequestRestModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequestRestModel> call,
                                           @NonNull Response<WeatherRequestRestModel> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            model = response.body();
                            setCityFullNameByCoord();
                            setInformation();
                        } else {
                            setInfoError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherRequestRestModel> call, @NonNull Throwable t) {
                        loadImageForError(R.drawable.error);
                    }
                });
    }

    private void detailsButton() {
        onMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                final String yandexHttp = getString(R.string.yandexWeatherMap);
                String url = (yandexHttp + CityInfoFragment.lat + "&lon=" + CityInfoFragment.lon + "&ll=" + CityInfoFragment.lon + "_" + CityInfoFragment.lat + "&z=9");
                intent.setClass(Objects.requireNonNull(getContext()), WebViewActivity.class);
                intent.putExtra(WebViewActivity.URL, url);
                startActivity(intent);
            }
        });
    }

    private void setInformation() {
        setWeatherIcon();
        setTemperature();
        setWeather();
        setPressure();
        setHumidity();
        setSunriseAndSunset();
        setWind();
        windAndHumidity.setVisibility(View.VISIBLE);
        onMap.setVisibility(View.VISIBLE);
        setUpdatedOn();
        lon = model.coordinates.lon;
        lat = model.coordinates.lat;
        cityId = String.valueOf(model.id);
        requestRetrofitForecast3h(cityId);
    }

    private void setInfoError() {
        loadImageForError(R.drawable.not_found);
        Toast.makeText(getContext(),
                getString(R.string.error_city_not_found), Toast.LENGTH_LONG).show();
    }

    private void setWind() {
        try {
            windIcon.setText(getString(R.string.wind_icon));
            Double speed = (double) model.wind.speed;
            windTextView.setText(String.format("%s %s", String.format(Locale.US, "%.0f", speed), getString(R.string.wind_speed_m_s)));

            //получаем направление ветра
            int deg = (int) model.wind.deg;
            String icon = setWindDirectionIcon(deg);
            directionWindIcon.setText(String.format(", %s", icon));
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
        float humidity = model.main.humidity;
        humidityTextView.setText(String.format("%s%%", String.format(Locale.US, "%.0f", humidity)));
    }

    private void setPressure() {
        pressureIcon.setText(getString(R.string.pressure_icon));
        float pressure = model.main.pressure;
        //переводим значение hPa в мм.рт.ст
        double i = (double) pressure * 0.750062;
        String si = String.format(Locale.US, "%.0f", i); //отображаем только значение до запятой
        pressureTextView.setText(String.format("%s %s", si, getString(R.string.pressure_mmHg)));
        pressureInfo.setVisibility(View.VISIBLE);
    }

    private void setCityFullNameByCoord() {
        String city = model.name;
        if (model.sys.country != null) {
            String country = model.sys.country;
            cityName.setText(String.format("%s, %s", city, country));
            cityTextView.setText(R.string.your_location);
            currentCityName = city;
        }
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
        String icon = "";
        long currentTime = new Date().getTime();
        icon = setWeatherIcon(id, sunrise * 1000,
                sunset * 1000, currentTime, icon, true);
        weatherIcon.setText(icon);
    }

    private void setTemperature() {
        Double temp = (double) model.main.temp;
        currentTemperatureTextView.setText(String.format("%s ℃", String.format(Locale.US, "%.0f", temp)));
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
    private String setWeatherIcon(int actualId, long sunrise, long sunset, long currentTime, String icon, boolean loadImage) {
        int id = actualId / 100; // Упрощение кодов (int оставляет только целочисленное значение)
        int image = 0;
        if (actualId == 800) {
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = getString(R.string.weather_sunny);
                image = R.drawable.weather_sunny;
            } else {
                icon = getString(R.string.weather_clear_night);
                image = R.drawable.weather_clear_night;
            }
        } else if (actualId == 801) {
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = getString(R.string.weather_day_cloudy);
                image = R.drawable.weather_day_cloudy;
            } else {
                icon = getString(R.string.weather_night_cloudy);
                image = R.drawable.weather_night_cloudy;
            }
        } else if ((actualId == 500) | (actualId == 501) | (actualId == 502) | (actualId == 503) | (actualId == 504)) {
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = getString(R.string.weather_sun_clouds_rain);
                image = R.drawable.weather_sun_clouds_rain;
            } else {
                icon = getString(R.string.weather_night_clouds_rain);
                image = R.drawable.weather_night_clouds_rain;
            }

        } else if ((actualId == 521) | (actualId == 522) | (actualId == 531)) {
            icon = getString(R.string.weather_showers);
            image = R.drawable.weather_showers;

        } else if (actualId == 802) {
            icon = getString(R.string.weather_cloud);
            image = R.drawable.weather_cloud;
        } else if ((actualId == 803) | (actualId == 804)) {
            icon = getString(R.string.weather_cloudy);
            image = R.drawable.weather_cloudy;
        } else {
            switch (id) {
                case 2:
                    icon = getString(R.string.weather_thunder);
                    image = R.drawable.weather_thunder;
                    break;
                case 3:
                    icon = getString(R.string.weather_drizzle);
                    image = R.drawable.weather_drizzle;
                    break;
                case 5:
                    icon = getString(R.string.weather_rainy);
                    image = R.drawable.weather_rainy;
                    break;
                case 6:
                    icon = getString(R.string.weather_snowy);
                    image = R.drawable.weather_snowy;
                    break;
                case 7:
                    icon = getString(R.string.weather_foggy);
                    image = R.drawable.weather_foggy;
                    break;
                case 8:
                    icon = getString(R.string.weather_cloudy);
                    image = R.drawable.weather_cloud;
                    break;
                // Можете доработать приложение, найдя все иконки и распарсив все значения
                default:
                    break;
            }
        }
        if (loadImage) loadImage(image);
        return icon;
    }

    //обработка данных для получения направления ветра
    private String setWindDirectionIcon(int deg) {
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
        return icon;
    }

    private void setUpdatedOn() {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        String updatedOn = dateFormat.format(new Date(currentTime.getTime())); //время последнего запроса данных (отображаем время устройства на момент запроса)
        updatedTextView.setText(String.format("%s %s", getString(R.string.last_update), updatedOn));
    }

    private void loadImage(int image) {
        if (MainActivity.minimalisticIcons) {
            imageView.setVisibility(View.INVISIBLE);
            weatherIcon.setVisibility(View.VISIBLE);
        } else {
            Picasso.get()
                    .load(image)
                    .error(R.drawable.error)
                    .into(imageView);
        }
        progressBar.setVisibility(View.GONE);
    }

    private void loadImageForError(int image) {
        Picasso.get()
                .load(image)
                .error(R.drawable.error)
                .into(imageView);
        progressBar.setVisibility(View.GONE);
    }

    //метод длчя проверки наличия подключения к интернету (если подключения нету - выводим сообщение)
    private void isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        final ImageView catsWeather = Objects.requireNonNull(getActivity()).findViewById(R.id.catsWeather);

        if (netInfo == null || !netInfo.isConnectedOrConnecting()) {
            internetConnection = false;
            Toast.makeText(getContext(),
                    getString(R.string.error_internet_connection), Toast.LENGTH_LONG).show();
            showErrorDialog();
            if (!catsHelper) {
                catsWeather.setVisibility(View.VISIBLE);
                catsHelper = false;
            }
        } else {
            internetConnection = true;
            catsWeather.setVisibility(View.GONE);
        }
    }

    private void showErrorDialog() {
        AlertDialog.Builder errorConnection = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        errorConnection.setIcon(R.drawable.error_icon);
        errorConnection.setTitle(R.string.error);
        errorConnection.setMessage(R.string.error_internet_text);
        errorConnection.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        errorConnection.show();
    }

    private void requestRetrofitForecast3h(final String id) {
        OpenWeatherRepo.getSingleton().getAPI().loadForecastWeatherHourly(id,
                apiKey, units, getString(R.string.location))
                .enqueue(new Callback<WeatherRequestRestModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequestRestModel> call,
                                           @NonNull Response<WeatherRequestRestModel> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            modelH = response.body();
                            setForecastData();
                            forecastWeather.setVisibility(View.VISIBLE);
                            forecastWeatherDaily.setVisibility(View.VISIBLE);
                            setForecastDailyData();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherRequestRestModel> call, @NonNull Throwable t) {
                    }
                });
    }

    private String getForecastWind(int i) {
        String info = "";
        try {
            Double speed = (double) modelH.list[i].wind.speed;
            //получаем направление ветра
            int deg = (int) modelH.list[i].wind.deg;
            String icon = setWindDirectionIcon(deg);
            info = String.format("%s %s, %s", String.format(Locale.US, "%.0f", speed), getString(R.string.wind_speed_m_s), icon);
        } catch (Exception e) {
            Log.d("Log", msgException + "(in forecast 'wind')" + i);//FIXME Обработка ошибки
        }
        return info;
    }

    private void setForecastData() {
        Double t1 = (double) modelH.list[0].main.temp;
        Double t2 = (double) modelH.list[1].main.temp;
        Double t3 = (double) modelH.list[2].main.temp;
        Double t4 = (double) modelH.list[3].main.temp;
        Double t5 = (double) modelH.list[4].main.temp;
        Double t6 = (double) modelH.list[5].main.temp;
        Double t7 = (double) modelH.list[6].main.temp;
        Double t8 = (double) modelH.list[7].main.temp;
        temp1.setText(String.format("%s ℃", String.format(Locale.US, "%.0f", t1)));
        temp2.setText(String.format("%s ℃", String.format(Locale.US, "%.0f", t2)));
        temp3.setText(String.format("%s ℃", String.format(Locale.US, "%.0f", t3)));
        temp4.setText(String.format("%s ℃", String.format(Locale.US, "%.0f", t4)));
        temp5.setText(String.format("%s ℃", String.format(Locale.US, "%.0f", t5)));
        temp6.setText(String.format("%s ℃", String.format(Locale.US, "%.0f", t6)));
        temp7.setText(String.format("%s ℃", String.format(Locale.US, "%.0f", t7)));
        temp8.setText(String.format("%s ℃", String.format(Locale.US, "%.0f", t8)));


        DateFormat df; //отображение только времени часы/минуты
        df = new SimpleDateFormat("HH:mm", Locale.US);
        long tm1 = (modelH.list[0].dt * 1000);
        String time1 = df.format(tm1);
        long tm2 = (modelH.list[1].dt * 1000);
        String time2 = df.format(tm2);
        long tm3 = (modelH.list[2].dt * 1000);
        String time3 = df.format(tm3);
        long tm4 = (modelH.list[3].dt * 1000);
        String time4 = df.format(tm4);
        long tm5 = (modelH.list[4].dt * 1000);
        String time5 = df.format(tm5);
        long tm6 = (modelH.list[5].dt * 1000);
        String time6 = df.format(tm6);
        long tm7 = (modelH.list[6].dt * 1000);
        String time7 = df.format(tm7);
        long tm8 = (modelH.list[7].dt * 1000);
        String time8 = df.format(tm8);

        String icon1 = setWeatherIconH(0, tm1);
        String icon2 = setWeatherIconH(1, tm2);
        String icon3 = setWeatherIconH(2, tm3);
        String icon4 = setWeatherIconH(3, tm4);
        String icon5 = setWeatherIconH(4, tm5);
        String icon6 = setWeatherIconH(5, tm6);
        String icon7 = setWeatherIconH(6, tm7);
        String icon8 = setWeatherIconH(7, tm7);

        weatherIcon1.setText(icon1);
        weatherIcon2.setText(icon2);
        weatherIcon3.setText(icon3);
        weatherIcon4.setText(icon4);
        weatherIcon5.setText(icon5);
        weatherIcon6.setText(icon6);
        weatherIcon7.setText(icon7);
        weatherIcon8.setText(icon8);

        windText1.setText(String.format("%s\n %s", getForecastWind(0), time1));
        windText2.setText(String.format("%s\n %s", getForecastWind(1), time2));
        windText3.setText(String.format("%s\n %s", getForecastWind(2), time3));
        windText4.setText(String.format("%s\n %s", getForecastWind(3), time4));
        windText5.setText(String.format("%s\n %s", getForecastWind(4), time5));
        windText6.setText(String.format("%s\n %s", getForecastWind(5), time6));
        windText7.setText(String.format("%s\n %s", getForecastWind(6), time7));
        windText8.setText(String.format("%s\n %s", getForecastWind(7), time8));
    }

    private String setWeatherIconH(int i, long time) {
        int id = modelH.list[i].weather[0].id;
        long sunrise = model.sys.sunrise * 1000;
        long sunset = model.sys.sunset * 1000;

        DateFormat dfHour;
        dfHour = new SimpleDateFormat("HH", Locale.US); //берем только "часы"
        long sSunrise = Integer.parseInt(dfHour.format(sunrise));
        long sSunset = Integer.parseInt(dfHour.format(sunset));
        long presentTime = Integer.parseInt(dfHour.format(time));
        String icon = "";
        icon = setWeatherIcon(id, sSunrise,
                sSunset, presentTime, icon, false);
        return icon;
    }

    private String setWeatherIconDaily(int i, long time) {
        int id = modelH.list[i].weather[0].id;
        long sunrise = 0;
        long sunset = 1;
        String icon = "";
        icon = setWeatherIcon(id, sunrise,
                sunset, time, icon, false);
        return icon;
    }

    private void setForecastDailyData() {
        long tm1 = (modelH.list[0].dt * 1000);
        DateFormat dfHour;
        dfHour = new SimpleDateFormat("HH", Locale.US); //берем только "часы"
        int hour = Integer.parseInt(dfHour.format(tm1));
        int i;
        if (hour == 0) {    //определяем какой эллемент в маассиве нам нужен
            i = 5;
        } else {
            i = (24 - hour + 15) / 3;
        }

        Double tempDay1 = (double) modelH.list[i].main.temp;
        Double tempNight1 = (double) modelH.list[i + 4].main.temp;

        String iconDay1 = setWeatherIconDaily(i, 0);
        String iconNight1 = setWeatherIconDaily(i + 4, 1);

        weatherDailyIcon1.setText(String.format("%s ℃  %s    %s", String.format(Locale.US, "%.0f", tempDay1), iconDay1, getForecastWind(i)));
        weatherNightIcon1.setText(String.format("%s ℃  %s    %s", String.format(Locale.US, "%.0f", tempNight1), iconNight1, getForecastWind(i + 4)));

        //прогноз на 2-ой день
        DateFormat df; //отображение дня недели
        df = new SimpleDateFormat("EEEE", Locale.getDefault());
        long day2 = (modelH.list[i + 8].dt * 1000);
        String dayWeek2 = df.format(day2);
        dayOfWeek2.setText(firstUpperCase(dayWeek2));

        Double tempDay2 = (double) modelH.list[i + 8].main.temp;
        Double tempNight2 = (double) modelH.list[i + 12].main.temp;

        String iconDay2 = setWeatherIconDaily(i + 8, 0);
        String iconNight2 = setWeatherIconDaily(i + 12, 1);

        weatherDailyIcon2.setText(String.format("%s ℃  %s    %s", String.format(Locale.US, "%.0f", tempDay2), iconDay2, getForecastWind(i + 8)));
        weatherNightIcon2.setText(String.format("%s ℃  %s    %s", String.format(Locale.US, "%.0f", tempNight2), iconNight2, getForecastWind(i + 12)));

        //прогноз на 3-ий день
        long day3 = (modelH.list[i + 16].dt * 1000);
        String dayWeek3 = df.format(day3);
        dayOfWeek3.setText(firstUpperCase(dayWeek3));

        Double tempDay3 = (double) modelH.list[i + 16].main.temp;
        Double tempNight3 = (double) modelH.list[i + 20].main.temp;

        String iconDay3 = setWeatherIconDaily(i + 16, 0);
        String iconNight3 = setWeatherIconDaily(i + 20, 1);

        weatherDailyIcon3.setText(String.format("%s ℃  %s    %s", String.format(Locale.US, "%.0f", tempDay3), iconDay3, getForecastWind(i + 16)));
        weatherNightIcon3.setText(String.format("%s ℃  %s    %s", String.format(Locale.US, "%.0f", tempNight3), iconNight3, getForecastWind(i + 20)));
    }

    private String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) return ""; //или return word;
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }
}
