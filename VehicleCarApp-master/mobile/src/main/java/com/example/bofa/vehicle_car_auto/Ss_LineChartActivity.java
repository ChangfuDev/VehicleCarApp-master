package com.example.bofa.vehicle_car_auto;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.example.bofa.vehicle_car_auto.fragment.Ss_LineChartFragment;

/**
 * Created by pc on 2018/5/10.
 */

public class Ss_LineChartActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ss_line_chart);
        Ss_LineChartFragment ss_lineChartFragment = new Ss_LineChartFragment();
        ss_lineChartFragment.getCurrent_Id(getIntent().getIntExtra("current_userId",0));
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, ss_lineChartFragment).commit();
        }
        setNavBarColor(R.color.circle_wave);//设置手机底部导航背景
    }

    public void setNavBarColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, color));
        }
    }
}
