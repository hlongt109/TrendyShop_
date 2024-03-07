package com.trendyshopteam.trendyshop.view.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.databinding.ActivityCreateUserBinding;
import com.trendyshopteam.trendyshop.interfaces.CreateUserInterface;
import com.trendyshopteam.trendyshop.interfaces.UserManageInterface;
import com.trendyshopteam.trendyshop.presenter.CreateUserPresenter;

public class CreateUserActivity extends AppCompatActivity implements CreateUserInterface {
    private ActivityCreateUserBinding binding;
    private CreateUserPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new CreateUserPresenter(this, binding);
        presenter.listener();

    }

    @Override
    public Activity getActivity() {
        return this;
    }


    @Override
    public void isEmailExist() {
        binding.tilEmail.setError("Email already exist");
    }
    @Override
    public void isPassEmpty() {
        binding.tilPw.setError("Vui lòng nhập password");
    }

    @Override
    public void isNameEmpty() {
        binding.tilFullname.setError("Vui lòng nhập tên");
    }

    @Override
    public void isEmailEmpty() {
        binding.tilEmail.setError("Vui lòng nhập email");
    }

    @Override
    public void clearPasswordErr() {
         binding.tilPw.setError(null);
    }
    @Override
    public void clearEmailErr() {
       binding.tilEmail.setError(null);
    }
    @Override
    public void clearNameErr() {
        binding.tilFullname.setError(null);
    }
    @Override
    public void isRdoNull() {
        Toast.makeText(this, "Chưa chọn vai trò người dùng", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isImgNull() {
        Toast.makeText(this, "Chưa chọn ảnh", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addSuccess() {
        Toast.makeText(this, "Thành công", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addFailure() {
        Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void uploadImageFailure() {
        Toast.makeText(this, "Error upload image", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.handleImagePickerResult(requestCode, resultCode, data);
    }
}