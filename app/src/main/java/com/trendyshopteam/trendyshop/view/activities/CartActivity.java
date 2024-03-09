package com.trendyshopteam.trendyshop.view.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.databinding.ActivityCartBinding;
import com.trendyshopteam.trendyshop.interfaces.CartInterface;
import com.trendyshopteam.trendyshop.model.Cart;
import com.trendyshopteam.trendyshop.presenter.CartPresenter;

public class CartActivity extends AppCompatActivity implements CartInterface {
    private ActivityCartBinding binding;
    private CartPresenter  presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // fake data
//        createAndSaveSampleObject("id_1", "product_id_1", "DVJKGqZYZEXvbCSGKIxr3bMl5jI2", "Product 1", "https://example.com/image_1.png", 2,500000, true,"M");
//        createAndSaveSampleObject("id_2", "product_id_2", "DVJKGqZYZEXvbCSGKIxr3bMl5jI2", "Product 2", "https://example.com/image_2.png", 4, 270000,false,"S");
//        createAndSaveSampleObject("id_3", "product_id_3", "VJKGqZYZEXvbCSGKIxr3bMl5jI2", "Product 3", "https://example.com/image_3.png", 6, 340000,true,"L");
//        createAndSaveSampleObject("id_4", "product_id_4", "DVJKGqZYZEXvbCSGKIxr3bMl5jI2", "Product 4", "https://example.com/image_4.png", 8, 680000,false,"M");
//        createAndSaveSampleObject("id_5", "product_id_5", "DVJKGqZYZEXvbCSGKIxr3bMl5jI2", "Product 5", "https://example.com/image_5.png", 10, 450000,true,"L");
        
        presenter = new CartPresenter(this);
        presenter.setDataOnRecyclerView(binding.rcvCart,presenter);
        init();
    }
    private void init() {
        binding.btnBack.setOnClickListener(v -> onBackPressed());

        binding.btnGotoCheckout.setOnClickListener(v -> {
            presenter.gotoCheckout();
        });

        binding.btnDeleteCart.setOnClickListener(v -> {
            presenter.deleteCartChecked();
        });
    }


    private void createAndSaveSampleObject(String id, String idProduct, String idKhachHang, String productName, String productImage, int soluong,double price, boolean isChecked, String size) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference cartRef = database.getReference("Cart");

        Cart cartItem = new Cart(id, idProduct, idKhachHang, productName, productImage, soluong,price, isChecked,size);

        // Save the cart item to the database
        cartRef.child(id).setValue(cartItem);
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
    public void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
       binding.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showImageEmpty() {
        binding.imgEmptyCart.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideImageEmpty() {
        binding.imgEmptyCart.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideBtnDeleteCart() {
        binding.btnDeleteCart.setVisibility(View.INVISIBLE);
    }
    @Override
    public void showTotalPrice(String totalPrice) {

        binding.tvTotal.setText(String.valueOf(totalPrice));
    }

    @Override
    public void enableBtnDeleteCart() {
        binding.btnDeleteCart.setVisibility(View.VISIBLE);
    }

}