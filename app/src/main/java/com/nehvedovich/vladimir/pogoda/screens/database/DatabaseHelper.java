package com.nehvedovich.vladimir.pogoda.screens.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Класс установки базы данных: создать базу данных, если ее нет; проапгрейдить ее
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notes.db"; // Название БД
    private static final int DATABASE_VERSION = 4; // Версия базы данных
    static final String TABLE_NOTES = "notes"; // Название таблицы в БД
    // Названия столбцов
    static final String COLUMN_ID = "_id";
    static final String COLUMN_NOTE = "note";
    static final String COLUMN_NOTE_TITLE = "title";
    static final String COLUMN_NOTE_WEATHER = "weather_condition";
    static final String COLUMN_NOTE_TIME = "time";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Вызывается при попытке доступа к базе данных, когда она еще не создана
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NOTES + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NOTE + " TEXT,"
                + COLUMN_NOTE_TITLE + " TEXT," + COLUMN_NOTE_WEATHER + " TEXT," + COLUMN_NOTE_TIME + " TEXT);");
    }

    // Вызывается, когда необходимо обновление базы данных
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        if ((oldVersion == 3) && (newVersion == 4)) {
            String upgradeQuery = "ALTER TABLE " + TABLE_NOTES + " ADD COLUMN " + COLUMN_NOTE_TIME +
                    " TEXT DEFAULT ''";
            db.execSQL(upgradeQuery);
        }
    }
}



