package com.trendyshopteam.trendyshop.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.presenter.SplashPresenter;

public class SplashActivity extends AppCompatActivity {
    private SplashPresenter splashPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashPresenter = new SplashPresenter();
        splashPresenter.runSplash(this);
    }
}