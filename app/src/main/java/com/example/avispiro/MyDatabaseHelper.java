package com.example.avispiro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "MyDataBaseHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "scores.db";

    public static final String TABLE_BIRDS = "birds";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PLACE = "place";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_MONTH = "month";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_HOUR = "hour";
    public static final String COLUMN_MINUTE = "minute";
    public static final String COLUMN_AVISID = "avisid";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + TABLE_BIRDS;
        db.execSQL(query);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = " CREATE TABLE " + TABLE_BIRDS + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +  COLUMN_DESCRIPTION + " TEXT, " + COLUMN_PLACE + " TEXT, " + COLUMN_CATEGORY + " TEXT, " + COLUMN_YEAR + " INTEGER, " + COLUMN_MONTH + " INTEGER, " + COLUMN_DATE + " INTEGER, " + COLUMN_HOUR + " INTEGER, " + COLUMN_MINUTE + " INTEGER, " + COLUMN_AVISID + " INTEGER ) "+ ";";
        db.execSQL(query);
    }

    public void addBird(Bird bird) {
        ContentValues values = new ContentValues();
        Time time = bird.getTime();
        values.put(COLUMN_NAME, bird.getName());
        values.put(COLUMN_DESCRIPTION, bird.getDescription());
        values.put(COLUMN_PLACE, bird.getPlace());
        values.put(COLUMN_CATEGORY, bird.getCategory());
        values.put(COLUMN_YEAR, time.getYear());
        values.put(COLUMN_MONTH, time.getMonth());
        values.put(COLUMN_DATE, time.getDate());
        values.put(COLUMN_HOUR, time.getHour());
        values.put(COLUMN_MINUTE, time.getMinute());
        values.put(COLUMN_AVISID, bird.getId());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_BIRDS, null, values);

        db.close();
    }

    public void removeBird(int avisId) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_BIRDS + " WHERE " + COLUMN_AVISID+ " =\"" + avisId + "\";";
        db.execSQL(query);

    }

    // This method creates a String representation of all the database elements
    // this is simply for quick viewing of our database contents

    public String databasetoString() {
        String dbstring = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_BIRDS + " WHERE 1";
        // This means to select all from the database

        // The cursor will extract the entries from the database
        Cursor c = db.rawQuery(query, null);

        // Move the cursor to the first position and then move through the db to the last
        c.moveToFirst();
        while(!c.isAfterLast()) {
            if(c.getString(c.getColumnIndex(COLUMN_NAME)) != null ) {
                dbstring += c.getString(c.getColumnIndex(COLUMN_NAME)) + ", ";
                dbstring += c.getString(c.getColumnIndex(COLUMN_DESCRIPTION)) + ", ";
                dbstring += c.getString(c.getColumnIndex(COLUMN_PLACE)) + ", ";
                dbstring += c.getString(c.getColumnIndex(COLUMN_CATEGORY)) + ", ";
                dbstring += c.getInt(c.getColumnIndex(COLUMN_YEAR)) + ", ";
                dbstring += c.getInt(c.getColumnIndex(COLUMN_MONTH)) + ", ";
                dbstring += c.getInt(c.getColumnIndex(COLUMN_DATE)) + ", ";
                dbstring += c.getInt(c.getColumnIndex(COLUMN_HOUR)) + ", ";
                dbstring += c.getInt(c.getColumnIndex(COLUMN_MINUTE)) + ", ";
                dbstring += c.getInt(c.getColumnIndex(COLUMN_AVISID)) + ", ";
                dbstring += "\n";
            }
            c.moveToNext();
        }

        db.close();
        return dbstring;

    }

    public Bird getBird(int avisId){

    }
}

