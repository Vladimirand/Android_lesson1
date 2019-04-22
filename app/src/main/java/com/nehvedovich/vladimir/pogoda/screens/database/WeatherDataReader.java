package com.nehvedovich.vladimir.pogoda.screens.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;

// Читатель источника данных на основе курсора
// Этот класс был вынесен из WeatherDataSource, чтобы разгрузить его ответственности
public class WeatherDataReader implements Closeable {

    private Cursor cursor;              // Курсор (фактически, подготовленный запрос),
    // но сами данные подсчитываются только по необходимости
    private SQLiteDatabase database;

    private String[] notesAllColumn = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_WEATHER,
            DatabaseHelper.COLUMN_WEATHER_TITLE,
            DatabaseHelper.COLUMN_WEATHER_WEATHER,
            DatabaseHelper.COLUMN_WEATHER_TIME,
    };

    WeatherDataReader(SQLiteDatabase database){
        this.database = database;
    }

    // Подготовить к чтению таблицу
    void open(){
        query();
        cursor.moveToFirst();
    }

    public void close(){
        cursor.close();
    }

    // Перечитать таблицу (если точно – обновить курсор)
    public void Refresh(){
        int position = cursor.getPosition();
        query();
        cursor.moveToPosition(position);
    }

    // Создание запроса на курсор
    private void query(){
        cursor = database.query(DatabaseHelper.TABLE_WEATHER,
                notesAllColumn, "_id", null, null, null, null);
    }

    // Прочитать данные по определенной позиции
    Weather getPosition(int position){
        cursor.moveToPosition(position);
        return cursorToNote();
    }

    // Получить количество строк в таблице
    int getCount(){
        return cursor.getCount();
    }

    // Преобразователь данных курсора в объект
    private Weather cursorToNote() {
        Weather weather = new Weather();
        weather.id = cursor.getLong(0);
        weather.description = cursor.getString(1);
        weather.title = cursor.getString(2);
        weather.weatherCondition = cursor.getString(3);
        weather.time = cursor.getString(4);
        return weather;
    }
}
