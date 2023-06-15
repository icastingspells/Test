package com.example.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DatabaseAdapter {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    public DatabaseAdapter(Context context){
        this.databaseHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public DatabaseAdapter open(){
        database = databaseHelper.getReadableDatabase();
        return DatabaseAdapter.this;
    }

    public void close(){databaseHelper.close();}

    public void insert(Profile profile){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, profile.name);
        cv.put(DatabaseHelper.COLUMN_AGE, profile.age);
        database.insert(DatabaseHelper.TABLE_PROFILE, null, cv);
    }
    public void delete(long id){
        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        database.delete(DatabaseHelper.TABLE_PROFILE, whereClause, whereArgs);
    }
    public void update(Profile profile) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, profile.name);
        cv.put(DatabaseHelper.COLUMN_AGE, profile.age);
        database.update(DatabaseHelper.TABLE_PROFILE, cv, "_id = ?", new String[]{String.valueOf(profile._id)});
    }
    public ArrayList<Profile> profiles(){
        ArrayList<Profile> profiles = new ArrayList<Profile>();
        Cursor cursor = getProfileEntries();
        while (cursor.moveToNext()){
            Profile profile = new Profile(
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AGE))
            );
            profiles.add(profile);
        }
        return profiles;
    }
    public Profile getSingleProfile(long id){
        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_PROFILE + " WHERE " + DatabaseHelper.COLUMN_ID + "=?" , new String[]{String.valueOf(id)});
        cursor.moveToFirst();

        return new Profile(
            cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)),
            cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AGE))
        );
    }
    private Cursor getProfileEntries(){
        String[] columns = new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_AGE, DatabaseHelper.COLUMN_AGE};
        return database.query(DatabaseHelper.TABLE_PROFILE, columns, null, null, null, null, null);
    }
}
