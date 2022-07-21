package com.coolweather.android;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class TestUtility {

    public static boolean networkJudge(Context context){

        ConnectivityManager connMag = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connMag.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connMag.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return wifi.isConnected() || mobile.isConnected();
    }
}
