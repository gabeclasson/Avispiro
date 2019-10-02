package com.example.avispiro;

public class Bird {
    public static int idGen = 0;

    private String name, description, place, category;
    private Time time;
    private int id;

    public Bird(){
        name = "Bird";
        description = "bird";
        place = "Nowhere";
        category = null;
        time = new Time();
        id = idGen++;
    }

    public Bird(String name, String description, String place, String category, Time time){
        this.name = name;
        this.description = description;
        this.place = place;
        this.category = category;
        this.time = time;
        id = idGen++;
    }

    public Bird(String name, String description, String place, Time time){
        this.name = name;
        this.description = description;
        this.place = place;
        this.category = null;
        this.time = time;
        id = idGen++;
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

    public static int getIdGen() {
        return idGen;
    }

    public static void setIdGen(int idGen) {
        Bird.idGen = idGen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
