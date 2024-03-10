package com.trendyshopteam.trendyshop.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.trendyshopteam.trendyshop.interfaces.SplashInterface;
import com.trendyshopteam.trendyshop.utilities.SharePreferencesManage;
import com.trendyshopteam.trendyshop.view.activities.LoginActivity;
import com.trendyshopteam.trendyshop.view.activities.MainActivity;

public class SplashPresenter {
    private SharePreferencesManage sharePreferencesManage;
    private SplashInterface splashInterface;

    public SplashPresenter(SplashInterface splashInterface) {
        this.splashInterface = splashInterface;
    }

    public void checkLogged() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        new Handler().postDelayed(() -> {
            if (user != null) {

                splashInterface.isLoggedIn();

            } else {
                splashInterface.getContext().startActivity(new Intent(splashInterface.getContext(), LoginActivity.class));
            }
        }, 4000);
    }

    public void switchScreen() {
        sharePreferencesManage = new SharePreferencesManage(splashInterface.getContext());
        if (sharePreferencesManage.getPosition().equals("Admin") || sharePreferencesManage.getPosition().equals("NhanVien")) {
            splashInterface.switchToAdmin();
        }
        if (sharePreferencesManage.getPosition().equals("User")) {
            splashInterface.switchToUser();
        }
    }
}
