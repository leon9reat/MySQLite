package com.medialink.mysqlitekotlin.db

import android.provider.BaseColumns

class DatabaseContract {
    class NoteColumns : BaseColumns {
        companion object {
            const val TABLE_NOTE = "TABLE_NOTE"
            const val _ID ="_id"
            const val TITLE = "TITLE"
            const val DESCRIPTION = "DESCRIPTION"
            const val DATE = "DATE"
        }
    }
}