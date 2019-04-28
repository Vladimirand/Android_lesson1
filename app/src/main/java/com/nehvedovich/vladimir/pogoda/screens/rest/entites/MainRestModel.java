package com.nehvedovich.vladimir.pogoda.screens.rest.entites;

import com.google.gson.annotations.SerializedName;

public class MainRestModel {
    @SerializedName("temp") public float temp;
    @SerializedName("pressure") public float pressure;
    @SerializedName("humidity") public float humidity;
    @SerializedName("temp_min") public float tempMin;
    @SerializedName("temp_max") public float tempMax;
    @SerializedName("sea_level") public float seaLevel;
    @SerializedName("grnd_level") public float grndLevel;
}
