package com.example.bofa.vehicle_car_auto;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.example.bofa.vehicle_car_auto.ContactInfo.IndexHR;
import com.example.bofa.vehicle_car_auto.ContactInfo.IndexTem;
import com.example.bofa.vehicle_car_auto.animation.LevelLoadingRenderer;
import com.example.bofa.vehicle_car_auto.animation.LoadingDrawable;
import com.example.bofa.vehicle_car_auto.sqlite.D;
import com.example.bofa.vehicle_car_auto.sqlite.Dao.HeartRateDao;
import com.example.bofa.vehicle_car_auto.sqlite.Dao.HistoryDao;
import com.example.bofa.vehicle_car_auto.sqlite.Dao.IndexHRDao;
import com.example.bofa.vehicle_car_auto.sqlite.Dao.IndexTemDao;
import com.example.bofa.vehicle_car_auto.sqlite.Dao.UserDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;


public class MainActivity extends Activity implements SpeechSynthesizerListener, View.OnClickListener {

    private HeartRateDao heartRateDao;
    private UserDao userDao;
    private HistoryDao historyDao;
    private IndexHRDao indexHRDao;
    private IndexTemDao indexTemDao;
    private IndexTem indexTem;
    private IndexHR indexHR;
    private IndexHR[] indexHRs;
    private IndexTem[] indexTems;
    private ImageView mIvGear;
    private RelativeLayout ring;
    private ImageView history;
    private ImageView settings;
    private ImageView users;
    private TextView tiwen;
    private TextView xinlv;
    private CardView cardView;
    private boolean flag = true;
    private LoadingDrawable mGearDrawable;
    private MathRandom mathRandom = new MathRandom();
    private boolean IsPlay = false;
    private boolean IsSynthesize = false;
    private int current_userId;
    private String phonenum;

    private FButton nowButton;
    private FButton userButton;
    private FButton historyButton;
    private FButton settingButton;
    private int count = 1;

    protected String appId = "11213672";

    protected String appKey = "HCx8U2FUt0nCWT48ZDd3eHUF";

    protected String secretKey = "qWVhgDecW7DiCKzqUdWQqFxhDvlCEtTM";

    private SpeechSynthesizer speechSynthesizer;//语音播报控制类

