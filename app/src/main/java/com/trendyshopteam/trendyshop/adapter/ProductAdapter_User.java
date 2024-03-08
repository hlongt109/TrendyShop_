package com.trendyshopteam.trendyshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.model.Product;
import com.trendyshopteam.trendyshop.view.activities.UserActivitys.DetailsActivity;

import java.util.ArrayList;

public class ProductAdapter_User extends RecyclerView.Adapter<ProductAdapter_User.ViewHolep>{
    private Context context;
    private ArrayList<Product> list;

    public ProductAdapter_User(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolep onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_user,parent,false);
        return new ViewHolep(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolep holder, int position) {
        Product product = list.get(position);
        Glide.with(context).load(product.getImgProduct()).into(holder.imgProduct);
        holder.tvEvaluate.setText(String.valueOf(product.getRate()));
        holder.tvNameProduct.setText(product.getProductName());
        holder.tvPrice.setText(String.valueOf(product.getPrice()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productId = product.getProductId();
                Log.d("PRODUCTID", "id " + productId);
                Intent intent = new Intent(view.getContext(), DetailsActivity.class);
                intent.putExtra("ProductId", productId);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolep extends RecyclerView.ViewHolder{
        ImageView imgProduct, imgFavorite;
        TextView tvEvaluate, tvNameProduct, tvPrice;
        public ViewHolep(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_productItem);
            imgFavorite = itemView.findViewById(R.id.img_favorite);
            tvEvaluate = itemView.findViewById(R.id.tv_evaluate);
            tvNameProduct = itemView.findViewById(R.id.tv_nameProduct);
            tvPrice = itemView.findViewById(R.id.tv_priceProduct);

        }
    }
}
