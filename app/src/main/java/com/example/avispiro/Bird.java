package com.example.avispiro;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;

public class Bird {

    private String name, description, place, category;
    private Time time;
    private int id;
    private Bitmap image;

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
        name = "Bird";
        description = "bird";
        place = "Nowhere";
        category = null;
        time = new Time();
        this.id = -1;
    }

    public Bird(String name, String description, String place, String category, Bitmap image, Time time){
        this.name = name;
        this.description = description;
        this.place = place;
        this.category = category;
        this.image = image;
        this.time = time;
        this.id = -1;
    }

    public Bird(String name, String description, String place, Bitmap image, Time time){
        this.name = name;
        this.description = description;
        this.place = place;
        this.category = null;
        this.image = image;
        this.time = time;
        this.id = -1;
    }

    public Bird(String name, String description, String place, String category, Bitmap image, Time time, int id){
        this.name = name;
        this.description = description;
        this.place = place;
        this.category = category;
        this.image = image;
        this.time = time;
        this.id = id;
    }

    public Bird(String name, String description, String place, Time time, Bitmap image, int id){
        this.name = name;
        this.description = description;
        this.place = place;
        this.category = null;
        this.image = image;
        this.time = time;
        this.id = id;
    }

    public Bird(String name, String description, String place, String category, Time time){
        this.name = name;
        this.description = description;
        this.place = place;
        this.category = category;
        this.time = time;
        this.id = -1;
    }

    public Bird(String name, String description, String place, Time time){
        this.name = name;
        this.description = description;
        this.place = place;
        this.category = null;
        this.time = time;
        this.id = -1;
    }

    public Bird(String name, String description, String place, String category, Time time, int id){
        this.name = name;
        this.description = description;
        this.place = place;
        this.category = category;
        this.time = time;
        this.id = id;
    }

    public Bird(String name, String description, String place, Time time, int id){
        this.name = name;
        this.description = description;
        this.place = place;
        this.category = null;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    // adapted from https://stackoverflow.com/questions/9357668/how-to-store-image-in-sqlite-database
    public byte[] getImageAsBlob(){
        if (image == null)
            return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public void setImageAsBlob(byte[] blob){
        if (blob == null || blob.length == 0) {
            image = null;
            return;
        }
        image = BitmapFactory.decodeByteArray(blob, 0, blob.length);
    }

    public String toString(){
        return name + " (" + "of category '" + category + "'): " + "\n" +
                "Description: " + description + "\n" +
                "Place seen: " + place + "\n" +
                "Time seen: " + time + "\n" +
                "Image: " + image;
    }
}
