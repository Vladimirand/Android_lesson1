package com.nehvedovich.vladimir.pogoda.screens.database;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class City implements Parcelable {

    private Long id;

    private String name;

    public City(@Nullable Long id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }

    private City(Parcel source) {
        id = source.readLong();
        name = source.readString();
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {

        @Override
        public City createFromParcel(Parcel source) {
            return new City(source);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };
}
