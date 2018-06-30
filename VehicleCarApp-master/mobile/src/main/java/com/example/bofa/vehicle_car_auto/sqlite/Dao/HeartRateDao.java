package com.example.bofa.vehicle_car_auto.sqlite.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.bofa.vehicle_car_auto.sqlite.D;
import com.example.bofa.vehicle_car_auto.sqlite.DatabaseHelper;

/**
 * Created by Bofa on 2018/5/7.
 */

public class HeartRateDao {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase mydb;
    private static HeartRateDao heartRateDao;

    public HeartRateDao(Context context){
        databaseHelper = DatabaseHelper.getDatabaseHelper(context);
    }

    public static HeartRateDao getHeartRateDao(Context context){
        if(heartRateDao == null){
            heartRateDao = new HeartRateDao(context);
        }
        return heartRateDao;
    }

    public void open(){
        try{
            mydb = databaseHelper.getWritableDatabase();
        }catch (SQLException e){
            Log.e("Database","SQL exception");
            e.printStackTrace();
        }
    }
    public void close(){
        mydb.close();
        databaseHelper.close();
    }
    public long InsertMessage(int xinlv, double tiwen, int UserId, String uptime){
        open();
        ContentValues cv = new ContentValues();
        long result = 0;
        try{
            cv.put(D.Vehicle_HeartRate.USER_HeartRate,xinlv);
            cv.put(D.Vehicle_HeartRate.USER_Temperature,tiwen);
            cv.put(D.Vehicle_HeartRate.USER_ID,UserId);
            cv.put(D.Vehicle_HeartRate.UPLOAD_TIME,uptime);
            result = mydb.insert(D.Tables.Vehicle_HeartRate,null,cv);
        }catch (SQLException e){
            e.printStackTrace();
        }
        close();
        return result;
    }

    public  long DeleteMessage(long user_id){
        open();
        long result = 0;
        try {
            result = mydb.delete(D.Tables.Vehicle_HeartRate,"user_id=?",new String[]{user_id+""});
        }catch (SQLException e){
            e.printStackTrace();
        }
        close();
        return result;
    }

    public Cursor getbyUserId(long user_id){
        open();
        Cursor cur = null;
        try {

            String col[] = {"_id", D.Vehicle_HeartRate.USER_HeartRate, D.Vehicle_HeartRate.USER_Temperature, D.Vehicle_HeartRate.UPLOAD_TIME};
            cur = mydb.query(D.Tables.Vehicle_HeartRate, col,"user_id=?",new String[]{user_id+""},null, null, null);
            cur.moveToFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return cur;
    }

    public Cursor getbyTimeId(long user_id,String up_time){
        open();
        Cursor cur = null;
        try {
            cur = mydb.rawQuery("select * from vehicle_heartRate where user_id = "+user_id+" and date_format(up_time,'%Y年%m月%d日') = "+up_time+";",null);
            cur.moveToFirst();
            System.out.println(cur);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return cur;
    }
}
