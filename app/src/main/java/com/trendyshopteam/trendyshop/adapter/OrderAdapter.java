package com.trendyshopteam.trendyshop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.databinding.ItemProductOrderBinding;
import com.trendyshopteam.trendyshop.model.Cart;
import com.trendyshopteam.trendyshop.presenter.PayOrderPresenter;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.myViewHolder>{
    private final Context context;
    private final ArrayList<Cart> list;
    private PayOrderPresenter presenter;

    public OrderAdapter(Context context, ArrayList<Cart> list, PayOrderPresenter presenter) {
        this.context = context;
        this.list = list;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductOrderBinding binding = ItemProductOrderBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new myViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Cart cart = list.get(position);
        holder.setDataOnItem(cart);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        private ItemProductOrderBinding binding;
        myViewHolder(ItemProductOrderBinding itemProductOrderBinding){
            super(itemProductOrderBinding.getRoot());
            binding = itemProductOrderBinding;
        }
        @SuppressLint("SetTextI18n")
        void setDataOnItem(Cart cart){
            Glide.with(context).load(cart.getProductImage()).error(R.drawable.image).into(binding.imgOder);
            binding.tvNameProduct.setText(cart.getProductName());
            binding.tvGia.setText(String.valueOf(cart.getProductPrice()));
            binding.tvQuantity.setText("x"+ cart.getSoluong());
        }
    }
}
