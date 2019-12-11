package com.nehvedovich.vladimir.pogoda.screens.rest.entites;

import com.google.gson.annotations.SerializedName;

public class ListRestModel {
    @SerializedName("dt") public long dt;
    @SerializedName("main") public MainRestModel main;
    @SerializedName("weather") public WeatherRestModel[] weather;
    @SerializedName("clouds") public  CloudsRestModel clouds;
    @SerializedName("rain") public  RainRestModel rain;
    @SerializedName("wind") public WindRestModel wind;
    @SerializedName("snow") public  SnowRestModel snow;
    @SerializedName("sys") public SysRestModel sys;
    @SerializedName("dt_txt") public String data;

}
