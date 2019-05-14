package com.nehvedovich.vladimir.pogoda.screens.rest.entites;

import com.google.gson.annotations.SerializedName;

public class WindRestModel {
    @SerializedName("speed") public float speed;
    @SerializedName("deg") public float deg;
}
