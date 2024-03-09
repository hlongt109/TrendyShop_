package com.trendyshopteam.trendyshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.databinding.ItemCartBinding;
import com.trendyshopteam.trendyshop.interfaces.InterfaceUpdateCart;
import com.trendyshopteam.trendyshop.model.Cart;
import com.trendyshopteam.trendyshop.presenter.CartPresenter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.myViewHolder>{
    private final Context context;
    private final ArrayList<Cart> list;
    private CartPresenter presenter;
    InterfaceUpdateCart interfaceUpdateCart;

    public CartAdapter(Context context, ArrayList<Cart> list, CartPresenter presenter) {
        this.context = context;
        this.list = list;
        this.presenter = presenter;
    }
    public void clickToUpdate(InterfaceUpdateCart interfaceUpdateCart){
        this.interfaceUpdateCart = interfaceUpdateCart;
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new myViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Cart cart = list.get(position);
        holder.setDataOnCart(cart);
        holder.binding.imgChecked.setOnClickListener(v -> {
              // cap nhat trang thai check
              if(interfaceUpdateCart != null){
                  interfaceUpdateCart.onSetChecked(position);
              }
        });
        holder.binding.imgTru.setOnClickListener(v -> {
            // cap nhat giam so luong
            if(interfaceUpdateCart != null){
                interfaceUpdateCart.onTruSoLuong(position);
            }
        });
        holder.binding.imgCong.setOnClickListener(v -> {
            // cap nhat tang so luong
            if(interfaceUpdateCart != null){
                interfaceUpdateCart.onCongSoLuong(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        private ItemCartBinding binding;
        myViewHolder(ItemCartBinding itemCartBinding){
            super(itemCartBinding.getRoot());
            binding = itemCartBinding;
        }
        void setDataOnCart(Cart cart){
            Glide.with(context).load(cart.getProductImage()).error(R.drawable.image).into(binding.imgProductCart);
            binding.txtProductName.setText(cart.getProductName());
            binding.tvQuantity.setText(String.valueOf(cart.getSoluong()));
            if(cart.isChecked()){
                binding.imgChecked.setChecked(true);
            }
            binding.tvSize.setText(cart.getSize().toUpperCase());
            Locale locale = new Locale("vi", "VN");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            String price = numberFormat.format(cart.getProductPrice());
            binding.txtProductPrice.setText(price);
        }
    }
}
