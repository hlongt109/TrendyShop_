package com.trendyshopteam.trendyshop.interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public interface PayOrderInterface {
    Intent getIntent();
    Context getContext();
    Activity getActivity();
    void customerNameEmpty();
    void customerPhoneEmpty();
    void customerAddressEmpty();
    void payMethodEmpty();
    void clearError();
    void showLoading();
    void hideLoading();
    void returnSelectedPayment(String payMethod);
    void returnTotalPrice(String total);
    void orderSuccess();
}
