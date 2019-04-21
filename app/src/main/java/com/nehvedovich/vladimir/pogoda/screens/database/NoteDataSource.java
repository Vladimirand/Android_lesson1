package com.nehvedovich.vladimir.pogoda.screens.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;

//  Источник данных, позволяет изменять данные в таблице
// Создает и держит в себе читатель данных
public class NoteDataSource implements Closeable {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private NoteDataReader noteDataReader;

    public NoteDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Открывает базу данных
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        // Создать читателя и открыть его
        noteDataReader = new NoteDataReader(database);
        noteDataReader.open();
    }

    // Закрыть базу данных
    public void close() {
        noteDataReader.close();
        dbHelper.close();
    }

    // Добавить новую запись
    public void addNote(String title, String description, String weather_condition, String time) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NOTE, description);
        values.put(DatabaseHelper.COLUMN_NOTE_TITLE, title);
        values.put(DatabaseHelper.COLUMN_NOTE_WEATHER, weather_condition);
        values.put(DatabaseHelper.COLUMN_NOTE_TIME, time);

        // Добавление записи
        long insertId = database.insert(DatabaseHelper.TABLE_NOTES, null,
                values);
        Note newNote = new Note();
        newNote.description = description;
        newNote.title = title;
        newNote.id = insertId;
        newNote.weatherCondition = weather_condition;
        newNote.time = time;
    }

    // Удалить запись
    public void deleteNote(Note note) {
        long id = note.id;
        database.delete(DatabaseHelper.TABLE_NOTES, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }

    // Очистить таблицу
    public void deleteAll() {
        database.delete(DatabaseHelper.TABLE_NOTES, null, null);
    }

    // Вернуть читателя (он потребуется в других местах)
    public NoteDataReader getNoteDataReader() {
        return noteDataReader;
    }
}

