package com.trendyshopteam.trendyshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.model.ProductType;
import com.trendyshopteam.trendyshop.view.activities.UserActivitys.MainUserActivity;
import com.trendyshopteam.trendyshop.view.fragments.FragmentUser.MainUserFragment;

import java.util.ArrayList;

public class ProductTypeAdapter_User extends RecyclerView.Adapter<ProductTypeAdapter_User.ViewHolder> {
    public interface OnClickItemType {
        void onClickType(String typeId);
    }

    private OnClickItemType onClickItemType;
    private Context context;
    private ArrayList<ProductType> list;

    public ProductTypeAdapter_User(Context context, ArrayList<ProductType> list, OnClickItemType onClickItemType) {
        this.context = context;
        this.list = list;
        this.onClickItemType = onClickItemType;
    }

    private int selectPosition = -1;

    public void selectedItem(int position){
        selectPosition = position;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_type_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductType productType = list.get(position);
        holder.tv_typeProduct.setText(productType.getTypeName());
        if (selectPosition != -1 && position == selectPosition) {
            holder.layout.setBackgroundResource(R.drawable.style_click_type);
            holder.tv_typeProduct.setTextColor(Color.WHITE);
        } else {
            holder.layout.setBackgroundResource(R.drawable.style_product_type);
            holder.tv_typeProduct.setTextColor(Color.BLACK);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int previousSelectedPosition = selectPosition;
                selectPosition = position;
                notifyItemChanged(previousSelectedPosition);
                notifyItemChanged(selectPosition);

                Log.d("TypeID", "TypeId:" + productType.getProductTypeId());
                if (onClickItemType != null) {
                    onClickItemType.onClickType(productType.getProductTypeId());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_typeProduct;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_typeProduct = itemView.findViewById(R.id.tv_type_product);
            layout = itemView.findViewById(R.id.ly_type);
        }
    }
}
