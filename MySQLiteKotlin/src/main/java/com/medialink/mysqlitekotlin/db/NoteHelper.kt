package com.medialink.mysqlitekotlin.db

import android.content.Context
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

}