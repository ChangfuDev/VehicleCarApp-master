package com.example.bofa.vehicle_car_auto;

/**
 * Created by Bofa on 2018/5/5.
 */

public class MathRandom {
    public static double rate5_6 = 0.0135;
    public static double rate6_7 = 0.2235;
    public static double rate7_8 = 0.6665;
    public static double rate8_9 = 0.0815;
    public static double rate9_10 = 0.0200;
    public static double rate10_11 = 0.0050;
    public static boolean flag1 = false; //50-60的状态符
    public static boolean flag2 = false; //60-70的状态符
    public static boolean flag3 = false; //70-80的状态符
    public static boolean flag4 = false; //80-90的状态符
    public static boolean flag5 = false; //90-100的状态符
    public static boolean flag6 = false; //100-110的状态符
    public static double rate_tiwen1 = 0.001;
    public static double rate_tiwen2 = 0.010;
    public static double rate_tiwen3 = 0.05;
    public static double rate_tiwen4 = 0.9;
    private double randomNumber;
    private double randomNumber2;
    public double[]piancha = new double[]{-0.3,-0.2,-0.1,0,0.1,0.2,0.3};
    private int xinlv;
    private boolean flag;
    private double tiwen = DEFAULT_TIWEN;
    private static final double DEFAULT_TIWEN = 36.8;

    public MathRandom(){
        flag = true;
    }

    public double PercentageRandom2(){
        randomNumber2 = Math.random();
        if(randomNumber2 >= 0 && randomNumber2 < rate_tiwen1){
            if(tiwen < DEFAULT_TIWEN){
                tiwen += piancha[6];
            }else{
                tiwen += piancha[0];
            }
        }else if(randomNumber2 >= rate_tiwen1 && randomNumber2 < rate_tiwen1 + rate_tiwen2){
            if(tiwen < DEFAULT_TIWEN){
                tiwen += piancha[5];
            }else{
                tiwen += piancha[1];
            }
        }else if(randomNumber2 >= rate_tiwen1 + rate_tiwen2 && randomNumber2 < rate_tiwen1 + rate_tiwen2 + rate_tiwen3){
            if(tiwen < DEFAULT_TIWEN){
                tiwen += piancha[4];
            }else{
                tiwen += piancha[2];
            }
        }else if(randomNumber2 >= rate_tiwen1 + rate_tiwen2 + rate_tiwen3 && randomNumber2 < rate_tiwen1 + rate_tiwen2 + rate_tiwen3
                +rate_tiwen4){
            tiwen += piancha[3];
        }
        return tiwen;

    }
    public int PercentageRandom()
    {
        randomNumber = Math.random();
        if (randomNumber >= 0 && randomNumber < rate5_6)
        {
            setFlag2();
            flag1 = true;
            change();
            if(!flag){
                if(xinlv < 55){
                    xinlv = (int)(Math.random()*5+50);
                }else{
                    xinlv = (int)(Math.random()*5+55);
                }
            }else{
                xinlv = (int)(Math.random()*10+50);
                flag = false;
            }
            return xinlv;
        }
        else if (randomNumber >= rate5_6 && randomNumber < rate6_7 + rate5_6)
        {
            setFlag1();
            setFlag3();
            flag2 = true;
            change();
            if(!flag){
                if(xinlv < 65){
                    xinlv = (int)(Math.random()*5+60);
                }else{
                    xinlv = (int)(Math.random()*5+65);
                }
            }else{
                xinlv = (int)(Math.random()*10+60);
                flag = false;
            }
            return xinlv;

        }
        else if (randomNumber >= rate5_6 + rate6_7
                && randomNumber < rate5_6 + rate6_7 + rate7_8)
        {
            setFlag2();
            setFlag4();
            flag3 = true;
            change();
            if(!flag){
                if(xinlv < 75){
                    xinlv = (int)(Math.random()*5+70);
                }else{
                    xinlv = (int)(Math.random()*5+75);
                }
            }else{
                xinlv = (int)(Math.random()*10+70);
                flag = false;
            }
            return xinlv;
        }
        else if (randomNumber >= rate5_6 + rate6_7 + rate7_8
                && randomNumber < rate5_6 + rate6_7 + rate7_8 + rate8_9 )
        {
            setFlag3();
            setFlag5();
            flag4 = true;
            change();
            if(!flag){
                if(xinlv < 85){
                    xinlv = (int)(Math.random()*5+80);
                }else{
                    xinlv = (int)(Math.random()*5+85);
                }
            }else{
                xinlv = (int)(Math.random()*10+80);
                flag = false;
            }
            return xinlv;

        }
        else if (randomNumber >= rate5_6 + rate6_7 + rate7_8 + rate8_9
                && randomNumber <  rate5_6 + rate6_7 + rate7_8 + rate8_9 + rate9_10 )
        {
            setFlag4();
            setFlag6();
            flag5 = true;
            change();
            if(!flag){
                if(xinlv < 95){
                    xinlv = (int)(Math.random()*5+90);
                }else{
                    xinlv = (int)(Math.random()*5+95);
                }
            }else{
                xinlv = (int)(Math.random()*10+90);
                flag = false;
            }
            return xinlv;

        }
        else if (randomNumber >= rate5_6 + rate6_7 + rate7_8 + rate8_9 + rate9_10
                && randomNumber < rate5_6 + rate6_7 + rate7_8 + rate8_9 + rate9_10 + rate10_11 )
        {
            setFlag5();
            flag6 = true;
            change();
            if(!flag){
                if(xinlv < 105){
                    xinlv = (int)(Math.random()*5+100);
                }else{
                    xinlv = (int)(Math.random()*5+105);
                }
            }else{
                xinlv = (int)(Math.random()*10+100);
                flag = false;
            }
            return xinlv;

        }
        return -1;
    }

