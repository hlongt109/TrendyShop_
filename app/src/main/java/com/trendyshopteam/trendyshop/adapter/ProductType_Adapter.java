package com.trendyshopteam.trendyshop.adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.trendyshopteam.trendyshop.model.ProductType;
import com.trendyshopteam.trendyshop.view.activities.ProductManage_Activity;

import java.util.ArrayList;

public class ProductType_Adapter extends RecyclerView.Adapter<ProductType_Adapter.ProductType_ViewHolder>{
    private Context context;
    private ArrayList<ProductType> list;
    private DatabaseReference database;

    public ProductType_Adapter(Context context, ArrayList<ProductType> list, DatabaseReference database) {
        this.context = context;
        this.list = list;
        this.database = database;
    }

    @NonNull
    @Override
    public ProductType_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_producttype, parent, false);
        return new ProductType_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductType_ViewHolder holder, int position) {
        ProductType Pdt = list.get(position);
        holder.tv_name_productType.setText(list.get(position).getTypeName());
        Glide.with(context)
                .load(Pdt.getImgProductType())
                .placeholder(R.drawable.logo_trendyshop)
                .into(holder.img_productType);

        holder.item_menuProductType.setOnClickListener(view -> {
            PopupMenu menu = new PopupMenu(context, holder.item_menuProductType);
            menu.getMenuInflater().inflate(R.menu.popup_menu, menu.getMenu());
            menu.setOnMenuItemClickListener(menuItem -> {
                if(menuItem.getItemId() == R.id.menu_delete){
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("Bạn có chắc chắn muốn xóa loại sản phẩm này chứ ?")
                            .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String productTypeId = Pdt.getProductTypeId();
                                    DatabaseReference productTypeRef = FirebaseDatabase.getInstance().getReference("ProductType");
                                    productTypeRef.child(productTypeId).removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    int position = list.indexOf(Pdt);
                                                    if(position != -1){
                                                        list.remove(position);
                                                        notifyItemRemoved(position);
                                                        notifyItemRangeChanged(position, list.size());
                                                        Toast.makeText(context, "Xóa thành công loại sản phẩm", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Xóa không thành công loại sản phẩm", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }).setNegativeButton("Hủy bỏ", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                }else if(menuItem.getItemId() == R.id.menu_update){
                    updateProductType(Pdt);
                    return true;
                }else{
                    return false;
                }
            });
            menu.show();
        });

        holder.itemView.setOnClickListener(view -> {
            String productTypeId = Pdt.getProductTypeId();
            Intent intent = new Intent(view.getContext(), ProductManage_Activity.class);
            intent.putExtra("Product_Type_Id", productTypeId);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ProductType_ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name_productType;
        ImageView img_productType, item_menuProductType;
        public ProductType_ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name_productType = itemView.findViewById(R.id.tv_name_productType);
            img_productType = itemView.findViewById(R.id.img_productType);
            item_menuProductType = itemView.findViewById(R.id.item_menuProductType);
        }
    }

    @SuppressLint("MissingInflatedId")
    private void updateProductType(ProductType pdt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.update_producttype, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        ImageView update_imgProductType = view.findViewById(R.id.update_imgProductType);
        TextInputLayout in_nameProductTypeUpdate = view.findViewById(R.id.in_nameProductTypeupdate);
        TextInputEditText ed_nameProductTypeUpdate = view.findViewById(R.id.ed_nameProductTypeupdate);
        Button btn_updateProductType = view.findViewById(R.id.btn_updateProductType);

        ed_nameProductTypeUpdate.setText(pdt.getTypeName());
        Glide.with(context)
                .load(pdt.getImgProductType())
                .into(update_imgProductType);

        btn_updateProductType.setOnClickListener(view1 -> {
            String newName = ed_nameProductTypeUpdate.getText().toString();
            if(newName.equals("")){
                in_nameProductTypeUpdate.setError("Vui lòng không để trống tên loại sản phẩm !");
                return;
            }else{
                in_nameProductTypeUpdate.setError(null);
            }
            DatabaseReference productTypeRef = FirebaseDatabase.getInstance().getReference("ProductType").child(pdt.getProductTypeId());
            productTypeRef.child("typeName").setValue(newName)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            notifyDataSetChanged();
                            Toast.makeText(context, "Cập nhật loại sản phẩm thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Cập nhật loại sản phẩm không thành công", Toast.LENGTH_SHORT).show();
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
