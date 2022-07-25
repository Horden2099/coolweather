package com.coolweather.android.mockitotest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.coolweather.android.R;

public class MockitoTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mockito_test);
    }



    public int addExact(int x, int y) {
        return x + y;
    }

    public int subtractExact(int x, int y) {
        return x - y;
    }

    public int multiplyExact(int x, int y) {
        return x * y;
    }

    // TODO: zero case
    public int intDivide(int x, int y) {
        return x / y;
    }
}