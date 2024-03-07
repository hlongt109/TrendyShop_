package com.trendyshopteam.trendyshop.interfaces;

import android.content.Context;

public interface SignupInterface {
    Context getContext();
    void signUpSucess();
    void signUpFailed();
    void emailExist();
    void nameError();
    void emailError();
    void passError();
    void passNotMatch();
    void setLoading();
    void stopLoading();
    void clearErr();
}
