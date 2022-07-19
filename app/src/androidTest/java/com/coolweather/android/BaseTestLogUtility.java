package com.coolweather.android;

import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public class BaseTestLogUtility {

    public static void showWeatherActivityTestSituation(String str){
        Log.d("weatherActivity",str);
    }

    /**
     * 判断温度是否在正常范畴内
     * @param string 温度信息
     * @param tem 具体温度
     */
    public static void judgeTem(String string,int tem){
        if ( tem>-100 && tem<100 ){
            showWeatherActivityTestSituation(string+"：" + tem + "℃" + "-->" + "正常");
        }else {
            showWeatherActivityTestSituation(string+"：" + tem + "℃" + "-->" + "不正常");
        }
    }

    /**
     * 判断天气信息是否正常
     */
    public static void judgeInfo(String where,String s){
        Set<String> set = new HashSet<>();
        set.add("多云");
        set.add("晴");
        set.add("阴");
        set.add("小雨");
        set.add("中雨");
        set.add("大雨");
        set.add("阵雨");
        set.add("雷阵雨");
        if (set.contains(s)){
            showWeatherActivityTestSituation(where+"天气信息："+s+"-->"+"正常");
        }else{
            showWeatherActivityTestSituation(where+"天气信息："+s+"-->"+"出错");
        }
    }

    /**
     * 判断返回信息是否正常
     */
    public static void judgeResponse(String s){
        if (s.equals("ok")){
            showWeatherActivityTestSituation("服务器返回结果正常");
        }else{
            showWeatherActivityTestSituation("服务器返回结果出错");
        }
    }
}
