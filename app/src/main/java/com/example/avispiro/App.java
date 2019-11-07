package com.example.avispiro;

import android.app.Application;
import android.content.Context;

/**
 * Adapted from https://stackoverflow.com/questions/4391720/how-can-i-get-a-resource-content-from-a-static-context/4391811#4391811
 */
public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    /**
     * Returns the app context
     * @return The app context
     */
    public static Context getContext(){
        return mContext;
    }
}