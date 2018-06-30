package com.example.bofa.vehicle_car_auto;


import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.bofa.vehicle_car_auto.fragment.EndSlide;
import com.example.bofa.vehicle_car_auto.fragment.FirstSlide;
import com.example.bofa.vehicle_car_auto.fragment.SecondSlide;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by Bofa on 2018/5/14.
 */

public class AppIntro extends AppIntro2 {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
//        addSlide(firstFragment);
//        addSlide(secondFragment);
//        addSlide(thirdFragment);
//        addSlide(fourthFragment);

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
//        addSlide(AppIntroFragment.newInstance("实时监控", "实时监控您的健康体征，为您安全行驶保驾护航", R.drawable.now1, getResources().getColor(R.color.circle_wave)));
//        addSlide(AppIntroFragment.newInstance("历史分析", "结合云端大数据与历史数据对历史趋势进行分析", R.drawable.ic_navigate_before_white, getResources().getColor(R.color.circle_wave)));
//        addSlide(AppIntroFragment.newInstance("紧急联系人", "录入紧急联系人，如遇突发状况，将通知紧急联系人", R.drawable.ic_navigate_before_white, getResources().getColor(R.color.circle_wave)));

        addSlide(new FirstSlide());
        addSlide(new SecondSlide());
        addSlide(new EndSlide());
        // OPTIONAL METHODS

        // SHOW or HIDE the statusbar
        showStatusBar(false);
        setProgressButtonEnabled(true);
        // Edit the color of the nav bar on Lollipop+ devices
        setNavBarColor(R.color.circle_wave);

        // Turn vibration on and set intensity
        // NOTE: you will need to ask VIBRATE permission in Manifest if you haven't already
        setVibrate(true);
        setVibrateIntensity(1000);

        // Animations -- use only one of the below. Using both could cause errors.
//        setFadeAnimation(); // OR
//        setZoomAnimation(); // OR
//        setFlowAnimation(); // OR
//        setSlideOverAnimation(); // OR
//        setDepthAnimation(); // OR

        // Permissions -- takes a permission and slide number
        askForPermissions(new String[]{Manifest.permission.CAMERA}, 3);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