    private String featureName;
    private int minHeartRate;
    private int featureID;
    private int maxHeartRate;
    private double maxTemperature;
    private double minTemperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        initDao(MainActivity.this);
        initView();
        initEvent();
        initIndex();
        initAnimation();
        initPermission();
        init_TTS();
        setHistory();
        new Thread(new MyRandom()).start();
    }


    /**
     * 初始化Dao
     */
    public void initDao(Context context) {
        heartRateDao = HeartRateDao.getHeartRateDao(context);
        userDao = UserDao.getUserDao(context);
        historyDao = HistoryDao.getHistoryDao(context);
        indexHRDao = IndexHRDao.getIndexHRDao(context);
        indexTemDao = IndexTemDao.getIndexTemDao(context);
    }

    /**
     * 初始化控件
     */
    @SuppressLint("ResourceAsColor")
    public void initView() {
        cardView = (CardView) findViewById(R.id.cardview);
        mIvGear = (ImageView) findViewById(R.id.imageview1);
        history = (ImageView) findViewById(R.id.img_2);
        settings = (ImageView) findViewById(R.id.img_3);
        users = (ImageView) findViewById(R.id.img_1);
        ring = (RelativeLayout) findViewById(R.id.ring);
        tiwen = (TextView) findViewById(R.id.tiwen);
        xinlv = (TextView) findViewById(R.id.xinlv);

        nowButton = findViewById(R.id.home_fbutton_now);
        userButton = findViewById(R.id.home_fbutton_user);
        historyButton = findViewById(R.id.home_fbutton_history);
        settingButton = findViewById(R.id.home_fbutton_setting);

        current_userId = getIntent().getIntExtra("current_userId", 0);
        Log.d("MainActivity_userId",""+current_userId);

        Cursor cursor = userDao.getUserPhoneNum(current_userId);
        System.out.println(cursor.getString(cursor.getColumnIndex(D.Vehicle_User.USER_PHONENUM)));
        if (cursor.getCount() > 0) {
            phonenum = cursor.getString(cursor.getColumnIndex(D.Vehicle_User.USER_PHONENUM));
        }
        cursor.close();
        setNavBarColor(R.color.circle_wave);//设置手机底部导航背景

        nowButton.setButtonColor(getResources().getColor(R.color.circle_wave));
        userButton.setButtonColor(getResources().getColor(R.color.circle_wave));
        historyButton.setButtonColor(getResources().getColor(R.color.circle_wave));
        settingButton.setButtonColor(getResources().getColor(R.color.circle_wave));

        nowButton.setAlpha(0.5f);
        userButton.setAlpha(0.5f);
        historyButton.setAlpha(0.5f);
        settingButton.setAlpha(0.5f);
//        cardView.setAlpha(0.95f);


        nowButton.setShadowHeight(5);
        userButton.setShadowHeight(5);
        historyButton.setShadowHeight(5);
        settingButton.setShadowHeight(5);

//        nowButton.setAlpha(0.8f);
//        userButton.setShadowHeight(5);
//        historyButton.setShadowHeight(5);
//        settingButton.setShadowHeight(5);



    }

    /**
     * 初始化事件
     */
    @SuppressLint("ClickableViewAccessibility")
    public void initEvent() {
        cardView.setOnClickListener(this);
        history.setOnClickListener(this);
        users.setOnClickListener(this);
        settings.setOnClickListener(this);

        nowButton.setOnClickListener(this);
        userButton.setOnClickListener(this);
        historyButton.setOnClickListener(this);
        settingButton.setOnClickListener(this);

        history.setVisibility(View.INVISIBLE);
        users.setVisibility(View.INVISIBLE);
        settings.setVisibility(View.INVISIBLE);
    }

    /**
     * 接收服务器数据指标以及症状id、name,存入对应指标表(Index)
     * 并从数据库中获取数据指标放入List<Index> list对象
     */
    public void initIndex() {
        String url_HR = "http://www.candidxd.com:8080/heartratefeature";
        String url_TEM = "http://www.candidxd.com:8080/temperaturefeature";

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());

        StringRequest mRequestHR = new StringRequest(Request.Method.GET, url_HR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("yl", "correct resp:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // 获取返回码，200表示成功
                    int retCode = jsonObject.getInt("code");
                    Log.d("yl", "code:" + retCode);
                    if (retCode == 200) {
                        JSONArray userArray = jsonObject.getJSONArray("data");
                        indexHRs = new IndexHR[userArray.length()];
                        Log.d("yl", "totalNum:" + userArray.length());
                        for (int i = 0; i < userArray.length(); i++) {
                            JSONObject userObject = (JSONObject) userArray.opt(i);
                            featureName = userObject.getString("featureName");
                            minHeartRate = userObject.getInt("minHeartRate");
                            featureID = userObject.getInt("featureID");
                            maxHeartRate = userObject.getInt("maxHeartRate");
                            indexHRs[i] = new IndexHR();
                            indexHRs[i].setFeature_id(featureID);
                            indexHRs[i].setHeartRate_min(minHeartRate);
                            indexHRs[i].setHeartRate_max(maxHeartRate);
                            indexHRDao.InsertMessage(maxHeartRate, minHeartRate, simpleDateFormat.format(date), featureID, featureName);
                            System.out.println(featureName + " " + minHeartRate + " " + featureID + " " + maxHeartRate);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("yl", "volley error:" + volleyError.toString());
            }
        });
        Volley.getRequestQueue().add(mRequestHR);

        StringRequest mRequestTEM = new StringRequest(Request.Method.GET, url_TEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("yl", "correct resp:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // 获取返回码，200表示成功
                    int retCode = jsonObject.getInt("code");
                    Log.d("yl", "code:" + retCode);
                    if (retCode == 200) {
                        JSONArray userArray = jsonObject.getJSONArray("data");
                        indexTems = new IndexTem[userArray.length()];
                        Log.d("yl", "totalNum:" + userArray.length());
                        for (int i = 0; i < userArray.length(); i++) {
                            JSONObject userObject = (JSONObject) userArray.opt(i);
                            maxTemperature = userObject.getDouble("maxTemperature");
                            featureName = userObject.getString("featureName");
                            minTemperature = userObject.getDouble("minTemperature");
                            featureID = userObject.getInt("featureID");
                            indexTems[i] = new IndexTem();
                            indexTems[i].setFeature_id(featureID);
                            indexTems[i].setTemperature_max(maxTemperature);
                            indexTems[i].setTemperature_min(minTemperature);
                            indexTemDao.InsertMessage(maxTemperature, minTemperature, simpleDateFormat.format(date), featureID, featureName);
                            System.out.println(maxTemperature + " " + featureName + " " + minTemperature + " " + featureID);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("yl", "volley error:" + volleyError.toString());
            }
        });
        Volley.getRequestQueue().add(mRequestTEM);



    }

    /**
     * 动画加载
     */

    public void initAnimation() {
//        mGearDrawable = new LoadingDrawable(new LevelLoadingRenderer(this));
//        mIvGear.setImageDrawable(mGearDrawable);
//        mGearDrawable.start();
//        SetShowAnimation(ring, 5000);
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            // TODO: 2018/6/17 点击监听 by yzk
            case R.id.home_fbutton_now:
                intent = new Intent(MainActivity.this, Ss_LineChartActivity.class);
                intent.putExtra("current_userId", current_userId);
                startActivity(intent);
                break;
            case R.id.home_fbutton_user:
                intent = new Intent(MainActivity.this, UsersActivity.class);
                intent.putExtra("current_userId", current_userId);
                startActivity(intent);
                break;
            case R.id.home_fbutton_history:
                intent = new Intent(MainActivity.this, LineChartActivity.class);
                intent.putExtra("current_userId", current_userId);
                startActivity(intent);
                break;
            case R.id.home_fbutton_setting:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.putExtra("current_userId", current_userId);
                startActivity(intent);
                break;
        }
    }


    /**
     * 线程模拟生成数据并导入数据库，并监控数据给出语音播报功能
     * 用的百度语音合成接口，支持离在线混合模式，根据网络wifi状况优先走在线
     * 在线时访问服务器失败后转为离线
     */

    class MyRandom implements Runnable {
        @Override
        public void run() {
            int x = 0;
            while (flag) {
                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int Int_xinlv = mathRandom.PercentageRandom();
                double Double_tiwen = mathRandom.PercentageRandom2();

//                int[] xinlv2 = {50,65,65,55};
//                int Int_xinlv = xinlv2[0];
//                double Double_tiwen = 36.9;
                try{
                    if (indexHRs.length > 0) {
                        for (int i = 0; i < indexHRs.length - 1; i++) {
                            if (Int_xinlv > indexHRs[i].getHeartRate_min()
                                    && Int_xinlv < indexHRs[i].getHeartRate_max()) {
                                switch (indexHRs[i].getFeature_id()) {
                                    case 1:
                                        speechSynthesizer.speak(getResources().getString(R.string.tts_hr_fea1));
                                        if((count%5) == 0){
                                            callphone(phonenum);
                                        }
                                        count++;
                                        break;
                                    case 2:
                                        speechSynthesizer.speak(getResources().getString(R.string.tts_hr_fea2));
                                        break;
                                    case 3:
                                        speechSynthesizer.speak(getResources().getString(R.string.tts_hr_fea3));
                                        break;
                                    case 4:
                                        speechSynthesizer.speak(getResources().getString(R.string.tts_hr_fea4));
                                        break;
                                }
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if (indexTems.length > 0) {
                        for (int i = 0; i < indexTems.length - 1; i++) {
                            if (Double_tiwen > indexTems[i].getTemperature_min()
                                    && Double_tiwen < indexTems[i].getTemperature_max()) {
                                switch (indexTems[i].getFeature_id()) {
                                    case 1:
                                        speechSynthesizer.speak(getResources().getString(R.string.tts_tem_fea1));
                                        callphone(phonenum);
                                        break;
                                    case 2:
                                        speechSynthesizer.speak(getResources().getString(R.string.tts_tem_fea2));
                                        break;
                                    case 3:
                                        speechSynthesizer.speak(getResources().getString(R.string.tts_tem_fea3));
                                        break;
                                }
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


                String str_xinlv = String.format(getResources().getString(R.string.xinlv), Int_xinlv);
                String str_tiwen = String.format(getResources().getString(R.string.tiwen), Double_tiwen);

                tiwen.setText(str_tiwen);
                xinlv.setText(str_xinlv);


                String uptime = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss", Locale.CHINA)
                        .format(new Date(System.currentTimeMillis()));
                long result = heartRateDao.InsertMessage(Int_xinlv, Double_tiwen, current_userId, uptime);

                if (result > 0) {
                    Log.d("HeartRate", "insert sucess!");
                }
            }
        }
    }


    private void setHistory() {
//        long a = historyDao.InsertMessage(1, 78, 36.7, "2018年06月07日 10:55:17");
//        long b = historyDao.InsertMessage(1, 79, 37.1, "2018年06月08日 10:55:17");
//        long c = historyDao.InsertMessage(1, 81, 37.3, "2018年06月09日 10:55:17");
//        long d = historyDao.InsertMessage(1, 77, 37.0, "2018年06月10日 10:55:17");
//        long e = historyDao.InsertMessage(1, 84, 36.8, "2018年06月11日 10:57:15");
//        long f = historyDao.InsertMessage(1, 85, 37.2, "2018年06月12日 10:57:15");
//        long g = historyDao.InsertMessage(1, 78, 37.4, "2018年06月13日 10:57:15");
//        long h = historyDao.InsertMessage(1, 76, 37.3, "2018年06月14日 10:57:15");
//        long i = historyDao.InsertMessage(1, 79, 37.0, "2018年06月15日 10:55:17");
//        long j = historyDao.InsertMessage(1, 81, 37.4, "2018年06月17日 10:55:17");
//        long k = historyDao.InsertMessage(1, 74, 36.8, "2018年06月19日 10:55:17");
//        if (a>0&&b>0&&c>0&&d>0&&e>0&&f>0&&g>0&&h>0&&i>0&&j>0&&k>0){
//            Log.d("111","historyDao insert success!");
//        }


        String oldtime = "";
        int heartrate = 0;
        int avg_heartrate = 0;
        double temperature = 0;
        double avg_temperature = 0;
        Cursor cursor = heartRateDao.getbyUserId(current_userId);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            oldtime = cursor.getString(cursor.getColumnIndex(D.Vehicle_HeartRate.UPLOAD_TIME)).substring(0, 11);
        }
        @SuppressLint("SimpleDateFormat") String currenttime = new SimpleDateFormat("yyyy年MM月dd日")
                .format(new Date(System.currentTimeMillis()));
        if (!oldtime.equals(currenttime) && !oldtime.equals("")) {
            cursor = heartRateDao.getbyTimeId(current_userId, oldtime);
            if (cursor.getCount() > 0) {
                for (float m = 0; m < cursor.getCount(); m++) {
                    if (cursor.moveToNext()) {
                        heartrate += cursor.getInt(2);
                        temperature += cursor.getDouble(3);
                    }
                }
                avg_heartrate = heartrate / cursor.getCount();
                avg_temperature = temperature / cursor.getCount();

                long result = historyDao.InsertMessage(current_userId, avg_heartrate, avg_temperature, oldtime);
                long result2 = heartRateDao.DeleteMessage(current_userId);
                if (result > 0) {
                    Log.d("22", "history insert sucess!");
                }
                if (result2 > 0) {
                    Log.d("22", "delete sucess!");
                }
                /*向服务器端传输平均心率和平均体温
                 **/
                JSONObject userObject1 = new JSONObject();
                JSONObject userObject2 = new JSONObject();
                JSONArray userArray = new JSONArray();
                try {
                    userObject1.put("temperatureAVG", avg_temperature);
                    userObject1.put("updateTime", oldtime);
                    userObject1.put("userId", current_userId);
                    userObject1.put("heartRateAVG", avg_heartrate);

                    userArray.put(userObject1);
                    userObject2.put("code", 200).put("data", userArray).put("status", "成功");
                    System.out.print(userObject2);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                String url_History = "http://candidxd.com:8080/history";
                JsonObjectRequest mRequestH = new JsonObjectRequest(Request.Method.POST, url_History, userObject2, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("yl", "correct resp:" + response.toString());
                        Toast.makeText(MainActivity.this, response.optString("msg"), Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("yl", "volley error:" + volleyError.toString());
                    }
                });
                mRequestH.setTag("myPost");
                Volley.getRequestQueue().add(mRequestH);

            }
        }

    }

    public void setNavBarColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, color));
        }
    }

    /**
     * 跳转到拨号界面，并自动拨打设定的电话号码
     *
     * @param number 当前用户设定的应急联系人的电话
     */
    public void callphone(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        startActivity(intent);
        finish();
    }

    /**
     * 初始化tts语音播报的参数
     */
    public void init_TTS() {
        speechSynthesizer = SpeechSynthesizer.getInstance();
        speechSynthesizer.setContext(MainActivity.this);
        speechSynthesizer.setAppId(appId);
        speechSynthesizer.setApiKey(appKey, secretKey);
        speechSynthesizer.auth(TtsMode.MIX);
        speechSynthesizer.initTts(TtsMode.MIX);
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "15");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, "MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI");
        speechSynthesizer.setSpeechSynthesizerListener(this);

    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.CAMERA,
                Manifest.permission.VIBRATE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限
            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    /**
     * @param view     圆环视图
     * @param duration 动画持续时长
     */
    public void SetShowAnimation(View view, int duration) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        view.startAnimation(alphaAnimation);
    }


    /**
     * 播放和合成监听事件
     *
     * @param s
     */
    @Override
    public void onSynthesizeStart(String s) {
        IsSynthesize = true;
        System.out.println("synthesizeStart!!!!");
    }

    @Override
    public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {

    }

    @Override
    public void onSynthesizeFinish(String s) {
        IsSynthesize = false;
        System.out.println("synthesizeFinish!!!!");
    }

    @Override
    public void onSpeechStart(String s) {
        IsPlay = true;
        System.out.println("speechStart");
    }

    @Override
    public void onSpeechProgressChanged(String s, int i) {

    }

    @Override
    public void onSpeechFinish(String s) {
        speechSynthesizer.stop();
        IsPlay = false;
    }

    @Override
    public void onError(String s, SpeechError speechError) {

    }
}
