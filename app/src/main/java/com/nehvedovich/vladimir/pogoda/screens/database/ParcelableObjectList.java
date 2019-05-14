package com.nehvedovich.vladimir.pogoda.screens.database;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ParcelableObjectList<T> implements Parcelable {

    private List<T> objectList;

    @Override
    public int describeContents() {
        return 0;
    }

    private ParcelableObjectList(Parcel source) {
        objectList = new ArrayList<>();
        source.readList(objectList, ParcelableObjectList.class.getClassLoader());
    }

    ParcelableObjectList(List<T> list) {
        this.objectList = list;
    }

    public List<T> getObjectList() {
        return objectList;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(objectList);
    }

    public static final Parcelable.Creator<ParcelableObjectList> CREATOR = new Parcelable.Creator<ParcelableObjectList>() {

        @Override
        public ParcelableObjectList createFromParcel(Parcel source) {
            return new ParcelableObjectList(source);
        }

        @Override
        public ParcelableObjectList[] newArray(int size) {
            return new ParcelableObjectList[size];
        }
    };
}
