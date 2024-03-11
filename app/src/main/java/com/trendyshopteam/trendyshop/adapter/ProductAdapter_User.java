package com.trendyshopteam.trendyshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.model.Favorite;
import com.trendyshopteam.trendyshop.model.Product;
import com.trendyshopteam.trendyshop.model.ProductType;
import com.trendyshopteam.trendyshop.utilities.SharePreferencesManage;
import com.trendyshopteam.trendyshop.view.activities.UserActivitys.DetailsActivity;
import com.trendyshopteam.trendyshop.view.activities.productDetail_Activity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class ProductAdapter_User extends RecyclerView.Adapter<ProductAdapter_User.ViewHolep>{
    private Context context;
    private ArrayList<Product> list;
    private SharePreferencesManage sharePreferencesManage;
    private DatabaseReference favoriteRef = FirebaseDatabase.getInstance().getReference().child("Favorite");
    private String userId;
    private Handler.Callback callback;

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
        sharePreferencesManage = new SharePreferencesManage(context);
        userId = sharePreferencesManage.getUserId();
        Glide.with(context).load(product.getImgProduct()).into(holder.imgProduct);
        holder.tvEvaluate.setText(String.valueOf(product.getRate()));
        holder.tvNameProduct.setText(product.getProductName());

        String productTypeId = product.getProductTypeId();
        DatabaseReference productTypeRef = FirebaseDatabase.getInstance().getReference().child("ProductType");
        productTypeRef.child(productTypeId).get()
                .addOnCompleteListener(task -> {
                    ProductType productType = task.getResult().getValue(ProductType.class);
                    if(productType != null){
                        holder.tv_name_productType.setText(productType.getTypeName());
                    }
                });

//        Locale vietnamLocale = new Locale("vi", "VN");
//        NumberFormat vietnamFormat = NumberFormat.getCurrencyInstance(vietnamLocale);
//        String priceFormatted = vietnamFormat.format(product.getPrice());
//        holder.tvPrice.setText(priceFormatted);

        favoriteRef.orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Favorite favorite = dataSnapshot.getValue(Favorite.class);
                        if(favorite != null && favorite.getProductId().equals(product.getProductId())){
                            holder.updateFavoriteStatus(true);
                            holder.imgFavorite.setOnClickListener(view -> {
                                Toast.makeText(context, "sản phẩm đã có trong danh mục yêu thích", Toast.LENGTH_SHORT).show();
                            });
                            return;
                        }
                    }
                }
                holder.updateFavoriteStatus(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.imgFavorite.setOnClickListener(view -> {
            String productId = product.getProductId();
            String favoriteId = UUID.randomUUID().toString();
            Favorite favorite = new Favorite(favoriteId, userId, productId);
            favoriteRef.child(favoriteId).setValue(favorite);
            Toast.makeText(context, "Thêm sản phẩm vào mục yêu thích thành công", Toast.LENGTH_SHORT).show();
        });

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productId = product.getProductId();
                Intent intent = new Intent(view.getContext(), productDetail_Activity.class);
                intent.putExtra("productId", productId);
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
        TextView tvEvaluate, tvNameProduct, tvPrice, tv_name_productType;
        CardView addToCart;
        public ViewHolep(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_productItem);
            imgFavorite = itemView.findViewById(R.id.img_favorite);
            tvEvaluate = itemView.findViewById(R.id.tv_evaluate);
            tvNameProduct = itemView.findViewById(R.id.tv_nameProduct);
            tvPrice = itemView.findViewById(R.id.tv_priceProduct);
            tv_name_productType = itemView.findViewById(R.id.tv_name_productType);
            addToCart = itemView.findViewById(R.id.addToCart);
        }

        private void updateFavoriteStatus(boolean isFavorite){
            if(isFavorite){
                imgFavorite.setColorFilter(ContextCompat.getColor(context, R.color.red));
            } else {
                imgFavorite.setColorFilter(null);
            }
        }
    }


}
