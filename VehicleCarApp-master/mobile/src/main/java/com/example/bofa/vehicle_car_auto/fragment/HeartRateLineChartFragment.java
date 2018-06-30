package com.example.bofa.vehicle_car_auto.fragment;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.bofa.vehicle_car_auto.CustomViewPager;
import com.example.bofa.vehicle_car_auto.R;
import com.example.bofa.vehicle_car_auto.Volley;
import com.example.bofa.vehicle_car_auto.sqlite.D;
import com.example.bofa.vehicle_car_auto.sqlite.Dao.HistoryDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PreviewLineChartView;


/**
 * Created by pc on 2018/5/8.
 */

public class HeartRateLineChartFragment extends Fragment {
    private LineChartView chart;
    private PreviewLineChartView previewChart;
    private LineChartData data;
    private LineChartData previewData;
    private HistoryDao dao;
    private Button more;
    private TextView notes;
    private CustomViewPager viewPager;
    private int current_Id;
    private String note;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_heartrate_line_chart, container, false);
        chart = (LineChartView) rootView.findViewById(R.id.line_chart);
        previewChart = (PreviewLineChartView) rootView.findViewById(R.id.line_chart_preview);
        generateDefaultData();
        chart.setLineChartData(data);
        chart.setZoomEnabled(false);
        chart.setScrollEnabled(false);
        previewChart.setLineChartData(previewData);
        previewChart.setViewportChangeListener(new ViewportListener());
        previewX(false);

        //获取服务器端数据
        String url = "http://candidxd.com:8080/analysis";
        StringRequest mRequestHR = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("yl", "correct resp:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // 获取返回码，200表示成功
                    int retCode = jsonObject.getInt("code");
                    Log.d("yl", "code:" + retCode);
                    if (retCode == 200) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        note = data.getString("text");
                        System.out.println(note);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("yl","volley error:"+volleyError.toString());
            }
        });
        Volley.getRequestQueue().add(mRequestHR);

        more = (Button) rootView.findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                View view = View.inflate(getActivity(), R.layout.dialog_suggest,null);
                notes = (TextView) view.findViewById(R.id.note);
                dialog.setView(view,0,0,0,0);
                Window window = dialog.getWindow();
                window.getDecorView().setPadding(0,0,0,0);
                window.requestFeature(Window.FEATURE_NO_TITLE);
                window.setWindowAnimations(R.style.bottomShow);
                WindowManager.LayoutParams windowparams = window.getAttributes();
                window.setGravity(Gravity.BOTTOM);
                windowparams.width = WindowManager.LayoutParams.MATCH_PARENT;
                windowparams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setBackgroundDrawableResource(android.R.color.transparent);
                window.setAttributes(windowparams);
                //todo:dialog样式修改，xml文件里面添加控件，美观，大气，上档次
//                notes.setBackgroundColor(getResources().getColor(R.color.circle_wave));
                notes.setText(note);
                dialog.show();
            }
        });

        viewPager = (CustomViewPager) getActivity().findViewById(R.id.viewpager);
        previewChart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int n=event.getPointerCount();
                if(n==1){
                    //允许ScrollView截断点击事件，ScrollView可滑动
                    viewPager.setScanScroll(true);
                }
                else{
                    //不允许ScrollView截断点击事件，点击事件由子View处理
                    viewPager.setScanScroll(false);
                }
                return false;
            }
        });
        return rootView;
    }
    public void getCurrentId(int current_Id){
        this.current_Id = current_Id;
    }

    private void generateDefaultData() {
        List<PointValue> values = new ArrayList<PointValue>();
        List<AxisValue> axisXValuess = new ArrayList<>();
        List<AxisValue> axisYValuess = new ArrayList<>();

        dao = HistoryDao.getHistoryDao(getActivity());
        Cursor cursor = dao.getbyUserId(current_Id);
        for (int i = 0; i <cursor.getCount(); i++){
            if(cursor.moveToNext()) {
                axisXValuess.add(new AxisValue(i).setLabel(formatDays(cursor.getString(cursor.getColumnIndex(D.Vehicle_History.UPLOAD_TIME)))));
                values.add(new PointValue(i, (float)cursor.getInt(cursor.getColumnIndex(D.Vehicle_History.USER_AVGHeartRate))));

            }
        }

        Line line = new Line(values);
        line.setColor(getResources().getColor(R.color.orange));//todo:折线的颜色
        line.setHasPoints(false);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);
        data = new LineChartData(lines);

        data.setAxisXBottom(new Axis(axisXValuess));
        for(float i = 50; i < 110; i+= 2){
            axisYValuess.add(new AxisValue(i).setLabel(String.valueOf(i)));
        }
        data.setAxisYLeft(new Axis(axisYValuess).setName("Heart Rate [次]").setHasLines(true));

        // prepare preview data, is better to use separate deep copy for preview chart.
        // Set color to grey to make preview area more visible.
        previewData = new LineChartData(data);
        previewData.getLines().get(0).setColor(ChartUtils.DEFAULT_DARKEN_COLOR);//todo:历史折线下半部分总数据线条的颜色

    }

    private void previewX(boolean animate) {
        Viewport tempViewport = new Viewport(chart.getMaximumViewport());
        float dx = tempViewport.width() / 4;
        tempViewport.inset(dx, 0);
        if (animate) {
            previewChart.setCurrentViewportWithAnimation(tempViewport);
        } else {
            previewChart.setCurrentViewport(tempViewport);
        }
        previewChart.setZoomType(ZoomType.HORIZONTAL);
    }

    private void previewXY() {
        // Better to not modify viewport of any chart directly so create a copy.
        Viewport tempViewport = new Viewport(chart.getMaximumViewport());
        // Make temp viewport smaller.
        float dx = tempViewport.width() / 4;
        float dy = tempViewport.height() / 4;
        tempViewport.inset(dx, dy);
        previewChart.setCurrentViewportWithAnimation(tempViewport);
    }

    private void previewY() {
        Viewport tempViewport = new Viewport(chart.getMaximumViewport());
        float dy = tempViewport.height() / 4;
        tempViewport.inset(0, dy);
        previewChart.setCurrentViewportWithAnimation(tempViewport);
        previewChart.setZoomType(ZoomType.VERTICAL);
    }

    private class ViewportListener implements ViewportChangeListener {

        @Override
        public void onViewportChanged(Viewport newViewport) {
            // don't use animation, it is unnecessary when using preview chart.
            chart.setCurrentViewport(newViewport);
        }

    }

    private String formatDays(String value) {
        StringBuilder sb = new StringBuilder();
        String mouth = value.substring(5,7);
        String day = value.substring(8,10);
        sb.append(mouth).append("月");
        sb.append(day).append("日");
        return sb.toString();
    }
}
