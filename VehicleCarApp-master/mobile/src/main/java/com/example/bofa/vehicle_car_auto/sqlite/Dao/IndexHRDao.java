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
 * Created by Bofa on 2018/6/7.
 */

public class IndexHRDao {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase mydb;
    private static IndexHRDao indexHRDao;


    private IndexHRDao(Context context){
        databaseHelper = DatabaseHelper.getDatabaseHelper(context);
    }

    public static IndexHRDao getIndexHRDao(Context context){
        if(indexHRDao == null){
            indexHRDao = new IndexHRDao(context);
        }
        return indexHRDao;
    }

    private void open(){
        try{
            mydb = databaseHelper.getWritableDatabase();
        }catch (SQLException e){
            Log.e("Database","SQL exception");
            e.printStackTrace();
        }
    }
    private void close(){
        mydb.close();
        databaseHelper.close();
    }
    public void InsertMessage(int xinlv_max, int xinlv_min, String uptime, int feature_id, String feature_name){
        if(!IsExsists(feature_id)){
            open();
            ContentValues cv = new ContentValues();
            try{
                cv.put(D.Vehicle_Index_HeartRate.WARNING_HeartRate_MAX,xinlv_max);
                cv.put(D.Vehicle_Index_HeartRate.WARNING_HeartRate_MIN,xinlv_min);
                cv.put(D.Vehicle_Index_HeartRate.UPLOAD_TIME,uptime);
                cv.put(D.Vehicle_Index_HeartRate.FEATURE_Id,feature_id);
                cv.put(D.Vehicle_Index_HeartRate.FEATURE_Name,feature_name);
                mydb.insert(D.Tables.Vehicle_Index_HeartRate,null,cv);
            }catch (SQLException e){
                e.printStackTrace();
            }
            close();
        }else{
            Update(feature_id,xinlv_max,xinlv_min,uptime,feature_name);
        }
    }

    private boolean IsExsists(int feature_id){
        open();
        Cursor cursor = null;
        boolean flag = false;
        try {
            cursor = mydb.query(D.Tables.Vehicle_Index_HeartRate,null,D.Vehicle_Index_HeartRate.FEATURE_Id+" = "+ feature_id,null,null,null,null);
            if(cursor.getCount()>0){
                flag = true;
                Log.d("IndexHR",feature_id+"已存在");
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        close();
        return flag;
    }
    private void Update(int feature_id ,int xinlv_max, int xinlv_min, String uptime, String feature_name){//更新对应用户ID的应急联系人电话
        if(IsExsists(feature_id)){
            open();
            try{
                String update_sql = "update "+D.Tables.Vehicle_Index_HeartRate+" set "+D.Vehicle_Index_HeartRate.WARNING_HeartRate_MAX+" = "+xinlv_max
                        +" ,"+D.Vehicle_Index_HeartRate.WARNING_HeartRate_MIN+" = "+xinlv_min
                        +" ,"+D.Vehicle_Index_HeartRate.UPLOAD_TIME+" = '"+uptime+"'"
                        +" ,"+D.Vehicle_Index_HeartRate.FEATURE_Name+" = '"+feature_name+"'"
                        +" where "+D.Vehicle_Index_HeartRate.FEATURE_Id+" = "+feature_id;
                System.out.println(update_sql);
                mydb.execSQL(update_sql);
                Log.d("IndexHR",feature_name+"update success!");
            }catch (SQLException e){
                e.printStackTrace();
            }
            close();
        }

    }
    public Cursor getIndex(){
        open();
        Cursor cur = null;
        try {

            String col[] = {D.Vehicle_Index_HeartRate.WARNING_HeartRate_MAX,
                    D.Vehicle_Index_HeartRate.WARNING_HeartRate_MIN,
                    D.Vehicle_Index_HeartRate.FEATURE_Id
            };
            cur = mydb.query(D.Tables.Vehicle_Index_HeartRate,col,null,null,null, null, null);
            cur.moveToFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return cur;
    }
}
