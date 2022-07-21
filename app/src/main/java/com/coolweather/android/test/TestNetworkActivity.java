package com.coolweather.android.test;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.coolweather.android.R;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * 没用！！！！没用！！！！没用！！！！没用！！！！没用！！！！
 */
public class TestNetworkActivity extends AppCompatActivity implements View.OnClickListener {

    private ControlNetwork controlNetwork = new ControlNetwork();
    private Button btnNetwork;
    private Button btnGetInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_network);
        btnNetwork = findViewById(R.id.btn_network);
        btnGetInfo = findViewById(R.id.btn_getInfo);
        btnNetwork.setOnClickListener(this);
        btnGetInfo.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_network:
                requestBluetoothPermission();
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                boolean enabled = adapter.isEnabled();
                if (!enabled){
                    adapter.enable();
                }
                break;
            case R.id.btn_getInfo:
                Intent intent = new Intent(TestNetworkActivity.this,WebViewTestActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 没用！！！！
     * 打开设置页面
     */
    public void openSet(){
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.setClassName("com.android.phone","com.android.phone.MobileNetworkSettings");
        startActivity(intent);
    }

//android关闭或开启移动网络数据（关闭后，设备不可以上网，但可以打电话和发短信）
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setMobileDataEnabled() throws IOException {
        Socket socket = new Socket();
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        cm.getActiveNetwork().bindSocket(socket);
        cm.setNetworkPreference(ConnectivityManager.TYPE_WIFI);
//        ActivityCompat.requestPermissions(TestNetworkActivity.this,new String[]{Manifest.permission.CHANGE_NETWORK_STATE},1);
    }

    /**
     * 没用！！！！
     * @param enabled
     * @return
     */
    public boolean setMobileDataEnabled(boolean enabled) {
        final TelephonyManager mTelManager;
        mTelManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        try {
            @SuppressLint("SoonBlockedPrivateApi") Method m = mTelManager.getClass().getDeclaredMethod("getITelephony"); m.setAccessible(true);
            Object telephony = m.invoke(mTelManager);
            m = telephony.getClass().getMethod((enabled ? "enable" : "disable") + "DataConnectivity");
            m.invoke(telephony);
            return true;
        } catch (Exception e) {
            Log.e("", "cannot fake telephony", e);
            return false;
        }
    }

    private static final int REQUEST_BLUETOOTH_PERMISSION=10;

    /**
     * 蓝牙连接权限申请
     */
    @RequiresApi(api = Build.VERSION_CODES.S)
    public void requestBluetoothPermission(){
        //判断系统版本
        if (Build.VERSION.SDK_INT >= 23) {
            //检测当前app是否拥有某个权限
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.BLUETOOTH_CONNECT);
            //判断这个权限是否已经授权过
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                //判断是否需要 向用户解释，为什么要申请该权限
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.BLUETOOTH_CONNECT))
                    Toast.makeText(this,"Need bluetooth permission.",
                            Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this ,new String[]
                        {Manifest.permission.BLUETOOTH_CONNECT,Manifest.permission.BLUETOOTH},REQUEST_BLUETOOTH_PERMISSION);
            }
        }
    }

    private void requestBluetoothPermission1(){
        //判断系统版本
        if (Build.VERSION.SDK_INT >= 23) {
            //检测当前app是否拥有某个权限
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
            //判断这个权限是否已经授权过
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                //判断是否需要 向用户解释，为什么要申请该权限
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION))
                    Toast.makeText(this,"Need bluetooth permission.",
                            Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this ,new String[]
                        {Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_BLUETOOTH_PERMISSION);
                return;
            }else{
            }
        } else {
        }
    }

}