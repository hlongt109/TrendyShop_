package com.trendyshopteam.trendyshop.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.trendyshopteam.trendyshop.view.activities.MainActivity;

public class SplashPresenter {
    public void runSplash(Context context) {
        new Handler().postDelayed(() -> {
            context.startActivity(new Intent(context, MainActivity.class));
        },4000);
    }
}
