package com.example.avispiro;

public class Bird {

    private String name, description, place, category;
    private Time time;
    private int id;

    public Bird(){
        name = "Bird";
        description = "bird";
        place = "Nowhere";
        category = null;
        time = new Time();
        this.id = -1;
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

    public String toString(){
        return name + " (" + "of category '" + category + "'): " + "\n" +
                "Description: " + description + "\n" +
                "Place seen: " + place + "\n" +
                "Time seen: " + time;
    }
}
