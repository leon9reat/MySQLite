package com.medialink.mysqlite;

import android.provider.BaseColumns;

public class DatabaseContract {
    public static String TABLE_NOTE = "TABLE_NOTE";

    public static final class NoteColumns implements BaseColumns {
        public static String TITLE = "TITLE";
        public static String DESCRIPTION = "DESCRIPTION";
        public static String DATE = "DATE";
    }
}
