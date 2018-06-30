package com.example.bofa.vehicle_car_auto;

import android.app.Application;

import com.android.volley.RequestQueue;

/**
 * Created by pc on 2018/6/9.
 */

public class Volley extends Application{
    private static RequestQueue mQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        mQueue = com.android.volley.toolbox.Volley.newRequestQueue(getApplicationContext());
    }
    public static RequestQueue getRequestQueue(){
        return mQueue;
    }
}
