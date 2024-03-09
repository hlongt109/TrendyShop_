package com.trendyshopteam.trendyshop.view.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.adapter.ProductType_Adapter;
import com.trendyshopteam.trendyshop.databinding.ActivityProductTypeManageBinding;
import com.trendyshopteam.trendyshop.model.Product;
import com.trendyshopteam.trendyshop.model.ProductType;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

public class ProductTypeManage_Activity extends AppCompatActivity {
    private ActivityProductTypeManageBinding binding;
    private String imageProductTypeUri;
    private ImageView add_imgProductType;
    private ArrayList<ProductType> list = new ArrayList<>();
    private ProductType_Adapter adapter;
    FirebaseDatabase database;
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
       if(result.getResultCode() == RESULT_OK){
           if(result.getData() != null){
               Uri imageUri = result.getData().getData();
               try {
                   InputStream inputStream = getContentResolver().openInputStream(imageUri);
                   Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                   add_imgProductType.setImageBitmap(bitmap);
                   imageProductTypeUri = imageUri.toString();
               }catch (FileNotFoundException e){
                   e.printStackTrace();
               }
           }
       }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductTypeManageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();

        DatabaseReference productTypeRef = database.getReference("ProductType");

        productTypeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ProductType productType = dataSnapshot.getValue(ProductType.class);
                    if(productType != null){
                        list.add(productType);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.rcvProductTypeManage.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductType_Adapter(this, list, database.getReference());
        binding.rcvProductTypeManage.setAdapter(adapter);

        binding.addProductTypeManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProductTypeDiaLog();
            }
        });
        binding.backProductTypeManage.setOnClickListener(v -> {
            startActivity(new Intent(this, MainManager.class));
            finishAffinity();
        });
    }

    private void addProductTypeDiaLog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.add_producttype, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        add_imgProductType = view.findViewById(R.id.add_imgProductType);
        TextInputEditText ed_nameProductType = view.findViewById(R.id.ed_nameProductType);
        TextInputLayout in_nameProductType = view.findViewById(R.id.in_nameProductType);
        Button btn_addProductType = view.findViewById(R.id.btn_addProductType);

        add_imgProductType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(intent);
            }
        });

        btn_addProductType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference productTypeRef = FirebaseDatabase.getInstance().getReference().child("ProductType");
                String nameProductType = ed_nameProductType.getText().toString();
                String idProductType = UUID.randomUUID().toString();

                if(imageProductTypeUri == null || imageProductTypeUri.isEmpty()){
                    Toast.makeText(ProductTypeManage_Activity.this, "Vui lòng chọn ảnh của loại sản phẩm !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(nameProductType.isEmpty()){
                    if(nameProductType.equals("")){
                        in_nameProductType.setError("Vui lòng không để trống tên loại sẩn phẩm !");
                    }else{
                        in_nameProductType.setError(null);
                    }
                    return;
                }

                Uri imageUri = Uri.parse(imageProductTypeUri);
                String imageName = nameProductType + ".jpg";
                StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("ProductTypeImage").child(imageName);
                imageRef.putFile(imageUri)
                        .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            ProductType newProductType = new ProductType(idProductType, nameProductType, imageUrl);
                            productTypeRef.child(idProductType).setValue(newProductType)
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(ProductTypeManage_Activity.this, "Thêm " + nameProductType + " thành công", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(ProductTypeManage_Activity.this, "Thêm " + nameProductType + " thất bại", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    });
                        }))
                        .addOnFailureListener(e -> {
                            Toast.makeText(ProductTypeManage_Activity.this, "Lỗi khi tải ảnh lên: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });
            }
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