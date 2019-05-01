package com.nehvedovich.vladimir.pogoda.screens.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.nehvedovich.vladimir.pogoda.screens.database.City;

public class WData implements Parcelable {

    private City city;

    public WData(City city) {
        this.city = city;
    }

    private WData(Parcel in) {
        boolean[] data = new boolean[3];
        in.readBooleanArray(data);

        city = in.readParcelable(WData.class.getClassLoader());
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(city, flags);
    }

    public static final Parcelable.Creator<WData> CREATOR = new Parcelable.Creator<WData>() {

        @Override
        public WData createFromParcel(Parcel source) {
            return new WData(source);
        }

        @Override
        public WData[] newArray(int size) {
            return new WData[size];
        }
    };

}
