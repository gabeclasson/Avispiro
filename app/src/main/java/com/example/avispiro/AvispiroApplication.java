package com.example.avispiro;

import android.app.Application;
import android.content.res.Configuration;

/**
 * Adapted from
 */
public class AvispiroApplication extends Application {
    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    private MyDatabaseHelper databaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        // Required initialization logic here!
        databaseHelper = new MyDatabaseHelper(this, null, null, 0);
    }

    @Override
    public void onTerminate() {
        databaseHelper.close();
        super.onTerminate();
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public MyDatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }
}