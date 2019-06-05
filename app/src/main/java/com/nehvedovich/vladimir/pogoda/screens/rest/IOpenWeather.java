package com.nehvedovich.vladimir.pogoda.screens.rest;

import com.nehvedovich.vladimir.pogoda.screens.rest.entites.WeatherRequestRestModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeather {
    @GET("data/2.5/weather")
    Call<WeatherRequestRestModel> loadWeather(@Query("q") String city,
                                              @Query("appid") String keyApi,
                                              @Query("units") String units,
                                              @Query("lang") String lang);

    @GET("data/2.5/weather")
    Call<WeatherRequestRestModel> loadWeatherByCoord(@Query("lat") String latitude,
                                                     @Query("lon") String Longitude,
                                                     @Query("appid") String keyApi,
                                                     @Query("units") String units,
                                                     @Query("lang") String lang);

    @GET("data/2.5/forecast")
    Call<WeatherRequestRestModel> loadForecastWeatherHourly(@Query("id") String cityId,
                                                             @Query("appid") String keyApi,
                                                             @Query("units") String units,
                                                             @Query("lang") String lang);
}
