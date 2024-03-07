package com.trendyshopteam.trendyshop.interfaces;

import android.app.Activity;
import android.content.Context;

public interface UserManageInterface {
    Context getContext();
    Activity getActivity();
    void showLoading();
    void hideLoading();

}
