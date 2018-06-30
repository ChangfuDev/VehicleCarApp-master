package com.example.bofa.vehicle_car_auto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.example.bofa.vehicle_car_auto.sqlite.D;
import com.example.bofa.vehicle_car_auto.sqlite.Dao.UserDao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Bofa on 2018/5/16.
 */

public class UsersActivity extends Activity implements View.OnClickListener {
    private CircleImageView current_user;
    private CircleImageView other_user;
    private CircleImageView other_user2;
    private AwesomeTextView select_user;
    private View divide;
    private int current_userId;
    private UserDao userDao;
    private Intent intent;
    private Drawable[] col = new Drawable[3];
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private BitmapDrawable drawable;
    private Bitmap bitmap;
    private Bitmap bitmap2;
    private ByteArrayOutputStream baos;
    private String imageBase64;
    private boolean flag1 ; //123
    private boolean flag2 ; //213
    private boolean flag3 ; //312
    private boolean flag4 ; //132
    private boolean flag5 ; //231
    private boolean flag6 ; //321



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users);
        current_userId = getIntent().getIntExtra("current_userId",0);
        if(getIntent().getBundleExtra("drawable")!=null){
            col = (Drawable[]) getIntent().getBundleExtra("drawable").getSerializable("drawable");
        }
        sharedPreferences = getSharedPreferences("drawable",MODE_PRIVATE);
        /**
         * 选择当前用户不读取后台数据
         * 切换用户 读取后台数据
         *
         */
        if(current_userId != 0){
            flag1 = sharedPreferences.getBoolean("flag1",false);
            flag2 = sharedPreferences.getBoolean("flag2",false);
            flag3 = sharedPreferences.getBoolean("flag3",false);
            flag4 = sharedPreferences.getBoolean("flag4",false);
            flag5 = sharedPreferences.getBoolean("flag5",false);
            flag6 = sharedPreferences.getBoolean("flag6",false);
            Log.d("flag1",""+flag1);
            Log.d("flag2",""+flag2);
            Log.d("flag3",""+flag3);
            Log.d("flag4",""+flag4);
            Log.d("flag5",""+flag5);
            Log.d("flag6",""+flag6);
        }


        initView();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                if (isFirstStart) {

                    final Intent i = new Intent(UsersActivity.this, AppIntro.class);

                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            startActivity(i);
                        }
                    });

                    SharedPreferences.Editor e = getPrefs.edit();

                    e.putBoolean("firstStart", false);

                    e.apply();
                }
            }
        });
        t.start();

    }

    public void setNavBarColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, color));
        }
    }

    public void initView(){
        current_user = (CircleImageView) findViewById(R.id.current_user);
        other_user = (CircleImageView) findViewById(R.id.other);
        other_user2 = (CircleImageView) findViewById(R.id.other2);
        select_user = (AwesomeTextView) findViewById(R.id.select);
        divide = (View) findViewById(R.id.divide);
        current_user.setOnClickListener(this);
        other_user.setOnClickListener(this);
        other_user2.setOnClickListener(this);
        if(current_userId == 0){
            current_user.setImageResource(R.drawable.man);
            other_user.setImageResource(R.drawable.woman);
            other_user2.setImageResource(R.drawable.other);
        }else{
            ChangeLayOutParams();
        }
        setNavBarColor(R.color.circle_wave);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.current_user:
                if(current_userId == 0){
                    current_userId = 1;
                    editor = getSharedPreferences("drawable",MODE_PRIVATE).edit();
                    saveDrawable(getDrawable(R.drawable.man),"current_user");
                    saveDrawable(getDrawable(R.drawable.woman),"other_user");
                    saveDrawable(getDrawable(R.drawable.other),"other_user2");
                    flag1 = true;
                    editor.putBoolean("flag1",flag1);
                    editor.apply();
                    gotoHome(current_userId);
                }
                break;
            case R.id.other:
                if(current_userId != 0){

                    Drawable drawable = other_user.getDrawable();
                    Drawable drawable1 = current_user.getDrawable();
                    Drawable drawable2 = other_user2.getDrawable();
                    current_user.setImageDrawable(drawable);
                    other_user.setImageDrawable(drawable1);
                    editor = getSharedPreferences("drawable",MODE_PRIVATE).edit();
                    if(flag1 || flag6){
                        /**
                         * if 123
                         * become 213
                         */
                        if(flag1){
                            flag1 = false;
                            flag2 = true;
                            editor.putBoolean("flag1",flag1);
                            editor.putBoolean("flag2",flag2);
                        }
                        /**
                         * if 321
                         * become 231
                         */
                        if(flag6){
                            flag6 = false;
                            flag5 = true;
                            editor.putBoolean("flag6",flag6);
                            editor.putBoolean("flag5",flag5);
                        }
                        current_userId = 2;
                        System.out.println("current_userId1"+current_userId);
                    }else if(flag2 || flag3) {
                        /**
                         * if 213
                         * become 123
                         */
                        if(flag2){
                            flag2 = false;
                            flag1 = true;
                            editor.putBoolean("flag2",flag2);
                            editor.putBoolean("flag1",flag1);
                        }
                        /**
                         * if 312
                         * become 132
                         */
                        if(flag3){
                            flag3 = false;
                            flag4 = true;
                            editor.putBoolean("flag3",flag3);
                            editor.putBoolean("flag4",flag4);
                        }
                        current_userId = 1;
                        System.out.println("current_userId2"+current_userId);
                    }else if(flag4 || flag5){
                        /**
                         * if 132
                         * become 312
                         */
                        if(flag4){
                            flag4 = false;
                            flag3 = true;
                            editor.putBoolean("flag4",flag4);
                            editor.putBoolean("flag3",flag3);
                        }
                        /**
                         * if 231
                         * become 321
                         */
                        if(flag5){
                            flag5 = false;
                            flag6 = true;
                            editor.putBoolean("flag5",flag5);
                            editor.putBoolean("flag6",flag6);
                        }
                        current_userId = 3;
                        System.out.println("current_userId3"+current_userId);
                    }


                    saveDrawable(drawable,"current_user");
                    saveDrawable(drawable1,"other_user");
                    saveDrawable(drawable2,"other_user2");
                    editor.apply();

                }else{
                    editor = getSharedPreferences("drawable",MODE_PRIVATE).edit();
                    saveDrawable(getDrawable(R.drawable.woman),"current_user");
                    saveDrawable(getDrawable(R.drawable.man),"other_user");
                    saveDrawable(getDrawable(R.drawable.other),"other_user2");
                    editor.apply();

                    flag2 = true;
                    editor.putBoolean("flag2",flag2);

                    current_userId = 2;

                }
                gotoHome(current_userId);
                break;
            case R.id.other2:
                if(current_userId != 0){
                    Drawable drawable = other_user2.getDrawable();
                    Drawable drawable1 = current_user.getDrawable();
                    Drawable drawable2 = other_user.getDrawable();
                    current_user.setImageDrawable(drawable);
                    other_user2.setImageDrawable(drawable1);
                    editor = getSharedPreferences("drawable",MODE_PRIVATE).edit();
                    if(flag3 || flag4){
                        /**
                         * if 312
                         * become 213
                         */
                        if(flag3){
                            flag3 = false;
                            flag2 = true;
                            editor.putBoolean("flag3",flag3);
                            editor.putBoolean("flag2",flag2);
                        }
                        /**
                         * if 132
                         * become 231
                         */
                        if(flag4){
                            flag4 = false;
                            flag5 = true;
                            editor.putBoolean("flag4",flag4);
                            editor.putBoolean("flag5",flag5);
                        }
                        current_userId = 2;
                        Log.d("UserActivity",""+current_userId);
                    }else if(flag5 || flag6) {
                        /**
                         * if 231
                         * become 132
                         */
                        if(flag5){
                            flag5 = false;
                            flag4 = true;
                            editor.putBoolean("flag5",flag5);
                            editor.putBoolean("flag4",flag4);
                        }
                        /**
                         * if 321
                         * become 123
                         */
                        if(flag6){
                            flag6 = false;
                            flag1 = true;
                            editor.putBoolean("flag6",flag6);
                            editor.putBoolean("flag1",flag1);
                        }
                        current_userId = 1;
                        Log.d("UserActivity",""+current_userId);
                    }else if(flag1 || flag2){
                        /**
                         * if 123
                         * become 321
                         */
                        if(flag1){
                            flag1 = false;
                            flag6 = true;
                            editor.putBoolean("flag1",flag1);
                            editor.putBoolean("flag6",flag6);
                        }
                        /**
                         * if 213
                         * become 312
                         */
                        if(flag2){
                            flag2 = false;
                            flag3 = true;
                            editor.putBoolean("flag2",flag2);
                            editor.putBoolean("flag3",flag3);
                        }

                        current_userId = 3;
                        Log.d("UserActivity",""+current_userId);
                    }


                    saveDrawable(drawable,"current_user");
                    saveDrawable(drawable2,"other_user");
                    saveDrawable(drawable1,"other_user2");
                    editor.apply();
                }else{
                    editor = getSharedPreferences("drawable",MODE_PRIVATE).edit();
                    saveDrawable(getDrawable(R.drawable.other),"current_user");
                    saveDrawable(getDrawable(R.drawable.man),"other_user");
                    saveDrawable(getDrawable(R.drawable.woman),"other_user2");
                    editor.apply();

                    flag3 = true;
                    editor.putBoolean("flag3",flag3);

                    current_userId = 3;
                }
                gotoHome(current_userId);
                break;

        }
    }

    /**
     * 选择当前用户，如果该用户在数据库中没有设定应急联系人电话，则跳转到SettingsActivity界面再进入Home首页
     * @param current_userId 传递当前用户的标识符
     */
    public void gotoHome(int current_userId){
        userDao = UserDao.getUserDao(UsersActivity.this);
        Cursor cursor = userDao.getUserPhoneNum(current_userId);
        if(cursor.getString(cursor.getColumnIndex(D.Vehicle_User.USER_PHONENUM))==null){
            intent = new Intent(UsersActivity.this,SettingsActivity.class);
        }else{
            intent = new Intent(UsersActivity.this,MainActivity.class);
        }
        intent.putExtra("current_userId",current_userId);
        startActivity(intent);
        finish();
    }

    /**
     * SharePreferences 保存图片参数，转为二进制参数
     * @param draw 保存的draw对象
     * @param object 对应的保存对象 current_user,other_user,other_user2
     */
    public void saveDrawable(Drawable draw,String object){
        baos = new ByteArrayOutputStream();
        drawable = (BitmapDrawable) draw;//这里需要强转BitmapDrawable类型
        bitmap = drawable.getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
        imageBase64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        editor.putString(object,imageBase64);
    }

    public boolean IsSame(Drawable d1, Drawable d2){
        bitmap = ((BitmapDrawable) d1).getBitmap();
        bitmap2 = ((BitmapDrawable) d2).getBitmap();

        //先判断宽高是否一致，不一致直接返回false
        if(bitmap.getWidth()==bitmap2.getWidth()
                &&bitmap.getHeight()==bitmap2.getHeight()){
            int xCount = bitmap.getWidth();
            int yCount = bitmap.getHeight();
            System.out.println("xCount:"+xCount);
            System.out.println("yCount:"+yCount);
            for(int x=0; x<xCount; x++){
                for(int y=0; y<yCount; y++){
                    //比较每个像素点颜色
                    if(bitmap.getPixel(x, y)!=bitmap2.getPixel(x, y)){
                        System.out.println("1111111222222");
                        return false;
                    }
                }
            }
            System.out.println("2222222");
            return true;
        }else{
            return false;
        }
    }

    /**
     * SharePreferences 读取图片参数，二进制参数转drawable对象
     * @param object 对应的读取对象 current_user,other_user,other_user2
     * @return
     */
    public Drawable loadDrawable(String object){
        String temp = sharedPreferences.getString(object,"");
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(temp.getBytes(), Base64.DEFAULT));
        return Drawable.createFromStream(bais,"");
    }

    /**
     * 当确定当前用户后的布局改变，以及切换当前用户后图片的图片资源参数改变
     */
    public void ChangeLayOutParams(){

        current_user.setImageDrawable(loadDrawable("current_user"));
        other_user.setImageDrawable(loadDrawable("other_user"));
        other_user2.setImageDrawable(loadDrawable("other_user2"));

        select_user.setGravity(Gravity.LEFT);
        select_user.setText(R.string.current_user);

        divide.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams layoutParams = new
                RelativeLayout.LayoutParams(other_user.getLayoutParams());
        layoutParams.height = dip2px(UsersActivity.this,100);
        layoutParams.width = dip2px(UsersActivity.this,100);
        layoutParams.setMargins(dip2px(UsersActivity.this,210),dip2px(UsersActivity.this,25),0,0);
        other_user.setLayoutParams(layoutParams);

        RelativeLayout.LayoutParams layoutParams2 = new
                RelativeLayout.LayoutParams(other_user2.getLayoutParams());
        layoutParams2.height = dip2px(UsersActivity.this,100);
        layoutParams2.width = dip2px(UsersActivity.this,100);
        layoutParams2.setMargins(dip2px(UsersActivity.this,360),dip2px(UsersActivity.this,25),0,0);
        other_user2.setLayoutParams(layoutParams2);
    }

    /**
     * dp转为px
     * @param context  上下文
     * @param dipValue dp值
     * @return
     */
    private int dip2px(Context context, float dipValue)
    {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
    }
}
