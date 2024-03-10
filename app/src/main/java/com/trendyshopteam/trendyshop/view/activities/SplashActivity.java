package com.trendyshopteam.trendyshop.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.interfaces.SplashInterface;
import com.trendyshopteam.trendyshop.presenter.SplashPresenter;
import com.trendyshopteam.trendyshop.view.activities.UserActivitys.MainUserActivity;

public class SplashActivity extends AppCompatActivity implements SplashInterface {
    private SplashPresenter splashPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashPresenter = new SplashPresenter(this);

        splashPresenter.checkLogged();
    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void isLoggedIn() {
        Toast.makeText(this, "Welcome to Trendy Shop", Toast.LENGTH_SHORT).show();
        splashPresenter.switchScreen();
    }

    @Override
    public void switchToAdmin() {
        startActivity(new Intent(this, MainManager.class));
        finishAffinity();
    }

    @Override
    public void switchToUser() {
        startActivity(new Intent(this, MainUserActivity.class));
        finishAffinity();
    }
}