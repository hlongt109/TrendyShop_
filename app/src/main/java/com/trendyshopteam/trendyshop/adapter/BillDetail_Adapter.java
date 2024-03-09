package com.trendyshopteam.trendyshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.model.BillDetail;
import com.trendyshopteam.trendyshop.model.Product;

import java.text.NumberFormat;
import java.util.Locale;

public class BillDetail_Adapter extends FirebaseRecyclerAdapter<BillDetail, BillDetail_Adapter.MyViewHolder> {

    private double priceProduct;
    private double total;
    public BillDetail_Adapter(@NonNull FirebaseRecyclerOptions<BillDetail> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int i, @NonNull BillDetail model) {
        String productId = model.getProductId();
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Product");
        productRef.child(productId).get()
                .addOnCompleteListener(task -> {
                    Product product = task.getResult().getValue(Product.class);
                    if(product != null){
                        Glide.with(holder.image_BillDetail.getContext())
                                .load(product.getImgProduct())
                                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                                .into(holder.image_BillDetail);

                        Locale vietnamLocale = new Locale("vi", "VN");
                        NumberFormat vietnamFormat = NumberFormat.getCurrencyInstance(vietnamLocale);
                        String priceFormatted = vietnamFormat.format(product.getPrice());
                        holder.Price_BillDetail.setText(priceFormatted);

                        holder.Nameproduct_BillDetail.setText(product.getProductName());
                    }
                });

        holder.Quantity_BillDetail.setText(String.valueOf(model.getQuantity()));

        Locale vietnamLocale = new Locale("vi", "VN");
        NumberFormat vietnamFormat = NumberFormat.getCurrencyInstance(vietnamLocale);
        String priceFormatted = vietnamFormat.format(model.getTotal());
        holder.TotalDetail_BillDetail.setText(priceFormatted);
        holder.Billdetailid_BillDetail.setText(model.getBillDetailId());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_billdetail,parent,false);
        return new MyViewHolder(view);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image_BillDetail;
        TextView Billdetailid_BillDetail,Nameproduct_BillDetail,Quantity_BillDetail,Price_BillDetail,TotalDetail_BillDetail;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image_BillDetail = itemView.findViewById(R.id.image_BillDetail);
            Billdetailid_BillDetail = itemView.findViewById(R.id.Billdetailid_BillDetail);
            Nameproduct_BillDetail = itemView.findViewById(R.id.Nameproduct_BillDetail);
            Quantity_BillDetail = itemView.findViewById(R.id.Quantity_BillDetail);
            Price_BillDetail = itemView.findViewById(R.id.Price_BillDetail);
            TotalDetail_BillDetail = itemView.findViewById(R.id.TotalDetail_BillDetail);
        }
    }

}
