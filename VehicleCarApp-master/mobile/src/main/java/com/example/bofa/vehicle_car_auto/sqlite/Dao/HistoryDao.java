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
 * Created by pc on 2018/5/17.
 */

public class HistoryDao {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase mydb;
    private static HistoryDao historyDao;

    public HistoryDao(Context context){
        databaseHelper = DatabaseHelper.getDatabaseHelper(context);
    }
    public static HistoryDao getHistoryDao(Context context){
        if(historyDao == null){
            historyDao = new HistoryDao(context);
        }
        return historyDao;
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
    public long InsertMessage(int UserId,int avg_xinlv, double avg_tiwen, String uptime){
        open();
        ContentValues cv = new ContentValues();
        long result = 0;
        try{
            cv.put(D.Vehicle_History.USER_ID,UserId);
            cv.put(D.Vehicle_History.USER_AVGHeartRate,avg_xinlv);
            cv.put(D.Vehicle_History.USER_AVGTemperature,avg_tiwen);
            cv.put(D.Vehicle_HeartRate.UPLOAD_TIME,uptime);
            result = mydb.insert(D.Tables.Vehicle_History,null,cv);
        }catch (SQLException e){
            e.printStackTrace();
        }
        close();
        return result;
    }
    public Cursor getbyUserId(long user_id){
        Cursor cur = null;
        try {
            open();
            String col[] = {"_id", D.Vehicle_History.USER_AVGHeartRate, D.Vehicle_History.USER_AVGTemperature, D.Vehicle_History.UPLOAD_TIME};
            cur = mydb.query(D.Tables.Vehicle_History, col,"user_id=?",new String[]{user_id+""},null, null, null);
            return cur;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cur;
    }
}
