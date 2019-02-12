package com.nehvedovich.vladimir.pogoda.screens.utils;

import java.io.Serializable;

public class Parcel implements Serializable {

    private final String cityName;

    Parcel(int imageIndex, String cityName) {
        this.cityName = cityName;
    }

    String getCityName() {
        return cityName;
    }
}
