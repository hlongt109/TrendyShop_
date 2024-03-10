package com.trendyshopteam.trendyshop.interfaces;

import android.app.Activity;
import android.content.Context;

public interface CartInterface {
    Context getContext();
    Activity getActivity();
    void showLoading();
    void hideLoading();
    void showImageEmpty();
    void hideImageEmpty();
    void showTotalPrice(String totalPrice);
    void enableBtnDeleteCart();
    void hideBtnDeleteCart();
}