    public void change(){
        if(flag1){//50-60
            rate5_6 = 0.5;
            rate6_7 = 0.48;
            rate7_8 = 0.02;
            rate8_9 = 0;
            rate9_10 = 0;
            rate10_11 = 0;
        }
        if(flag2){//60-70
            rate5_6 = 0.005;
            rate6_7 = 0.7;
            rate7_8 = 0.295;
            rate8_9 = 0;
            rate9_10 = 0;
            rate10_11 = 0;
        }
        if(flag3){//70-80
            rate5_6 = 0;
            rate6_7 = 0.085;
            rate7_8 = 0.9;
            rate8_9 = 0.015;
            rate9_10 = 0;
            rate10_11 = 0;
        }
        if(flag4){//80-90
            rate5_6 = 0;
            rate6_7 = 0;
            rate7_8 = 0.095;
            rate8_9 = 0.9;
            rate9_10 = 0.005;
            rate10_11 = 0;
        }
        if(flag5){//90-100
            rate5_6 = 0;
            rate6_7 = 0;
            rate7_8 = 0;
            rate8_9 = 0.390;
            rate9_10 = 0.6;
            rate10_11 = 0.01;
        }
        if(flag6){//100-110
            rate5_6 = 0;
            rate6_7 = 0;
            rate7_8 = 0;
            rate8_9 = 0.380;
            rate9_10 = 0.6;
            rate10_11 = 0.02;
        }
    }

    public void setFlag1(){
        if(flag1){
            flag1 = false;
        }
    }
    public void setFlag2(){
        if(flag2){
            flag2 = false;
        }
    }
    public void setFlag3(){
        if(flag3){
            flag3 = false;
        }
    }
    public void setFlag4(){
        if(flag4){
            flag4 = false;
        }
    }
    public void setFlag5(){
        if(flag5){
            flag5 = false;
        }
    }
    public void setFlag6(){
        if(flag6){
            flag6 = false;
        }
    }
}
