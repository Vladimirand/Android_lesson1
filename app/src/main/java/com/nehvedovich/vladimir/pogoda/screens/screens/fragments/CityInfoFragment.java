package com.nehvedovich.vladimir.pogoda.screens.screens.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nehvedovich.vladimir.pogoda.R;
import com.nehvedovich.vladimir.pogoda.screens.database.WeatherDataSource;
import com.nehvedovich.vladimir.pogoda.screens.rest.OpenWeatherRepo;
import com.nehvedovich.vladimir.pogoda.screens.rest.entites.WeatherRequestRestModel;
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
    private ImageView imageView;

    WeatherRequestRestModel model = new WeatherRequestRestModel();
    String currentCityName;
    String msgException = "One or more fields not found in the JSON data";
    String apiKey = "bb0856d6336d3c2ca1a809b325fecefa";
    String units = "metric";

    private WeatherDataSource notesDataSource;     // Источник данных

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_info, container, false);
        Bundle bundle = getArguments();

        boolean pressure = false;
        boolean sunriseSunset = false;

        initDataSource();

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
        progressBar.setVisibility(View.VISIBLE);
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

    private void initDataSource() {
        notesDataSource = new WeatherDataSource(getContext());
        notesDataSource.open();
    }

    public void initVew(View layout) {
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
    }

    private void refreshList() {
        requestRetrofit();
        setUpdatedOn();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void requestRetrofit() {
        isOnline(Objects.requireNonNull(getContext())); //Проверяем подключение к интернету

        final Button detailsBtn = Objects.requireNonNull(getActivity()).findViewById(R.id.moreInformation);
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
                            detailsBtn.setVisibility(View.VISIBLE);
                            setUpdatedOn();
                            getDataForHistory();
                        } else {
                            loadImage("https://i.pinimg.com/originals/32/81/56/328156fb3ce91b68e080b37eecd66fd6.png");
                            Toast.makeText(getContext(),
                                    getString(R.string.error_city_not_found), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherRequestRestModel> call, @NonNull Throwable t) {
                        loadImage("error");
                    }
                });
    }

    private void getDataForHistory() {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        String time = dateFormat.format(new Date(currentTime.getTime()));

        String weatherDescription = String.valueOf(model.weather[0].description);
        Double temp = (double) model.main.temp;
        notesDataSource.addNote(currentCityName, String.format("%s ℃", String.format(Locale.US, "%.0f", temp)), weatherDescription, time);
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
    private void setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100; // Упрощение кодов (int оставляет только целочисленное значение)
        String icon = "";
        String url = "";
        long currentTime = new Date().getTime();

        if (actualId == 800) {
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = getString(R.string.weather_sunny);
                url = "http://clipart-library.com/img/1816931.png";
            } else {
                icon = getString(R.string.weather_clear_night);
                url = "http://clipart-library.com/img/1817023.png";
            }
        } else if (actualId == 801) {
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = getString(R.string.weather_day_cloudy);
                url = "http://clipart-library.com/img/1816960.png";
            } else {
                icon = getString(R.string.weather_night_cloudy);
                url = "http://clipart-library.com/image_gallery/583348.png";
            }
        } else if (actualId == 802) {
            icon = getString(R.string.weather_cloud);
            url = "https://upload.wikimedia.org/wikipedia/commons/4/40/Draw_cloudy.png";
        } else if ((actualId == 803) | (actualId == 804)) {
            icon = getString(R.string.weather_cloudy);
            url = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/35/Weather-heavy-overcast.svg/480px-Weather-heavy-overcast.svg.png";
        } else {
            switch (id) {
                case 2:
                    icon = getString(R.string.weather_thunder);
                    url = "http://clipart-library.com/img/1816972.png";
                    break;
                case 3:
                    icon = getString(R.string.weather_drizzle);
                    url = "https://banner2.kisspng.com/20180713/osr/kisspng-rain-weather-drizzle-cloud-snow-light-rain-5b4955002c4da3.8910760415315325441815.jpg";
                    break;
                case 5:
                    icon = getString(R.string.weather_rainy);
                    url = "http://clipart-library.com/img/1816918.png";
                    break;
                case 6:
                    icon = getString(R.string.weather_snowy);
                    url = "http://clipart-library.com/img/1816994.png";
                    break;
                case 7:
                    icon = getString(R.string.weather_foggy);
                    url = "https://cdn3.iconfinder.com/data/icons/flat-main-weather-conditions-2/842/fog-512.png";
                    break;
                case 8:
                    icon = getString(R.string.weather_cloudy);
                    url = "https://upload.wikimedia.org/wikipedia/commons/4/40/Draw_cloudy.png";
                    break;
                // Можете доработать приложение, найдя все иконки и распарсив все значения
                default:
                    break;
            }
        }
        loadImage(url);
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

    private void loadImage(String url) {
        Picasso.get()
                .load(url)
                .error(R.drawable.error)
                .into(imageView);
        progressBar.setVisibility(View.GONE);
    }

    //метод длчя проверки наличия подключения к интернету (если подключения нету - выводим сообщение)
    public void isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isConnectedOrConnecting()) {
            Toast.makeText(getContext(),
                    getString(R.string.error_internet_connection), Toast.LENGTH_LONG).show();
            showErrorDialog();
        }
    }

    //показываем окно с информацией о разработчике
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
}
