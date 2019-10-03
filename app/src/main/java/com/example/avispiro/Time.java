package com.example.avispiro;

public class Time {
    private int year, month, date, hour, minute;

    public Time(int year, int month, int date, int hour, int minute){
        this.year = year;
        this.month = month;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
    }

    public Time (int year){
        this.year = year;
        this.month = -1;
        this.date = -1;
        this.hour = -1;
        this.minute = -1;
    }

    public Time (int year, int month){
        this.year = year;
        this.month = month;
        this.date = -1;
        this.hour = -1;
        this.minute = -1;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public Time (int year, int month, int date){
        this.year = year;
        this.month = month;
        this.date = date;
        this.hour = -1;
        this.minute = -1;
    }

    public Time (int year, int month, int date, int hour){
        this.year = year;
        this.month = month;
        this.date = date;
        this.hour = hour;
        this.minute = -1;
    }

    public Time(){
        this.year = -1;
        this.month = -1;
        this.date = -1;
        this.hour = -1;
        this.minute = -1;
    }

    public String toString(){
        if (year < 0)
            return null;
        String out = "";
        out += year;
        if (month < 0)
            return out;
        out += "-" + month;
        if (date < 0)
            return out;
        out += "-" + date;
        if (hour < 0)
            return out;
        out += " " + hour;
        if (minute < 0)
            return out + ":00";
        out += ":" + minute;
        return out;
    }

}
