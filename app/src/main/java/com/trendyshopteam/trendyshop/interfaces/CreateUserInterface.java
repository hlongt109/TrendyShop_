package com.trendyshopteam.trendyshop.interfaces;

import android.app.Activity;
import android.content.Intent;

public interface CreateUserInterface {
    Activity getActivity();
    void isUsernameEmpty();
    void isUsernameExists();
    void isPassEmpty();
    void isNameEmpty();
    void isEmailEmpty();
    void isRdoNull();
    void isImgNull();
    void clearUsernameErr();
    void clearPasswordErr();
    void clearEmailErr();
    void clearNameErr();
    void addSuccess();
    void addFailure();

    void uploadImageFailure();
}
