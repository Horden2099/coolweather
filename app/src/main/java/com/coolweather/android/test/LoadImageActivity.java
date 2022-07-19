package com.coolweather.android.test;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.coolweather.android.R;

public class LoadImageActivity extends AppCompatActivity  {
    private ImageView mImageView;
    private boolean mIsLoadFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image);
        mImageView = (ImageView) findViewById(R.id.iv_test);

        final String url = "http://pic21.photophoto.cn/20111019/0034034837110352_b.jpg";
        Glide.with(this)
                .load(url)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        mImageView.setImageDrawable(resource);
                        mImageView.setContentDescription(url);
                        mIsLoadFinished = true;
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        mIsLoadFinished = true;
                    }
                });
    }

    public boolean isLoadFinished() {
        return mIsLoadFinished;
    }
}
