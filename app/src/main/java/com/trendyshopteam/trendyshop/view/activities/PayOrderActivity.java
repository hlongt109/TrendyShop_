package com.trendyshopteam.trendyshop.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.databinding.ActivityPayOrderBinding;
import com.trendyshopteam.trendyshop.interfaces.PayOrderInterface;
import com.trendyshopteam.trendyshop.presenter.PayOrderPresenter;

public class PayOrderActivity extends AppCompatActivity implements PayOrderInterface {
    private ActivityPayOrderBinding binding;
    private PayOrderPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPayOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new PayOrderPresenter(this);
        init();
    }
    private void init() {
        presenter.setDataOnRcv(binding.rcvOrder,presenter);

        presenter.setUserInformation(binding.edNameCustomOrder, binding.edPhoneCustomOrder,binding.edAddressCustomOrder);

        binding.btnBack.setOnClickListener(v -> onBackPressed());

        binding.tvMethodPay.setOnClickListener(v -> {
            presenter.selectPaymentMethod();
        });

        binding.btnThanhToanOrder.setOnClickListener(v -> {
            String name = String.valueOf(binding.edNameCustomOrder.getText()).trim();
            String phone = String.valueOf(binding.edPhoneCustomOrder.getText()).trim();
            String address = String.valueOf(binding.edAddressCustomOrder.getText()).trim();
            presenter.handleOrder(name,phone,address);
        });
    }
    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void customerNameEmpty() {
        binding.edNameCustomOrder.setError("Enter your name");
    }

    @Override
    public void customerPhoneEmpty() {
        binding.edPhoneCustomOrder.setError("Enter your phone number");
    }

    @Override
    public void customerAddressEmpty() {
        binding.edAddressCustomOrder.setError("Enter your address");
    }

    @Override
    public void payMethodEmpty() {
        Toast.makeText(this, "Please select payment method", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void clearError() {
        binding.edNameCustomOrder.setError(null);
        binding.edPhoneCustomOrder.setError(null);
        binding.edAddressCustomOrder.setError(null);
    }

    @Override
    public void showLoading() {
        binding.btnThanhToanOrder.setVisibility(View.INVISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        binding.btnThanhToanOrder.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void returnSelectedPayment(String payMethod) {
        binding.tvMethodPay.setText(payMethod);
    }

    @Override
    public void returnTotalPrice(String total) {
        binding.tvTotalOrder.setText(total);
    }

    @Override
    public void orderSuccess() {
        Toast.makeText(this, "Order Success", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, CartActivity.class));
        finish();
    }
}