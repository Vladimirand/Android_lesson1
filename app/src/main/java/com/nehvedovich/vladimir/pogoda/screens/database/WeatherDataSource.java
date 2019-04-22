package com.nehvedovich.vladimir.pogoda.screens.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;

//  Источник данных, позволяет изменять данные в таблице
// Создает и держит в себе читатель данных
public class WeatherDataSource implements Closeable {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private WeatherDataReader weatherDataReader;

    public WeatherDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Открывает базу данных
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        // Создать читателя и открыть его
        weatherDataReader = new WeatherDataReader(database);
        weatherDataReader.open();
    }

    // Закрыть базу данных
    public void close() {
        weatherDataReader.close();
        dbHelper.close();
    }

    // Добавить новую запись
    public void addNote(String title, String description, String weather_condition, String time) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_WEATHER, description);
        values.put(DatabaseHelper.COLUMN_WEATHER_TITLE, title);
        values.put(DatabaseHelper.COLUMN_WEATHER_WEATHER, weather_condition);
        values.put(DatabaseHelper.COLUMN_WEATHER_TIME, time);

        // Добавление записи
        long insertId = database.insert(DatabaseHelper.TABLE_WEATHER, null,
                values);
        Weather newWeather = new Weather();
        newWeather.description = description;
        newWeather.title = title;
        newWeather.id = insertId;
        newWeather.weatherCondition = weather_condition;
        newWeather.time = time;
    }

    // Удалить запись
    public void deleteNote(Weather weather) {
        long id = weather.id;
        database.delete(DatabaseHelper.TABLE_WEATHER, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }

    // Очистить таблицу
    public void deleteAll() {
        database.delete(DatabaseHelper.TABLE_WEATHER, null, null);
    }

    // Вернуть читателя (он потребуется в других местах)
    public WeatherDataReader getWeatherDataReader() {
        return weatherDataReader;
    }
}

