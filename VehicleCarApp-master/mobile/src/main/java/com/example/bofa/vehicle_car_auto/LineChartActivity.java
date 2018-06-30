package com.example.bofa.vehicle_car_auto;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;

import com.example.bofa.vehicle_car_auto.fragment.HeartRateLineChartFragment;
import com.example.bofa.vehicle_car_auto.fragment.TemperatureLineChartFragment;


/**
 * Created by pc on 2018/5/8.
 */

public class LineChartActivity extends FragmentActivity {
    private CustomViewPager mViewPager;
    private Fragment[] mFragments;
    private TabLayout tabLayout;
    public int current_userId;
    HeartRateLineChartFragment heartRateLineChartFragment = new HeartRateLineChartFragment();
    TemperatureLineChartFragment temperatureLineChartFragment = new TemperatureLineChartFragment();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line_chart);
        mViewPager = (CustomViewPager) findViewById(R.id.viewpager);
        init();
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(mViewPager);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    private void init(){
        setNavBarColor(R.color.circle_wave);//设置手机底部导航背景

        current_userId = getIntent().getIntExtra("current_userId",0);
        mFragments = new Fragment[2];
        mFragments[0] = heartRateLineChartFragment;
        mFragments[1] = temperatureLineChartFragment;

        heartRateLineChartFragment.getCurrentId(current_userId);
        temperatureLineChartFragment.getCurrent_Id(current_userId);

        MySimpleFragmentPagerAdapter mySimpleFragmentPagerAdapter = new MySimpleFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mySimpleFragmentPagerAdapter);
    }
    private class MySimpleFragmentPagerAdapter extends FragmentPagerAdapter {
        private CharSequence tabTitles[] = new CharSequence[]{getString(R.string.heartrate),getString(R.string.temperature)};
        MySimpleFragmentPagerAdapter(FragmentManager fm){
            super(fm);
        }
        @Override
        public Fragment getItem(int position){
            return mFragments[position];
        }

        @Override
        public int getCount(){
            return mFragments.length;
        }
        @Override
        public CharSequence getPageTitle(int position){
            return tabTitles[position];
        }
    }

    public void setNavBarColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, color));
        }
    }
}
