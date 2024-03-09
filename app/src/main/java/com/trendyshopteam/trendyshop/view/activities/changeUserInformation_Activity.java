package com.trendyshopteam.trendyshop.view.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.trendyshopteam.trendyshop.databinding.ActivityChangeUserInformationBinding;
import com.trendyshopteam.trendyshop.model.User;
import com.trendyshopteam.trendyshop.utilities.SharePreferencesManage;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class changeUserInformation_Activity extends AppCompatActivity {
    private ActivityChangeUserInformationBinding binding;
    private String productId, userId;
    private Uri imageProductUri;

    private SharePreferencesManage sharePreferencesManage;

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if(result.getResultCode() == RESULT_OK){
                    if(result.getData() != null){
                        imageProductUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageProductUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.updateImage.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangeUserInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productId = getIntent().getStringExtra("productId");

        sharePreferencesManage = new SharePreferencesManage(this);
        userId = sharePreferencesManage.getUserId();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");
        userRef.child(userId).get()
                .addOnCompleteListener(task -> {
                    User user = task.getResult().getValue(User.class);
                    if (user != null) {
                        binding.editTextFullName.setText(user.getFullname());
                        binding.editTextEmail.setText(user.getEmail());
                        binding.editTextAddress.setText(user.getAddress());
                    }
                });

        binding.backBillManage.setOnClickListener(view -> {
            Intent intent = new Intent(changeUserInformation_Activity.this, productDetail_Activity.class);
            intent.putExtra("productId", productId);
            startActivity(intent);
        });

        binding.btnSave.setOnClickListener(view -> updateUser());

        binding.updateImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private void updateUser() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");
        userRef.child(userId).get()
                .addOnCompleteListener(task -> {
                    User user = task.getResult().getValue(User.class);
                    if (user != null) {
                        String newFullName = binding.editTextFullName.getText().toString().trim();
                        String newEmail = binding.editTextEmail.getText().toString().trim();
                        String newAddress = binding.editTextAddress.getText().toString().trim();

                        // Kiểm tra tên không được trống
                        if (TextUtils.isEmpty(newFullName)) {
                            binding.textInputLayoutFullName.setError("Tên không được để trống");
                            return;
                        }

                        // Kiểm tra địa chỉ email hợp lệ
                        if (TextUtils.isEmpty(newEmail) || !android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                            binding.textInputLayoutEmail.setError("Email không hợp lệ");
                            return;
                        }

                        // Kiểm tra địa chỉ không được trống và đúng định dạng
                        if (TextUtils.isEmpty(newAddress)) {
                            binding.textInputLayoutAddress.setError("Địa chỉ không được để trống");
                            return;
                        } else if (!isValidAddress(newAddress)) {
                            binding.textInputLayoutAddress.setError("Địa chỉ không hợp lệ");
                            return;
                        }

                        if (imageProductUri != null) {
                            uploadImageAndSaveData(user, newFullName, newEmail, newAddress);
                        } else {
                            continueUserUpdate(user, newFullName, newEmail, newAddress);
                        }
                    }
                });
    }

    private void uploadImageAndSaveData(User user, String newFullName, String newEmail, String newAddress) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("UserPhoto");
        String fileName = "user" + user.getId() + "_image.jpg";
        StorageReference imageRef = storageRef.child(fileName);
        UploadTask uploadTask = imageRef.putFile(imageProductUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                user.setPhoto(uri.toString());
                continueUserUpdate(user, newFullName, newEmail, newAddress);
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(changeUserInformation_Activity.this, "Lỗi khi tải ảnh lên Firebase Storage", Toast.LENGTH_SHORT).show();
        });
    }

    private void continueUserUpdate(User user, String newFullName, String newEmail, String newAddress) {
        if (!newFullName.equals(user.getFullname())) {
            user.setFullname(newFullName);
        }
        if (!newEmail.equals(user.getEmail())) {
            user.setEmail(newEmail);
        }
        if (!newAddress.equals(user.getAddress())) {
            user.setAddress(newAddress);
        }

        Map<String, Object> updateUserData = new HashMap<>();
        updateUserData.put("fullname", user.getFullname());
        updateUserData.put("email", user.getEmail());
        updateUserData.put("address", user.getAddress());
        updateUserData.put("photo", user.getPhoto());

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");
        userRef.child(userId).updateChildren(updateUserData)
                .addOnCompleteListener(updateTask -> {
                    if (updateTask.isSuccessful()) {
                        Intent intent = new Intent(changeUserInformation_Activity.this, productDetail_Activity.class);
                        intent.putExtra("productId", productId);
                        startActivity(intent);
                        Toast.makeText(changeUserInformation_Activity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(changeUserInformation_Activity.this, "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isValidAddress(String address) {
        String addressRegex = "^(?=.*[0-9]).{10,}$";
        return address.matches(addressRegex);
    }
}
