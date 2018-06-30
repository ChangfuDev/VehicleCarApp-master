package com.example.bofa.vehicle_car_auto.animation;

/**
 * Created by Bofa on 2018/5/3.
 */

import android.content.Context;

public class DensityUtil {

    public static float dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dpValue * scale;
    }
}
