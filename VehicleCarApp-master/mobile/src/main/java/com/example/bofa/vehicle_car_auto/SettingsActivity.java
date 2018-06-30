package com.example.bofa.vehicle_car_auto;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.bofa.vehicle_car_auto.sqlite.D;
import com.example.bofa.vehicle_car_auto.sqlite.Dao.UserDao;

/**
 * Created by Bofa on 2018/5/16.
 */

public class SettingsActivity extends Activity implements View.OnClickListener{
    private BootstrapButton yingji;
    private BootstrapButton help;
    private BootstrapEditText edit_yingji;
    private BootstrapButton submit;
    private BootstrapButton cancel;
    private BootstrapButton enter_home;
    private UserDao userDao;
    private int current_userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        initView();
    }
    private void initView(){
        userDao = UserDao.getUserDao(SettingsActivity.this);
        yingji = (BootstrapButton) findViewById(R.id.yingji);
        help = (BootstrapButton) findViewById(R.id.help);
        edit_yingji = (BootstrapEditText) findViewById(R.id.edit_yingji);
        submit = (BootstrapButton) findViewById(R.id.submit);
        cancel = (BootstrapButton) findViewById(R.id.cancel);
        enter_home = (BootstrapButton) findViewById(R.id.enter);
        enter_home.setOnClickListener(this);
        cancel.setOnClickListener(this);
        submit.setOnClickListener(this);
        yingji.setOnClickListener(this);
        help.setOnClickListener(this);
        current_userId = getIntent().getIntExtra("current_userId",0);
        setNavBarColor(R.color.circle_wave);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.yingji:
                setHide();
                getPhonenum();
                break;
            case R.id.help:
                Intent intent = new Intent(SettingsActivity.this,AppIntro.class);
                startActivity(intent);
                break;
            case R.id.submit:
                userDao = new UserDao(SettingsActivity.this);
                String phonenum = edit_yingji.getText().toString();
                boolean judge = isMobile(phonenum);
                if(!phonenum.equals("")&& judge){
                    userDao.Update(current_userId,phonenum);
                    setShow();
                    Toast.makeText(SettingsActivity.this,"更新成功！",Toast.LENGTH_LONG).show();
                }else if(phonenum.equals("")){
                    Toast.makeText(SettingsActivity.this,"手机号码不能为空",Toast.LENGTH_LONG).show();
                }else if(!judge){
                    Toast.makeText(SettingsActivity.this,"手机号码格式不正确",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.cancel:
                setShow();
                break;
            case R.id.enter:
                emptyUserPhoneNum();
                break;
        }
    }

    /**
     * 判断用户的应急电话是否为空
     */
    public void emptyUserPhoneNum(){
        Cursor cursor = userDao.getUserPhoneNum(current_userId);
        if(cursor.getString(cursor.getColumnIndex(D.Vehicle_User.USER_PHONENUM)) == null){
            Toast.makeText(SettingsActivity.this,"该用户应急电话为空，请先设置应急号码",Toast.LENGTH_LONG).show();
        }else{
            Intent intent1 = new Intent(SettingsActivity.this,MainActivity.class);
            intent1.putExtra("current_userId",current_userId);
            startActivity(intent1);
            finish();
        }
    }

    /**
     * 验证手机格式正则
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、152、157、158、159、178、182、184、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、170、173、177、180、181、189、（1349卫通）
    */
        String num = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    public void setShow(){
        yingji.setVisibility(View.VISIBLE);
        edit_yingji.setVisibility(View.GONE);
        submit.setAlpha(0.0f);
        cancel.setAlpha(0.0f);
    }

    public void setHide(){
        yingji.setVisibility(View.GONE);
        edit_yingji.setVisibility(View.VISIBLE);
        submit.setAlpha(0.7f);
        cancel.setAlpha(0.7f);
    }

    public void getPhonenum(){
        System.out.println("current_userId:"+current_userId);
        Cursor cursor = userDao.getUserPhoneNum(current_userId);
        if(cursor.getCount()>=0){
            String phonenum = cursor.getString(cursor.getColumnIndex(D.Vehicle_User.USER_PHONENUM));
            edit_yingji.setText(phonenum);
        }
        cursor.close();
    }

    public void setNavBarColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, color));
        }
    }
}
