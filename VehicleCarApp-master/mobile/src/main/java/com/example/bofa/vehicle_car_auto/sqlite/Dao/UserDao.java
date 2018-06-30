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

public class UserDao {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase mydb;
    private static UserDao userDao;

    public UserDao(Context context){
        databaseHelper = DatabaseHelper.getDatabaseHelper(context);
        Insert();
    }
    public static UserDao getUserDao(Context context){
        if(userDao == null){
            userDao = new UserDao(context);
        }
        return userDao;
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
    public void Update(int UserId,String phonenum){//更新对应用户ID的应急联系人电话
        open();
        try{
            String update_sql = "update "+D.Tables.Vehicle_User+" set "+D.Vehicle_User.USER_PHONENUM+" = '"+phonenum+"' where "+D.Vehicle_User.USER_ID+" = "+UserId;
            System.out.println(update_sql);
            mydb.execSQL(update_sql);
            Log.d("dao","update success!");
        }catch (SQLException e){
            e.printStackTrace();
        }
        close();
    }
    public Cursor getUserPhoneNum(int UserId){//获取对应用户ID的应急联系人电话
        open();
        Cursor cur = null;
        try {
            String col[] = { D.Vehicle_User.USER_PHONENUM };
            cur = mydb.query(D.Tables.Vehicle_User, col, D.Vehicle_User.USER_ID+" = "+UserId, null, null, null, null);
            cur.moveToFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cur;
    }
    public void Insert(){
        open();
        Cursor cur = null;
        try{
            String col[] = { D.Vehicle_User.USER_ID};
            cur = mydb.query(D.Tables.Vehicle_User,col,null,null,null,null,null);
            cur.moveToFirst();
        }catch (SQLException e){
            e.printStackTrace();
        }
        if(!(cur.getCount()>0)){
            cur.close();
            for(int i = 1; i <= 3; i++){
                ContentValues cv = new ContentValues();
                long result = 0;
                try{
                    cv.put(D.Vehicle_User.USER_ID,i);
                    result = mydb.insert(D.Tables.Vehicle_User,null,cv);
                }catch (SQLException e){
                    e.printStackTrace();
                }
                if(result > 0){
                    Log.d("user_id: "+i,"insert success!");
                }
            }
        }
        close();
    }
}
