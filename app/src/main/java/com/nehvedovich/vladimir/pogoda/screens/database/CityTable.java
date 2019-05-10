package com.nehvedovich.vladimir.pogoda.screens.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

class CityTable {

    private final static String TABLE_NAME = "Cities";
    private final static String COLUMN_ID = "id";
    private final static String COLUMN_CITY_NAME = "name";

    static void createTable(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CITY_NAME + " TEXT);");
        String[] cities = {"Минск", "Гродно", "Брест", "Гомель", "Могилев", "Витебск"};
        for (String cityName : cities) {
            addCity(new City(null, cityName), database);
        }
    }

    static void addCity(City city, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CITY_NAME, city.getName());
        database.insert(TABLE_NAME, null, values);
    }

    static List<City> getAllCities(SQLiteDatabase database) {
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, COLUMN_CITY_NAME);
        return getResultFromCursor(cursor);
    }

    static City getCityById(Long cityId, SQLiteDatabase database) {
        String[] args = {String.valueOf(cityId)};
        Cursor cursor = database.query(TABLE_NAME, null, "id = ?", args, null, null, COLUMN_CITY_NAME);
        List<City> list = getResultFromCursor(cursor);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    static void deleteCity(Long cityId, SQLiteDatabase database) {
        database.delete(TABLE_NAME, COLUMN_ID + " = " + cityId, null);
    }

    static void onUpgrade(SQLiteDatabase database) {
        database.execSQL("CREATE INDEX IF NOT EXISTS cityNameIdx on  " + TABLE_NAME + "(" + COLUMN_CITY_NAME + ")");
    }

    private static List<City> getResultFromCursor(Cursor cursor) {
        List<City> result = null;
        if (cursor != null && cursor.moveToFirst()) {
            result = new ArrayList<>(cursor.getCount());
            int nameIdx = cursor.getColumnIndex(COLUMN_CITY_NAME);
            int idIdx = cursor.getColumnIndex(COLUMN_ID);
            do {
                Long id = cursor.getLong(idIdx);
                String name = cursor.getString(nameIdx);
                City city = new City(id, name);
                result.add(city);
            } while (cursor.moveToNext());
        }
        try {
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception ignored) {
        }
        return result == null ? new ArrayList<City>(0) : result;
    }
}