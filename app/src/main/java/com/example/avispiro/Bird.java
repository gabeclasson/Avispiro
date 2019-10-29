package com.example.avispiro;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;

/**
 * Represent the information of a bird.
 * Notes:
 * No object instance variables should ever be null. Use empty strings and/or empty constructors to fill invalid fields.
 * The category field should always be lower case with no white space on the outside.
 */
public class Bird{

    private String name, description, place;
    private Category category;
    private Time time;
    private int id;
    private String imageURI;

    public static Bitmap drawableToBitmap (Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Drawable bitmapToDrawable (Context context, Bitmap bitmap){
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public Bird(){
        name = "";
        description = "";
        place = "";
        category = new Category();
        time = new Time();
        imageURI = "";
        this.id = -1;
    }

    public Bird(String name, String description, String place, Category category, String imageURI, Time time){
        this.name = name;
        this.description = description;
        this.place = place;
        this.category = category;
        this.imageURI = imageURI;
        this.time = time;
        this.id = -1;
    }

    public Bird(String name, String description, String place, String imageURI, Time time){
        this.name = name;
        this.description = description;
        this.place = place;
        this.category = new Category();
        this.imageURI = imageURI;
        this.time = time;
        this.id = -1;
    }

    public Bird(String name, String description, String place, String category, String imageURI, Time time, int id){
        this.name = name;
        this.description = description;
        this.place = place;
        this.category = new Category();
        this.imageURI = imageURI;
        this.time = time;
        this.id = id;
    }

    public Bird(String name, String description, String place, Time time, String imageURI, int id){
        this.name = name;
        this.description = description;
        this.place = place;
        this.category = new Category();
        this.imageURI = imageURI;
        this.time = time;
        this.id = id;
    }


    public Bird(String name, String description, String place, Time time){
        this.name = name;
        this.description = description;
        this.place = place;
        this.category = new Category();
        this.time = time;
        this.id = -1;
    }

    public Bird(String name, String description, String place, String category, Time time, int id){
        this.name = name;
        this.description = description;
        this.place = place;
        this.category = new Category();
        this.time = time;
        this.id = id;
    }

    public Bird(String name, String description, String place, Time time, int id){
        this.name = name;
        this.description = description;
        this.place = place;
        this.category = new Category();
        this.time = time;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public Bitmap getImage(Context context) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(imageURI));
            return bitmap;
        }
        catch (Exception e) {
            return null;
        }
    }

    public String toString(){
        return name + " (" + "of category '" + category + "'): " + "\n" +
                "Description: " + description + "\n" +
                "Place seen: " + place + "\n" +
                "Time seen: " + time + "\n" +
                "Image: " + imageURI;
    }
}
