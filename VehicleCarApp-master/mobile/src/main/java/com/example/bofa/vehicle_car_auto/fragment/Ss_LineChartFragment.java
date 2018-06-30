package com.example.bofa.vehicle_car_auto.fragment;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.bofa.vehicle_car_auto.R;
import com.example.bofa.vehicle_car_auto.sqlite.D;
import com.example.bofa.vehicle_car_auto.sqlite.Dao.HeartRateDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by pc on 2018/5/10.
 */

public class Ss_LineChartFragment extends Fragment {
    private LineChartView chart;
    private LineChartData data;
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private List<AxisValue> mAxisYValues = new ArrayList<AxisValue>();
    private List<Line> lines = new ArrayList<Line>();;
    private HeartRateDao dao;
    private int current_userId;
    Timer timer;
    TimerTask timerTask;
    private float x;
    private float DownX = 0,UpX = 0,m = 0;
    private float width = 0,num = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_line_chart, container, false);
        chart = (LineChartView) rootView.findViewById(R.id.h_line_chart);
        /*
        获取屏幕的分辨率，并确定一个点
         */
        WindowManager manager = getActivity().getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        width = outMetrics.widthPixels;
        num = (width-200) - (width-200)/138;
        Log.d("test","标准点："+String.valueOf(num));

        startTimer();

        chart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int n=event.getPointerCount();
                if(n==1){
                    stopTimer();
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    DownX = event.getX();
                    Log.d("test","按下点："+String.valueOf(DownX));
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    UpX = event.getX() - DownX;
                    Log.d("test","移动距离："+String.valueOf(UpX));
                    m = num + UpX;
                    Log.d("test","m："+String.valueOf(m));
                }
                if (m < num){
                    startTimer();
                }
                return false;
            }
        });

        return rootView;
    }
    public void getCurrent_Id(int current_Id){
        this.current_userId = current_Id;
    }
    private void startTimer(){
        if (timer == null){
            timer = new Timer();
        }
        if (timerTask == null){
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    lines.clear();
                    mPointValues.clear();;
                    mAxisXValues.clear();
                    mAxisYValues.clear();
                    initLineChart();
                }
            };
        }
        timer.schedule(timerTask,1000,5000);
    }

    private void stopTimer(){
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null){
            timerTask.cancel();
            timerTask = null;
        }
    }

    private void initLineChart(){
        dao = HeartRateDao.getHeartRateDao(getActivity());
        Cursor cursor = dao.getbyUserId(current_userId);
        for (float i = 0;i < cursor.getCount();i++){
            if (cursor.moveToNext()){
                mAxisXValues.add(new AxisValue(i).setLabel(formatTime(cursor.getString(cursor.getColumnIndex(D.Vehicle_HeartRate.UPLOAD_TIME)))));
                mPointValues.add(new PointValue(i,
                        (float)cursor.getInt(cursor.getColumnIndex(D.Vehicle_HeartRate.USER_HeartRate))));
            }
        }

//        Line line = new Line(mPointValues).setColor(getResources().getColor(R.color.orange));  //折线的颜色
        try {
            Line line = new Line(mPointValues).setColor(getResources().getColor(R.color.orange));  //折线的颜色
            line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
            line.setCubic(true);//曲线是否平滑
            line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
            line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
            line.setHasPoints(false);//是否显示圆点 如果为false 则没有原点只有点显示
            lines.add(line);
            data = new LineChartData(lines);
        }catch (IllegalStateException e){

        }

//        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
//        line.setCubic(true);//曲线是否平滑
//        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
//        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
//        line.setHasPoints(false);//是否显示圆点 如果为false 则没有原点只有点显示
//        lines.add(line);
//        data = new LineChartData(lines);

        data.setAxisXBottom(new Axis(mAxisXValues).setName("时间"));

        for(float i = 50; i < 110; i+= 2){
            mAxisYValues.add(new AxisValue(i).setLabel(String.valueOf(i)));
        }
        Axis axisY = new Axis();
        axisY.setName("心率");
        axisY.setValues(mAxisYValues);
        axisY.setHasLines(true);
        data.setAxisYLeft(axisY);

        //设置行为属性，支持缩放、滑动以及平移
        chart.setInteractive(true);
        chart.setOnValueTouchListener(new ValueTouchListener());
        chart.setZoomEnabled(false);
        chart.setZoomType(ZoomType.HORIZONTAL);
//        chart.setScrollEnabled(true);
        chart.setLineChartData(data);

        Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 50;
        v.top = 110;
        //固定Y轴的范围,如果没有这个,Y轴的范围会根据数据的最大值和最小值决定
        chart.setMaximumViewport(v);
        try {
            v.left = mPointValues.get(mPointValues.size()-7).getX();
            v.right = mPointValues.get(mPointValues.size()-1).getX();
            chart.setCurrentViewport(v);
        }catch (Exception e){
            e.printStackTrace();
            v.left = mPointValues.get(0).getX();
            v.right = mPointValues.get(mPointValues.size()-1).getX();
            chart.setMaximumViewport(v);
        }
        //这2个属性的设置一定要在lineChart.setMaximumViewport(v);这个方法之后,不然显示的坐标数据是不能左右滑动查看更多数据的


    }

    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }
    }

    private String formatTime(String value) {
        StringBuilder sb = new StringBuilder();
        String hour = value.substring(11,13);
        String minute = value.substring(14,16);
        String second = value.substring(17,19);
        sb.append(hour).append(':');
        sb.append(minute).append(':');
        sb.append(second);
        return sb.toString();
    }
}
