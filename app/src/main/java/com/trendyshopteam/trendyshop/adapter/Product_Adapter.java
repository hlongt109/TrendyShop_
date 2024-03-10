package com.trendyshopteam.trendyshop.adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.model.Product;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Product_Adapter extends RecyclerView.Adapter<Product_Adapter.Product_ViewHolder> {

    private Context context;
    private ArrayList<Product> list;
    private DatabaseReference database;
    private Uri newImageUri;
    private static final int IMAGE_PICK_REQUEST = 1;


    public Product_Adapter(Context context, ArrayList<Product> list, DatabaseReference database) {
        this.context = context;
        this.list = list;
        this.database = database;
    }

    @NonNull
    @Override
    public Product_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new Product_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Product_ViewHolder holder, int position) {
        Product pd = list.get(position);
        holder.tv_name_product.setText(list.get(position).getProductName());

        Locale vietnamLocale = new Locale("vi", "VN");
        NumberFormat vietnamFormat = NumberFormat.getCurrencyInstance(vietnamLocale);
        String priceFormatted = vietnamFormat.format(list.get(position).getPrice());
        holder.tv_price_product.setText(priceFormatted);

        Glide.with(context)
                .load(pd.getImgProduct())
                .placeholder(R.drawable.logo_trendyshop)
                .into(holder.img_product);

        holder.item_menuProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(context, holder.item_menuProduct);
                menu.getMenuInflater().inflate(R.menu.popup_menu, menu.getMenu());
                menu.setOnMenuItemClickListener(menuItem -> {
                    if (menuItem.getItemId() == R.id.menu_delete) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này chứ ? ")
                                .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        int position = holder.getAdapterPosition();
                                        String productId = pd.getProductId();
                                        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Product");
                                        productRef.child(productId).removeValue()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        int position = list.indexOf(pd);
                                                        if (position != -1) {
                                                            list.remove(position);
                                                            notifyItemRemoved(position);
                                                            notifyItemRangeChanged(position, list.size());
                                                        }
                                                        Toast.makeText(context, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(context, "Xóa sản phẩm không thành công", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    }
                                }).setNegativeButton("Hủy bỏ", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        return true;
                    } else if (menuItem.getItemId() == R.id.menu_update) {
                        updateProduct(pd);
                        return true;
                    } else {
                        return false;
                    }
                });
                menu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Product_ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_product, item_menuProduct;
        TextView tv_name_product, tv_price_product;

        public Product_ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product);
            item_menuProduct = itemView.findViewById(R.id.item_menuProduct);
            tv_name_product = itemView.findViewById(R.id.tv_name_product);
            tv_price_product = itemView.findViewById(R.id.tv_price_product);
        }
    }

    private void updateProduct(Product product){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.update_product, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        ImageView update_imgProduct = view.findViewById(R.id.update_imgProduct);
        TextInputLayout in_nameProductUpdate = view.findViewById(R.id.in_nameProductUpdate);
        TextInputLayout in_priceProductUpdate = view.findViewById(R.id.in_priceProductUpdate);
        TextInputLayout in_descriptionProductUpdate = view.findViewById(R.id.in_descriptionProductUpdate);
        TextInputEditText ed_nameProductUpdate = view.findViewById(R.id.ed_nameProductUpdate);
        TextInputEditText ed_priceProductUpdate = view.findViewById(R.id.ed_priceProductUpdate);
        TextInputEditText ed_descriptionProductUpdate = view.findViewById(R.id.ed_descriptionProductUpdate);
        Button btn_UpdateProduct = view.findViewById(R.id.btn_UpdateProduct);

        ed_nameProductUpdate.setText(product.getProductName());
        ed_priceProductUpdate.setText(String.valueOf(product.getPrice()));
        ed_descriptionProductUpdate.setText(product.getDescription());
        Glide.with(context).load(product.getImgProduct()).into(update_imgProduct);


        btn_UpdateProduct.setOnClickListener(view1 -> {
            String newName = ed_nameProductUpdate.getText().toString();
            String newPriceStr = ed_priceProductUpdate.getText().toString();
            String newDescription = ed_descriptionProductUpdate.getText().toString();

            if (newName.isEmpty() || newPriceStr.isEmpty() || newDescription.isEmpty()) {
                if (newName.equals("")) {
                    in_nameProductUpdate.setError("Vui lòng không bỏ trống tên sản phẩm");
                } else {
                    in_nameProductUpdate.setError(null);
                }

                if (newPriceStr.equals("")) {
                    in_priceProductUpdate.setError("Vui lòng không bỏ trống giá sản phẩm");
                } else {
                    in_priceProductUpdate.setError(null);
                }

                if (newDescription.equals("")) {
                    in_descriptionProductUpdate.setError("Vui lòng mô tả sản phẩm");
                } else {
                    in_descriptionProductUpdate.setError(null);
                }
                return;
            }

            double newPrice;
            try {
                newPrice = Double.parseDouble(newPriceStr);
                if (newPrice <= 0) {
                    in_priceProductUpdate.setError("giá sản phẩm phải lớn hơn 0");
                    return;
                }
                in_priceProductUpdate.setError(null);
            } catch (NumberFormatException e) {
                in_priceProductUpdate.setError("giá sản phẩm phải là số");
                return;
            }

            DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Product").child(product.getProductId());
            productRef.child("productName").setValue(newName);
            productRef.child("price").setValue(newPrice);
            productRef.child("description").setValue(newDescription)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            notifyDataSetChanged();
                            Toast.makeText(context, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Cập nhật sản phẩm không thành công", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        animateDialog(view);
    }

    private void animateDialog(View view) {
        // Thiết lập animation cho cửa sổ cập nhật
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f);
        scaleX.setDuration(500);
        scaleX.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f);
        scaleY.setDuration(500);
        scaleY.setInterpolator(new AccelerateDecelerateInterpolator());

        // Bắt đầu animation
        scaleX.start();
        scaleY.start();
    }

}
