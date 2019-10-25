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
    private static final String TAG = "MyDatabaseHelperLog";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "birds.db";

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

    public static final String TABLE_CATEGORIES = "category_table";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_CATEGORY_NAME = "category_name";

    public static MyDatabaseHelper databaseHelper;

    public static MyDatabaseHelper getInstance(Context context){
        if (databaseHelper == null)
            databaseHelper = new MyDatabaseHelper(context, null, null, 0);
        return databaseHelper;
    }

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
                COLUMN_NAME + " TEXT, " +  COLUMN_DESCRIPTION + " TEXT, " + COLUMN_PLACE + " TEXT, " + COLUMN_CATEGORY + " INT, " + COLUMN_IMAGE + " BLOB, " + COLUMN_YEAR + " INTEGER, " + COLUMN_MONTH + " INTEGER, " + COLUMN_DATE + " INTEGER, " + COLUMN_HOUR + " INTEGER, " + COLUMN_MINUTE +  " INTEGER ) "+ ";";
        db.execSQL(query);
        String query2 = " CREATE TABLE " + TABLE_CATEGORIES + " ( " + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CATEGORY_NAME + " TEXT ) ;";
        db.execSQL(query2);
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, new Category().getName());
        db.insert(TABLE_CATEGORIES,null, values);
    }

    /**
     * Adds a bird to the data base.
     * @param bird The bird to add to the database
     * @return The id of the bird. Immediately after adding the bird to the database, the bird object's id should be set to this value using bird.setId().
     */
    public int addBird(Bird bird) {
        ContentValues values = new ContentValues();
        Time time = bird.getTime();
        values.put(COLUMN_NAME, bird.getName());
        values.put(COLUMN_DESCRIPTION, bird.getDescription());
        values.put(COLUMN_PLACE, bird.getPlace());
        values.put(COLUMN_CATEGORY, bird.getCategory().getId());
        values.put(COLUMN_IMAGE, bird.getImageAsBlob());
        values.put(COLUMN_YEAR, time.getYear());
        values.put(COLUMN_MONTH, time.getMonth());
        values.put(COLUMN_DATE, time.getDate());
        values.put(COLUMN_HOUR, time.getHour());
        values.put(COLUMN_MINUTE, time.getMinute());
        SQLiteDatabase db = getWritableDatabase();
        int id = (int) db.insert(TABLE_BIRDS, null, values);
        return id;
    }

    /**
     * Removes a bird from the database given an id.
     * @param id The id of the bird to remove. If this id is invalid, no bird will be removed.
     * @return The bird that was removed from the database. If a bird by that id could not be found, the method will return null.
     */
    public Bird removeBird(int id) {
        SQLiteDatabase db = getWritableDatabase();
        Bird bird = getBird(id);
        String query = "DELETE FROM " + TABLE_BIRDS + " WHERE " + COLUMN_ID+ " =\"" + id + "\";";
        db.execSQL(query);
        return bird;
    }

    public int addCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, category.getName());
        SQLiteDatabase db = getWritableDatabase();
        int id = (int) db.insert(TABLE_CATEGORIES,null, values);
        return id;
    }

    /**
     * Removes a given category and uncategorizes all birds with the given category.
     * @param id
     * @return
     */
    public Category removeCategory(int id) {
        SQLiteDatabase db = getWritableDatabase();
        Category category = getCategory(id);
        String query = "DELETE FROM " + TABLE_CATEGORIES + " WHERE " + COLUMN_ID + " =\"" + id + "\";";
        db.execSQL(query);
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY, 1);
        db.update(TABLE_BIRDS, values, COLUMN_CATEGORY + " = '" + id + "';", null);
        return category;
    }

    /**
     * Updates a category's name
     * @param category
     * @return
     */
    public Category updateCategory(Category category){
        if (category.getId() < 0)
            return null;
        Category out = getCategory(category.getId());
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, category.getId());
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_CATEGORIES, values, COLUMN_CATEGORY_ID + " = '" + category.getId() + "';", null);
        return out;
    }

    public Category getCategory(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CATEGORIES + " WHERE " + COLUMN_CATEGORY_ID + " = '" + id + "' ; ";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        if (!c.isAfterLast()){
            Category category = new Category();
            category.setName(c.getString(c.getColumnIndex(COLUMN_CATEGORY_NAME)));
            category.setId(c.getInt(c.getColumnIndex(COLUMN_CATEGORY_ID)));
            c.close();
            return category;
        }
        c.close();
        return null;
    }

    public Category[] getCategories() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CATEGORIES + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        Category[] out = new Category[c.getCount()];
        int index = 0;
        while(!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(COLUMN_CATEGORY_NAME)) != null) {
                out[index++] = new Category(c.getString(c.getColumnIndex(COLUMN_CATEGORY_NAME)), c.getInt(c.getColumnIndex(COLUMN_CATEGORY_ID)));
            }
            c.moveToNext();
        }
        c.close();
        return out;
    }

    /**
     * Returns a rudimentary representation of the database as a string
     * @return
     */
    public String databasetoString() {
        String dbstring = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_BIRDS + " WHERE ?";
        // This means to select all from the database

        // The cursor will extract the entries from the database
        Cursor c = db.rawQuery(query, new String[] {"1"});
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
        c.close();
        return dbstring;
    }

    /**
     * Fetches a bird from the database given an id.
     * @param id The id of the bird fetch.
     * @return The bird in the database with the given id. If the bird cannot be found, the return is null.
     */
    public Bird getBird(int id){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_BIRDS + " WHERE " + COLUMN_ID + " = '" + id + "' ; ";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        if (!c.isAfterLast()){
            Bird bird = new Bird();
            Time time = new Time();
            int categoryId;
            bird.setName(c.getString(c.getColumnIndex(COLUMN_NAME)));
            bird.setDescription(c.getString(c.getColumnIndex(COLUMN_DESCRIPTION)));
            bird.setPlace(c.getString(c.getColumnIndex(COLUMN_PLACE)));
            categoryId = c.getInt(c.getColumnIndex(COLUMN_CATEGORY));
            bird.setCategory(getCategory(categoryId));
            bird.setImageAsBlob(c.getBlob(c.getColumnIndex(COLUMN_IMAGE)));
            time.setYear(c.getInt(c.getColumnIndex(COLUMN_YEAR)));
            time.setMonth(c.getInt(c.getColumnIndex(COLUMN_MONTH)));
            time.setDate(c.getInt(c.getColumnIndex(COLUMN_DATE)));
            time.setHour(c.getInt(c.getColumnIndex(COLUMN_HOUR)));
            time.setMinute(c.getInt(c.getColumnIndex(COLUMN_MINUTE)));
            bird.setTime(time);
            bird.setId(id);
            c.close();
            return bird;
        }
        c.close();
        return null;
    }

    public Bird[] getAllBirds(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_BIRDS + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        Bird[] out = new Bird[c.getCount()];
        int index = 0;
        while(!c.isAfterLast()) {
            if(c.getString(c.getColumnIndex(COLUMN_NAME)) != null ) {
                Bird bird = new Bird();
                Time time = new Time();
                int categoryId;
                bird.setName(c.getString(c.getColumnIndex(COLUMN_NAME)));
                bird.setDescription(c.getString(c.getColumnIndex(COLUMN_DESCRIPTION)));
                bird.setPlace(c.getString(c.getColumnIndex(COLUMN_PLACE)));
                categoryId = c.getInt(c.getColumnIndex(COLUMN_CATEGORY));
                bird.setCategory(getCategory(categoryId));
                bird.setImageAsBlob(c.getBlob(c.getColumnIndex(COLUMN_IMAGE)));
                time.setYear(c.getInt(c.getColumnIndex(COLUMN_YEAR)));
                time.setMonth(c.getInt(c.getColumnIndex(COLUMN_MONTH)));
                time.setDate(c.getInt(c.getColumnIndex(COLUMN_DATE)));
                time.setHour(c.getInt(c.getColumnIndex(COLUMN_HOUR)));
                time.setMinute(c.getInt(c.getColumnIndex(COLUMN_MINUTE)));
                bird.setTime(time);
                bird.setId(c.getInt(c.getColumnIndex(COLUMN_ID)));
                out[index++] = bird;
            }
            c.moveToNext();
        }
        c.close();
        return out;
    }

    public Bird updateBird(Bird bird){
        if (bird.getId() < 0)
            return null;
        Bird returned = getBird(bird.getId());
        if (returned == null)
            return null;
        ContentValues values = new ContentValues();
        Time time = bird.getTime();
        values.put(COLUMN_NAME, bird.getName());
        values.put(COLUMN_DESCRIPTION, bird.getDescription());
        values.put(COLUMN_PLACE, bird.getPlace());
        values.put(COLUMN_CATEGORY, bird.getCategory().getId());
        values.put(COLUMN_IMAGE, bird.getImageAsBlob());
        values.put(COLUMN_YEAR, time.getYear());
        values.put(COLUMN_MONTH, time.getMonth());
        values.put(COLUMN_DATE, time.getDate());
        values.put(COLUMN_HOUR, time.getHour());
        values.put(COLUMN_MINUTE, time.getMinute());
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_BIRDS, values,COLUMN_ID + " = '" + bird.getId() + "';", null);
        return returned;
    }

    public void deleteAllBirds(String areYouSure){
        if (areYouSure.equalsIgnoreCase("yes"));{
            SQLiteDatabase db = getWritableDatabase();
            String query = "DELETE FROM " + TABLE_BIRDS + " WHERE 1;";
            db.execSQL(query);
        }
    }
}

