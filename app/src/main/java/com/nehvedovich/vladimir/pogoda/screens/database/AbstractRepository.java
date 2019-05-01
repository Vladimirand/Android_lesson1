package com.nehvedovich.vladimir.pogoda.screens.database;

import android.database.sqlite.SQLiteDatabase;


abstract class AbstractRepository<T> implements IRepository<T> {

    public SQLiteDatabase getDatabase() {
        DataBaseProvider dataBaseProvider = DataBaseProvider.getInstance();
        return dataBaseProvider.getWritableDatabase();
    }
}
