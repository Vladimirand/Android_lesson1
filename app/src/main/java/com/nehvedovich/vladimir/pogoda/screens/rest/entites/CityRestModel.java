package com.nehvedovich.vladimir.pogoda.screens.rest.entites;

import com.google.gson.annotations.SerializedName;

public class CityRestModel {
    @SerializedName("id") public int id;
    @SerializedName("name") public String name;
    @SerializedName("coord") public CoordRestModel coordinates;
    @SerializedName("country") public String country;
    @SerializedName("population") public int population;
    @SerializedName("timezone") public String timezone;
}
