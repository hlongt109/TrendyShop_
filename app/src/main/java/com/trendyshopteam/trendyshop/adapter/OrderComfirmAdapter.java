package com.trendyshopteam.trendyshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trendyshopteam.trendyshop.databinding.ItemConfirmOderBinding;
import com.trendyshopteam.trendyshop.interfaces.FragmentInterface.OrderClickInterface;
import com.trendyshopteam.trendyshop.model.Order;
import com.trendyshopteam.trendyshop.presenter.FragmentPresenter.MainManageFrgPresenter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderComfirmAdapter extends RecyclerView.Adapter<OrderComfirmAdapter.myViewHolder>{
    private final Context context;
    private final ArrayList<Order> list;
    private MainManageFrgPresenter presenter;
    OrderClickInterface orderClickInterface;
    public void clickToComfirmOrder(OrderClickInterface orderClickInterface){
        this.orderClickInterface = orderClickInterface;
    }
    public OrderComfirmAdapter(Context context, ArrayList<Order> list, MainManageFrgPresenter presenter) {
        this.context = context;
        this.list = list;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemConfirmOderBinding binding = ItemConfirmOderBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new myViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
         Order order = list.get(position);
         holder.setData(order);
         holder.itemView.setOnClickListener( v -> {
              orderClickInterface.onClickOrder(position);
         });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        private ItemConfirmOderBinding binding;
        myViewHolder(ItemConfirmOderBinding itemConfirmOderBinding){
            super(itemConfirmOderBinding.getRoot());
            binding = itemConfirmOderBinding;
        }
        void setData(Order order){
            binding.tvIdOder.setText(order.getOrderId());
            if(order.getStatus() == 0){
                binding.tvTrangThai.setText("Pending confirmation");
            }
            Locale locale = new Locale("vi", "VN");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            String price = numberFormat.format(order.getTotalAmount());
            binding.tvTotalPrice.setText(price);
            binding.tvDateTime.setText(order.getTimestamp());
        }
    }
}
