package com.example.bofa.vehicle_car_auto.ContactInfo;

/**
 * Created by Bofa on 2018/6/8.
 */

public class IndexTem {
    private static int feature_id;
    private static double temperature_max;
    private static double temperature_min;
    private static IndexTem  indexTem;

    public static IndexTem getIndexTem(){
        if(indexTem == null){
            indexTem = new IndexTem();
        }
        return indexTem;
    }

    public int getFeature_id() {
        return feature_id;
    }

    public void setFeature_id(int feature_id) {
        this.feature_id = feature_id;
    }

    public double getTemperature_max() {
        return temperature_max;
    }

    public void setTemperature_max(double temperature_max) {
        this.temperature_max = temperature_max;
    }

    public double getTemperature_min() {
        return temperature_min;
    }

    public void setTemperature_min(double temperature_min) {
        this.temperature_min = temperature_min;
    }

}
