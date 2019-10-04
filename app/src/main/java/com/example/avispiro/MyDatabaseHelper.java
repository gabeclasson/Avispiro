package com.example.avispiro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_MONTH = "month";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_HOUR = "hour";
    public static final String COLUMN_MINUTE = "minute";

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
                COLUMN_NAME + " TEXT, " +  COLUMN_DESCRIPTION + " TEXT, " + COLUMN_PLACE + " TEXT, " + COLUMN_CATEGORY + " TEXT, " + COLUMN_IMAGE + " BLOB, " + COLUMN_YEAR + " INTEGER, " + COLUMN_MONTH + " INTEGER, " + COLUMN_DATE + " INTEGER, " + COLUMN_HOUR + " INTEGER, " + COLUMN_MINUTE +  " INTEGER ) "+ ";";
        db.execSQL(query);

    }

    public int addBird(Bird bird) {
        ContentValues values = new ContentValues();
        Time time = bird.getTime();
        values.put(COLUMN_NAME, bird.getName());
        values.put(COLUMN_DESCRIPTION, bird.getDescription());
        values.put(COLUMN_PLACE, bird.getPlace());
        values.put(COLUMN_CATEGORY, bird.getCategory());
        values.put(COLUMN_IMAGE, bird.getImageAsBlob());
        values.put(COLUMN_YEAR, time.getYear());
        values.put(COLUMN_MONTH, time.getMonth());
        values.put(COLUMN_DATE, time.getDate());
        values.put(COLUMN_HOUR, time.getHour());
        values.put(COLUMN_MINUTE, time.getMinute());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_BIRDS, null, values);
        // USE THIS TO FIX https://stackoverflow.com/questions/27764486/java-sqlite-last-insert-rowid-return-0
        String query = "SELECT MAX(" + COLUMN_ID +") AS LAST FROM " + TABLE_BIRDS;
        PreparedStatement pst1 = db.prepareStatement(query);
        ResultSet rs1 = pst1.executeQuery();
        String maxId=  rs1.getString("LAST");
        //Max Table Id Convert to Integer and +1
        int intMaxId =(Integer.parseInt(maxId))+1;
        //Convert to String
        String stringMaxId = Integer.toString(intMaxId);
        tUazon.setText(stringMaxId);
        pst1.execute();

        //JOptionPane.showMessageDialog(null, "Adat elmentve");

        pst1.close();
        rs1.close();
        db.close();
        return id;
    }

    public void removeBird(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_BIRDS + " WHERE " + COLUMN_ID+ " =\"" + id + "\";";
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
                dbstring += c.getBlob(c.getColumnIndex(COLUMN_IMAGE)) + ", ";
                dbstring += c.getInt(c.getColumnIndex(COLUMN_YEAR)) + ", ";
                dbstring += c.getInt(c.getColumnIndex(COLUMN_MONTH)) + ", ";
                dbstring += c.getInt(c.getColumnIndex(COLUMN_DATE)) + ", ";
                dbstring += c.getInt(c.getColumnIndex(COLUMN_HOUR)) + ", ";
                dbstring += c.getInt(c.getColumnIndex(COLUMN_MINUTE)) + ", ";
                dbstring += "\n";
            }
            c.moveToNext();
        }

        db.close();
        return dbstring;

    }

    public Bird getBird(int id){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_BIRDS + " WHERE " + COLUMN_ID + " = '" + id + "' ; ";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        if (!c.isAfterLast()){
            Bird bird = new Bird();
            Time time = new Time();
            bird.setName(c.getString(c.getColumnIndex(COLUMN_NAME)));
            bird.setDescription(c.getString(c.getColumnIndex(COLUMN_DESCRIPTION)));
            bird.setPlace(c.getString(c.getColumnIndex(COLUMN_PLACE)));
            bird.setCategory(c.getString(c.getColumnIndex(COLUMN_CATEGORY)));
            bird.setImageAsBlob(c.getBlob(c.getColumnIndex(COLUMN_IMAGE)));
            time.setYear(c.getInt(c.getColumnIndex(COLUMN_YEAR)));
            time.setMonth(c.getInt(c.getColumnIndex(COLUMN_MONTH)));
            time.setDate(c.getInt(c.getColumnIndex(COLUMN_DATE)));
            time.setHour(c.getInt(c.getColumnIndex(COLUMN_HOUR)));
            time.setMinute(c.getInt(c.getColumnIndex(COLUMN_MINUTE)));
            bird.setTime(time);
            bird.setId(id);
            return bird;
        }
        return null;
    }
}

