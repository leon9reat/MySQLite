package com.medialink.mysqlitekotlin.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase

class NoteHelper(context: Context) {
    companion object {
        private const val TABLE_NOTE = DatabaseContract.NoteColumns.TABLE_NOTE
        private lateinit var dbHelper: DatabaseHelper
        private var INSTANCE: NoteHelper? = null

        private lateinit var database: SQLiteDatabase

        fun getInstance(context: Context): NoteHelper =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: NoteHelper(context)
                }


    }

    init {
        dbHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dbHelper.writableDatabase
    }

    fun close() {
        dbHelper.close()
        if (database.isOpen) {
            database.close()
        }

    }

    fun queryAll(): Cursor {
        return database.query(
                TABLE_NOTE,
                null,
                null,
                null,
                null,
                null,
                "${DatabaseContract.NoteColumns._ID} ASC"
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
                TABLE_NOTE,
                null,
                "${DatabaseContract.NoteColumns._ID} = ?",
                arrayOf(id),
                null,
                null,
                null,
                null
        )

    }

    fun insert(values: ContentValues?): Long {
        return database.insert(TABLE_NOTE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(
                TABLE_NOTE,
                values,
                "${DatabaseContract.NoteColumns._ID} = ?",
                arrayOf(id)
        )
    }

    fun deleteById(id: String): Int {
        return database.delete(
                TABLE_NOTE,
                "${DatabaseContract.NoteColumns._ID} = '$id'",
                null
        )
    }

}