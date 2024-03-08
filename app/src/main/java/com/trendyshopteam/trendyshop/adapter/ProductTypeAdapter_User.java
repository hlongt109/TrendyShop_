package com.trendyshopteam.trendyshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.model.ProductType;

import java.util.ArrayList;

public class ProductTypeAdapter_User extends RecyclerView.Adapter<ProductTypeAdapter_User.ViewHolder>{
    private Context context;
    private ArrayList<ProductType> list;

    public ProductTypeAdapter_User(Context context, ArrayList<ProductType> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_type_product,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductType productType = list.get(position);
        holder.tv_typeProduct.setText(productType.getTypeName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
    TextView tv_typeProduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_typeProduct = itemView.findViewById(R.id.tv_type_product);
        }
    }
}
