package com.medialink.mysqlite.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.medialink.mysqlite.DatabaseContract;
import com.medialink.mysqlite.DatabaseHelper;

import static android.provider.BaseColumns._ID;

public class NoteHelper {
    private static final String TABLE_NOTE = DatabaseContract.TABLE_NOTE;
    private static DatabaseHelper dbHelper;
    private static NoteHelper INSTANCE;
    private static SQLiteDatabase database;

    public NoteHelper(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public static NoteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NoteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dbHelper.getReadableDatabase();
    }

    public void close() {
        dbHelper.close();
        if (database.isOpen()) {
            database.close();
        }
    }

    public Cursor queryAll() {
        return database.query(
                TABLE_NOTE,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC"
        );
    }

    public Cursor queryById(String id) {
        return database.query(
                TABLE_NOTE,
                null,
                _ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null
        );
    }

    public long insert(ContentValues values) {
        return database.insert(
                TABLE_NOTE,
                null,
                values
        );
    }

    public int update(String id, ContentValues values) {
        return database.update(
                TABLE_NOTE,
                values,
                _ID + " = ?",
                new String[]{id}
        );
    }

    public int deleteById(String id) {
        return database.delete(
                TABLE_NOTE,
                _ID + " = ?",
                new String[]{id}
        );
    }
}
