package com.example.avispiro;

import android.content.Context;
import android.content.res.Resources;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;

public class Category {
    private String name;
    private int id;

    public static final String UNCATEGORIZED = App.getContext().getResources().getString(R.string.category_name_default);

    public static int search (SpinnerAdapter arr, String name){
        int i = 0;
        while (i < arr.getCount()) {
            if (reduce(arr.getItem(i).toString()).equals(name))
                return i;
            i++;
        }
            return -1;
    }

    /**
     * Returns the uncatagorized category
     */
    public Category(){
        name = UNCATEGORIZED;
        id = 1;
    }

    public Category(String name) {
        this.name = name;
        id = -1;
    }


    public Category(String name, int id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Reduces some string to a basic form. Two categories cannot have the same reduced name.
     * @return
     */
    public static String reduce(String string){
        return string.toLowerCase().trim();
    }

    /**
     * Must always return the same as getName()
     * @return
     */
    public String toString(){
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
