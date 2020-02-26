package com.example.credhub;

import android.provider.BaseColumns;

public class Database {
    private Database() {}

    public static class DatabaseEntry implements BaseColumns {
        public static final String TABLE_NAME = "credentials";
        public static final String COLUMN_NAME_1 = "service";
        public static final String COLUMN_NAME_2 = "user";
        public static final String COLUMN_NAME_3 = "password";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DatabaseEntry.TABLE_NAME + " (" +
            DatabaseEntry._ID + " INTEGER PRIMARY KEY," +
            DatabaseEntry.COLUMN_NAME_1 + " TEXT," +
            DatabaseEntry.COLUMN_NAME_2 + " TEXT," +
            DatabaseEntry.COLUMN_NAME_3 + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseEntry.TABLE_NAME;
}
