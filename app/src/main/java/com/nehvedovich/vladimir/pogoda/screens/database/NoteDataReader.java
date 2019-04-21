package com.nehvedovich.vladimir.pogoda.screens.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;

// Читатель источника данных на основе курсора
// Этот класс был вынесен из NoteDataSource, чтобы разгрузить его ответственности
public class NoteDataReader implements Closeable {

    private Cursor cursor;              // Курсор (фактически, подготовленный запрос),
    // но сами данные подсчитываются только по необходимости
    private SQLiteDatabase database;

    private String[] notesAllColumn = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_NOTE,
            DatabaseHelper.COLUMN_NOTE_TITLE,
            DatabaseHelper.COLUMN_NOTE_WEATHER,
            DatabaseHelper.COLUMN_NOTE_TIME,
    };

    NoteDataReader(SQLiteDatabase database){
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
        cursor = database.query(DatabaseHelper.TABLE_NOTES,
                notesAllColumn, "_id", null, null, null, null);
    }

    // Прочитать данные по определенной позиции
    Note getPosition(int position){
        cursor.moveToPosition(position);
        return cursorToNote();
    }

    // Получить количество строк в таблице
    int getCount(){
        return cursor.getCount();
    }

    // Преобразователь данных курсора в объект
    private Note cursorToNote() {
        Note note = new Note();
        note.id = cursor.getLong(0);
        note.description = cursor.getString(1);
        note.title = cursor.getString(2);
        note.weatherCondition = cursor.getString(3);
        note.time = cursor.getString(4);
        return note;
    }
}
