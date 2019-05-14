package com.nehvedovich.vladimir.pogoda.screens.database;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.nehvedovich.vladimir.pogoda.screens.utils.WConstants;

import java.util.List;

public class CityService extends IntentService {

    public static final String WEATHER_CITY_SERVICE = "WeatherCityService";

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //Здесь будем получать список городов
        List<City> list = getCitiesList();
        senDataToActivity(new ParcelableObjectList<>(list));
    }

    private List<City> getCitiesList() {
        return CityRepository.getInstance().getAllList();
    }

    private void senDataToActivity(ParcelableObjectList<City> objects) {
        Intent responseIntent = new Intent();
        responseIntent.setAction(WConstants.SERVICE_CITY_RESPONSE);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
        responseIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        responseIntent.putExtra(WConstants.CITIES_LIST, objects);
        sendBroadcast(responseIntent);
    }

    public CityService() {
        super(WEATHER_CITY_SERVICE);
    }
}
