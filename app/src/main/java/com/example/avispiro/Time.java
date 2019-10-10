package com.example.avispiro;

/**
 * Represents a point in time, with varing levels of specificity. All integer fields are human-normalized. 1 AM, 1 AD, 1 minute, and January are all represented by the number 1.
 */
public class Time {
    private int year, month, date, hour, minute;

    /**
     * Creates a time object with accuracy to the nearest minute
     * @param year
     * @param month
     * @param date
     * @param hour
     * @param minute
     */
    public Time(int year, int month, int date, int hour, int minute){
        this.year = year;
        this.month = month;
        this.date = date;
        this.hour = hour;
        this.minute = minute;

    }

    /**
     * Creates a time object with accuracy to the nearest year
     * @param year
     */
    public Time (int year){
        this.year = year;
        this.month = -1;
        this.date = -1;
        this.hour = -1;
        this.minute = -1;
    }

    /**
     * Creates a time object with accuracy to the nearest month
     * @param year
     * @param month
     */
    public Time (int year, int month){
        this.year = year;
        this.month = month;
        this.date = -1;
        this.hour = -1;
        this.minute = -1;
    }

    /**
     * Creates a time object with accuracy to the nearest day
     * @param year
     * @param month
     * @param date
     */
    public Time (int year, int month, int date){
        this.year = year;
        this.month = month;
        this.date = date;
        this.hour = -1;
        this.minute = -1;
    }

    /**
     * Creates a time object with accuracy to the nearest hour
     * @param year
     * @param month
     * @param date
     * @param hour
     */
    public Time (int year, int month, int date, int hour){
        this.year = year;
        this.month = month;
        this.date = date;
        this.hour = hour;
        this.minute = -1;
    }

    /**
     * Cteates a time object with no accuracy.
     */
    public Time(){
        this.year = -1;
        this.month = -1;
        this.date = -1;
        this.hour = -1;
        this.minute = -1;
    }

    /**
     * Returns the date in ISO format: YYYY-MM-DD
     * @return
     */
    public String toString(){
        if (year < 0)
            return null;
        String out = "";
        out += formatInt(year,4);
        if (month < 0)
            return out;
        out += "-" + formatInt(month,2);
        if (date < 0)
            return out;
        out += "-" + formatInt(date, 2);
        if (hour < 0)
            return out;
        out += " " + formatInt(hour,2);
        if (minute < 0)
            return out + ":00";
        out += ":" + formatInt(minute,2);
        return out;
    }

    /**
     * Formats an integer into a string which has at least some number of digits.
     * @param value the integer to format
     * @param numDigits the minimum number of digits that the output string should contain. Any added digits are leading zeros.
     * @return
     */
    private String formatInt(int value, int numDigits){
        String out = Integer.toString(value);
        if (out.length() >= numDigits)
            return out;
        while (out.length() < numDigits)
            out = "0" + out;
        return out;
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
}
