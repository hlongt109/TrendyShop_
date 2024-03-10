package com.trendyshopteam.trendyshop.interfaces;

import android.content.Context;

public interface SplashInterface {
    Context getContext();
    void isLoggedIn();
    void switchToAdmin();
    void switchToUser();
}
