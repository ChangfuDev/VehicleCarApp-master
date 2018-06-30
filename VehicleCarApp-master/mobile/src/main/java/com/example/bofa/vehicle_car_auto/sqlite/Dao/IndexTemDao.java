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

public class IndexTemDao {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase mydb;
    private static IndexTemDao indexTemDao;


    private IndexTemDao(Context context){
        databaseHelper = DatabaseHelper.getDatabaseHelper(context);
    }

    public static IndexTemDao getIndexTemDao(Context context){
        if(indexTemDao == null){
            indexTemDao = new IndexTemDao(context);
        }
        return indexTemDao;
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
    public void InsertMessage(double tiwen_max, double tiwen_min, String uptime, int feature_id, String feature_name){
        if(!IsExsists(feature_id)){
            open();
            ContentValues cv = new ContentValues();
            long result = 0;
            try{
                cv.put(D.Vehicle_Index_Temperature.WARNING_Temperature_MAX,tiwen_max);
                cv.put(D.Vehicle_Index_Temperature.WARNING_Temperature_MIN,tiwen_min);
                cv.put(D.Vehicle_Index_Temperature.UPLOAD_TIME,uptime);
                cv.put(D.Vehicle_Index_Temperature.FEATURE_Id,feature_id);
                cv.put(D.Vehicle_Index_Temperature.FEATURE_Name,feature_name);
                result = mydb.insert(D.Tables.Vehicle_Index_Temperature,null,cv);
            }catch (SQLException e){
                e.printStackTrace();
            }
            close();
        }else{
            Update(feature_id,tiwen_max,tiwen_min,uptime,feature_name);
        }
    }

    private boolean IsExsists(int feature_id){
        open();
        Cursor cursor = null;
        boolean flag = false;
        try {
            cursor = mydb.query(D.Tables.Vehicle_Index_Temperature,null,D.Vehicle_Index_Temperature.FEATURE_Id+" = "+ feature_id,null,null,null,null);
            if(cursor.getCount()>0){
                flag = true;
                Log.d("IndexTem",feature_id+"已存在");
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        close();
        return flag;
    }
    private void Update(int feature_id ,double tiwen_max, double tiwen_min, String uptime, String feature_name){//更新对应用户ID的应急联系人电话
        open();
        try{
            String update_sql = "update "+D.Tables.Vehicle_Index_Temperature+" set "+D.Vehicle_Index_Temperature.WARNING_Temperature_MAX+" = "+tiwen_max
                    +" ,"+D.Vehicle_Index_Temperature.WARNING_Temperature_MIN+" = "+tiwen_min
                    +" ,"+D.Vehicle_Index_Temperature.UPLOAD_TIME+" = '"+uptime+"'"
                    +" ,"+D.Vehicle_Index_Temperature.FEATURE_Name+" = '"+feature_name+"'"
                    +" where "+D.Vehicle_Index_Temperature.FEATURE_Id+" = "+feature_id;
            System.out.println(update_sql);
            mydb.execSQL(update_sql);
            Log.d("IndexTem",feature_name+"update success!");
        }catch (SQLException e){
            e.printStackTrace();
        }
        close();
    }

    public Cursor getIndex(){
        open();
        Cursor cur = null;
        try {

            String col[] = {D.Vehicle_Index_Temperature.WARNING_Temperature_MIN,
                    D.Vehicle_Index_Temperature.WARNING_Temperature_MAX,
                    D.Vehicle_Index_Temperature.FEATURE_Id
            };
            cur = mydb.query(D.Tables.Vehicle_Index_Temperature,col,null,null,null, null, null);
            cur.moveToFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return cur;
    }
}
