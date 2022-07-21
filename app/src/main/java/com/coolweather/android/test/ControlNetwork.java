package com.coolweather.android.test;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ControlNetwork {

    public void toggleMobileData(Context context, boolean enabled) {

        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        Class conMgrClass = null; // ConnectivityManager类

        Field iConMgrField; // ConnectivityManager类中的字段

        Object iConMgr = null; // IConnectivityManager类的引用

        Class iConMgrClass = null; // IConnectivityManager类

        Method setMobileDataEnabledMethod = null; // setMobileDataEnabled方法

        try {

            // 取得ConnectivityManager类

            conMgrClass = Class.forName(conMgr.getClass().getName());

            // 取得ConnectivityManager类中的对象mService

            iConMgrField = conMgrClass.getDeclaredField("mService");

            // 设置mService可访问

            iConMgrField.setAccessible(true);

            // 取得mService的实例化类IConnectivityManager

            iConMgr = iConMgrField.get(conMgr);

            // 取得IConnectivityManager类

            iConMgrClass = Class.forName(iConMgr.getClass().getName());

            // 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法

            setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);

            // 设置setMobileDataEnabled方法可访问

            setMobileDataEnabledMethod.setAccessible(true);

            // 调用setMobileDataEnabled方法

            setMobileDataEnabledMethod.invoke(iConMgr, enabled);

        } catch (ClassNotFoundException | NoSuchFieldException | SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {

            e.printStackTrace();

        }
    }

    public void setMobileConnectionEnabled(Context context, boolean enabled) throws Exception {
        final ConnectivityManager mConnectivityManager = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class mClass = Class.forName(mConnectivityManager.getClass().getName());
        final Field mField = mClass.getDeclaredField("mService");
        mField.setAccessible(true);
        final Object mObject = mField.get(mConnectivityManager);
        final Class mConnectivityManagerClass =  Class.forName(mObject.getClass().getName());
        final Method setMobileDataEnabledMethod = mConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);

        setMobileDataEnabledMethod.invoke(mObject, enabled);
    }

    public void setMobileDataEnabled(Context context, boolean enabled) throws Exception {
        final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class conmanClass = Class.forName(conman.getClass().getName());
        final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
        iConnectivityManagerField.setAccessible(false);
        final Object iConnectivityManager = iConnectivityManagerField.get(conman);
        final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
        final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(false);

        setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
    }
}
