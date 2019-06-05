package com.nehvedovich.vladimir.pogoda.screens.rest.entites;

import com.google.gson.annotations.SerializedName;

public class WeatherRequestRestModel {
    @SerializedName("coord") public CoordRestModel coordinates;
    @SerializedName("weather") public WeatherRestModel[] weather;
    @SerializedName("base") public String base;
    @SerializedName("main") public MainRestModel main;
    @SerializedName("visibility") public int visibility;
    @SerializedName("wind") public WindRestModel wind;
    @SerializedName("rain") public  RainRestModel rain;
    @SerializedName("clouds") public  CloudsRestModel clouds;
    @SerializedName("snow") public  SnowRestModel snow;
    @SerializedName("dt") public long dt;
    @SerializedName("sys") public  SysRestModel sys;
    @SerializedName("id") public  long id;
    @SerializedName("name") public String name;
    @SerializedName("cod") public String cod;

    // for forecast 5 day respons
    @SerializedName("city") public CityRestModel cityInfo;
    @SerializedName("message") public String message;
    @SerializedName("cnt") public int cnt;
    @SerializedName("list") public ListRestModel[] list;


}
