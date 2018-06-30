package com.example.bofa.vehicle_car_auto.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Bofa on 2018/5/7.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "BOFA";
    private static DatabaseHelper databaseHelper;

    private DatabaseHelper (Context context){
        super(context,D.DATABASENAME,null,D.DATABASE_VERSION);
        Log.d(TAG,"DB Version=" + D.DATABASE_VERSION);
    }
    public static DatabaseHelper getDatabaseHelper(Context context){
        if(databaseHelper == null){
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTableUser(sqLiteDatabase);
        createTableHeartRate(sqLiteDatabase);
        createTableHistory(sqLiteDatabase);
        createTableIndexHR(sqLiteDatabase);
        createTableIndexTem(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        dropTableUser(sqLiteDatabase);
        dropTableHeartRate(sqLiteDatabase);
        dropTableHistory(sqLiteDatabase);
        dropTableIndexHR(sqLiteDatabase);
        dropTableIndexTem(sqLiteDatabase);

        createTableUser(sqLiteDatabase);
        createTableHeartRate(sqLiteDatabase);
        createTableHistory(sqLiteDatabase);
        createTableIndexHR(sqLiteDatabase);
        createTableIndexTem(sqLiteDatabase);
    }


    private void createTableUser(SQLiteDatabase db){
        String createTableUser = "create table " + D.Tables.Vehicle_User + "("
                + D.Vehicle_User.KEY_ROWID + " INTEGER PRIMARY KEY,"
                + D.Vehicle_User.USER_ID + " INTEGER not null,"
                + D.Vehicle_User.USER_PHONENUM + " TEXT"
                + ");";
        db.execSQL(createTableUser);
        Log.d(TAG," CreateDB SUCCESS, SQL=" + createTableUser);
    }
    private void dropTableUser(SQLiteDatabase db){
        String dropTableUser = "drop table if exists " +D.Tables.Vehicle_User +";";
        db.execSQL(dropTableUser);
        Log.d(TAG,"DeleteTable success, SQL=" + dropTableUser);
    }
    private void createTableHeartRate(SQLiteDatabase db){
        String createTableXinlv = "create table " + D.Tables.Vehicle_HeartRate + "("
                + D.Vehicle_HeartRate.KEY_ROWID + " INTEGER PRIMARY KEY,"
                + D.Vehicle_HeartRate.USER_ID + " INTEGER not null,"
                + D.Vehicle_HeartRate.USER_HeartRate + " INTEGER not null,"
                + D.Vehicle_HeartRate.USER_Temperature + " FLOAT not null,"
                + D.Vehicle_HeartRate.UPLOAD_TIME + " DATETIME not null"
                + ");";
        db.execSQL(createTableXinlv);
        Log.d(TAG," CreateDB SUCCESS, SQL=" + createTableXinlv);
    }
    private void dropTableHeartRate(SQLiteDatabase db){
        String dropTableXinlv = "drop table if exists " +D.Tables.Vehicle_HeartRate +";";
        db.execSQL(dropTableXinlv);
        Log.d(TAG,"DeleteTable success, SQL=" + dropTableXinlv);
    }
    private void createTableHistory(SQLiteDatabase db){
        String createTableHistory = "create table " + D.Tables.Vehicle_History + "("
                + D.Vehicle_History.KEY_ROWID + " INTEGER PRIMARY KEY,"
                + D.Vehicle_History.USER_ID + " INTEGER not null,"
                + D.Vehicle_History.USER_AVGHeartRate + " INTEGER not null,"
                + D.Vehicle_History.USER_AVGTemperature + " FLOAT not null,"
                + D.Vehicle_History.UPLOAD_TIME + " DATETIME not null"
                + ");";
        db.execSQL(createTableHistory);
        Log.d(TAG," CreateDB SUCCESS, SQL=" + createTableHistory);
    }
    private void dropTableHistory(SQLiteDatabase db){
        String dropTableHistory = "drop table if exists " +D.Tables.Vehicle_History +";";
        db.execSQL(dropTableHistory);
        Log.d(TAG,"DeleteTable success, SQL=" + dropTableHistory);
    }
    private void createTableIndexHR(SQLiteDatabase db){
        String createTableIndex = "create table " + D.Tables.Vehicle_Index_HeartRate + "("
                + D.Vehicle_Index_HeartRate.KEY_ROWID + " INTEGER PRIMARY KEY,"
                + D.Vehicle_Index_HeartRate.FEATURE_Id + " INTEGER not null,"
                + D.Vehicle_Index_HeartRate.FEATURE_Name + " TEXT not null,"
                + D.Vehicle_Index_HeartRate.WARNING_HeartRate_MAX + " INTEGER not null,"
                + D.Vehicle_Index_HeartRate.WARNING_HeartRate_MIN + " INTEGER not null,"
                + D.Vehicle_Index_HeartRate.UPLOAD_TIME + " TEXT not null"
                + ");";
        db.execSQL(createTableIndex);
        Log.d(TAG," CreateDB SUCCESS, SQL=" + createTableIndex);
    }
    private void dropTableIndexHR(SQLiteDatabase db){
        String dropTableIndex = "drop table if exists " +D.Tables.Vehicle_Index_HeartRate +";";
        db.execSQL(dropTableIndex);
        Log.d(TAG,"DeleteTable success, SQL=" + dropTableIndex);
    }
    private void createTableIndexTem(SQLiteDatabase db){
        String createTableIndex = "create table " + D.Tables.Vehicle_Index_Temperature + "("
                + D.Vehicle_Index_Temperature.KEY_ROWID + " INTEGER PRIMARY KEY,"
                + D.Vehicle_Index_Temperature.FEATURE_Id + " INTEGER not null,"
                + D.Vehicle_Index_Temperature.FEATURE_Name + " TEXT not null,"
                + D.Vehicle_Index_Temperature.WARNING_Temperature_MIN + " DOUBLE not null,"
                + D.Vehicle_Index_Temperature.WARNING_Temperature_MAX + " DOUBLE not null,"
                + D.Vehicle_Index_Temperature.UPLOAD_TIME + " TEXT not null"
                + ");";
        db.execSQL(createTableIndex);
        Log.d(TAG," CreateDB SUCCESS, SQL=" + createTableIndex);
    }
    private void dropTableIndexTem(SQLiteDatabase db){
        String dropTableIndex = "drop table if exists " +D.Tables.Vehicle_Index_Temperature +";";
        db.execSQL(dropTableIndex);
        Log.d(TAG,"DeleteTable success, SQL=" + dropTableIndex);
    }

}
