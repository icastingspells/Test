package com.example.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database.db";
    private static final int SCHEME = 1;
    public static final String TABLE_PROFILE = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AGE = "age";


    public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, SCHEME);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_PROFILE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + COLUMN_NAME + " TEXT, " + COLUMN_AGE + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
        onCreate(sqLiteDatabase);
    }
}
