package com.trendyshopteam.trendyshop.view.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.adapter.ProductType_Adapter;
import com.trendyshopteam.trendyshop.adapter.Product_Adapter;
import com.trendyshopteam.trendyshop.databinding.ActivityProductManageBinding;
import com.trendyshopteam.trendyshop.model.Product;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

public class ProductManage_Activity extends AppCompatActivity {
    private ActivityProductManageBinding binding;
    private String imageProductUri;
    private ImageView add_imgProduct;
    private ArrayList<Product> list = new ArrayList<>();
    private Product_Adapter adapter;
    FirebaseDatabase database;
    String productTypeId;
    private static final int IMAGE_PICK_REQUEST = 1;
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if(result.getResultCode() == RESULT_OK){
                    if(result.getData() != null){
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            add_imgProduct.setImageBitmap(bitmap);
                            imageProductUri = imageUri.toString();
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductManageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();

        productTypeId = getIntent().getStringExtra("Product_Type_Id");

        DatabaseReference productRef = database.getReference("Product");

        Query query = productRef.orderByChild("productTypeId").equalTo(productTypeId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Product product = dataSnapshot.getValue(Product.class);
                    if(product != null){
                        list.add(product);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.rcv2ProductManage.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Product_Adapter(this, list, database.getReference());
        binding.rcv2ProductManage.setAdapter(adapter);

        binding.addProductManage.setOnClickListener(view -> addProductDiaLog());
        binding.backProductManage.setOnClickListener(v -> {
            startActivity(new Intent(this,ProductTypeManage_Activity.class));
            finish();
        });
    }

    private void addProductDiaLog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.add_product, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        add_imgProduct = view.findViewById(R.id.add_imgProduct);
        TextInputLayout in_nameProduct = view.findViewById(R.id.in_nameProduct);
        TextInputLayout in_priceProduct = view.findViewById(R.id.in_priceProduct);
        TextInputLayout in_descriptionProduct = view.findViewById(R.id.in_descriptionProduct);
        TextInputEditText ed_nameProduct = view.findViewById(R.id.ed_nameProduct);
        TextInputEditText ed_priceProduct = view.findViewById(R.id.ed_priceProduct);
        TextInputEditText ed_descriptionProduct = view.findViewById(R.id.ed_descriptionProduct);
        Button btn_addProduct = view.findViewById(R.id.btn_addProduct);

        add_imgProduct.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });

        btn_addProduct.setOnClickListener(view12 -> {
            DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Product");
            String nameProduct = ed_nameProduct.getText().toString();
            String priceProduct = ed_priceProduct.getText().toString();
            String descriptionProduct = ed_descriptionProduct.getText().toString();
            String productId = UUID.randomUUID().toString();

            if (imageProductUri == null || imageProductUri.isEmpty()) {
                Toast.makeText(ProductManage_Activity.this, "Vui lòng chọn ảnh sản phẩm!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (nameProduct.isEmpty() || priceProduct.isEmpty() || descriptionProduct.isEmpty()) {
                if (nameProduct.equals("")) {
                    in_nameProduct.setError("Vui lòng không bỏ trống tên sản phẩm");
                } else {
                    in_nameProduct.setError(null);
                }

                if (priceProduct.equals("")) {
                    in_priceProduct.setError("Vui lòng không bỏ trống giá sản phẩm");
                } else {
                    in_priceProduct.setError(null);
                }

                if (descriptionProduct.equals("")) {
                    in_descriptionProduct.setError("Vui lòng mô tả sản phẩm");
                } else {
                    in_descriptionProduct.setError(null);
                }
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceProduct);
                if (price <= 0) {
                    in_priceProduct.setError("giá sản phẩm phải lớn hơn 0");
                    return;
                }
                in_priceProduct.setError(null);
            } catch (NumberFormatException e) {
                in_priceProduct.setError("giá sản phẩm phải là số");
                return;
            }

            Uri imageUri = Uri.parse(imageProductUri);
            String imageName = nameProduct + ".jpg";
            StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("ProductImages").child(imageName);
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        Product newProduct = new Product(productId, productTypeId, nameProduct, descriptionProduct, price, 0.0, imageUrl);
                        productRef.child(productId).setValue(newProduct)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(ProductManage_Activity.this, "Thêm " + nameProduct + " thành công", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(ProductManage_Activity.this, "Thêm " + nameProduct + " thất bại", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                });
                    }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(ProductManage_Activity.this, "Lỗi khi tải ảnh lên: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
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
