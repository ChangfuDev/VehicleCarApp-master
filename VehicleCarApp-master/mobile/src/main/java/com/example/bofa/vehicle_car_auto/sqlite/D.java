package com.example.bofa.vehicle_car_auto.sqlite;

/**
 * Created by Bofa on 2018/5/7.
 */

public final class D {
    public static final String DATABASENAME = "Vehicleapp.db";
    public static final int DATABASE_VERSION = 2;

    public static final class Tables{
        public static final String Vehicle_User = "vehicle_user";
        public static final String Vehicle_HeartRate = "vehicle_heartRate";
        public static final String Vehicle_History = "vehicle_history";
        public static final String Vehicle_Index_HeartRate ="vehicle_index_heartRate";
        public static final String Vehicle_Index_Temperature ="vehicle_index_temperature";
    }
    public static final class Vehicle_HeartRate{
        public static final String KEY_ROWID = "_id";
        public static final String USER_ID = "user_id";
        public static final String USER_HeartRate = "user_heartRate";
        public static final String USER_Temperature = "user_temperature";
        public static final String UPLOAD_TIME = "up_time";
    }
    public static final class Vehicle_User{
        public static final String KEY_ROWID = "_id";
        public static final String USER_ID = "user_id";
        public static final String USER_PHONENUM = "user_phonenum";
    }
    public static final class Vehicle_History{
        public static final String KEY_ROWID = "_id";
        public static final String USER_ID = "user_id";
        public static final String USER_AVGHeartRate = "user_avgheartRate";
        public static final String USER_AVGTemperature = "user_avgtemperature";
        public static final String UPLOAD_TIME = "up_time";
    }
    public static final class Vehicle_Index_HeartRate{
        public static final String KEY_ROWID = "_id";
        public static final String FEATURE_Id = "feature_id";
        public static final String FEATURE_Name = "feature_name";
        public static final String WARNING_HeartRate_MIN = "warning_heartRate_min";
        public static final String WARNING_HeartRate_MAX = "warning_heartRate_max";
        public static final String UPLOAD_TIME = "up_time";
    }
    public static final class Vehicle_Index_Temperature{
        public static final String KEY_ROWID = "_id";
        public static final String FEATURE_Id = "feature_id";
        public static final String FEATURE_Name = "feature_name";
        public static final String WARNING_Temperature_MIN = "warning_temperature_min";
        public static final String WARNING_Temperature_MAX = "warning_temperature_max";
        public static final String UPLOAD_TIME = "up_time";
    }
}