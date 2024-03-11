package com.trendyshopteam.trendyshop.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.model.Favorite;
import com.trendyshopteam.trendyshop.model.Product;
import com.trendyshopteam.trendyshop.view.activities.UserActivitys.productDetailFavorite_Activity;
import com.trendyshopteam.trendyshop.view.activities.productDetail_Activity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Favorite_Adapter extends FirebaseRecyclerAdapter<Favorite, Favorite_Adapter.MyViewHolder> {

    private ArrayList<Favorite> originalList;

    public Favorite_Adapter(@NonNull FirebaseRecyclerOptions<Favorite> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int i, @NonNull Favorite model) {
        String productId = model.getProductId();

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Product");

        productRef.child(productId).get()
                .addOnCompleteListener(task -> {
                    Product product = task.getResult().getValue(Product.class);
                    if(product != null){
                        holder.name_favorite.setText(product.getProductName());

//                        Locale vietnamLocale = new Locale("vi", "VN");
//                        NumberFormat vietnamFormat = NumberFormat.getCurrencyInstance(vietnamLocale);
//                        String priceFormatted = vietnamFormat.format(product.getPrice());
//                        holder.price_favorite.setText(priceFormatted);

                        Glide.with(holder.image_favorite.getContext())
                                .load(product.getImgProduct())
                                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                                .into(holder.image_favorite);

                    }
                });

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), productDetailFavorite_Activity.class);
            intent.putExtra("productId", productId);
            intent.putExtra("favoriteId", model.getFavoriteId());
            view.getContext().startActivity(intent);
        });

        holder.favorite.setOnClickListener(view -> {
            DatabaseReference favoriteRef = FirebaseDatabase.getInstance().getReference().child("Favorite");
            favoriteRef.child(model.getFavoriteId()).removeValue()
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(view.getContext(), "Loại thành công sản phẩm ra khỏi mục yêu thích !", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(view.getContext(), "Loại không thành công sản phẩm ra khỏi mục yêu thích !", Toast.LENGTH_SHORT).show();
                    });
        });

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new MyViewHolder(view);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image_favorite, favorite;
        TextView name_favorite, price_favorite;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image_favorite = itemView.findViewById(R.id.image_favorite);
            favorite = itemView.findViewById(R.id.favorite);
            name_favorite = itemView.findViewById(R.id.name_favorite);
            price_favorite = itemView.findViewById(R.id.price_favorite);

        }

    }

}
