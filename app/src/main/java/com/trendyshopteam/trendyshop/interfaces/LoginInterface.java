package com.trendyshopteam.trendyshop.interfaces;

import android.content.Context;

public interface LoginInterface {
    Context getContext();
    void loginSuccess();
    void loginFailure();
    void emailError();
    void passwordError();
    void setLoading();
    void stopLoading();
    void clearError();
}
