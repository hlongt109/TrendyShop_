package com.trendyshopteam.trendyshop.view.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.databinding.ActivitySignUpBinding;
import com.trendyshopteam.trendyshop.interfaces.SignupInterface;
import com.trendyshopteam.trendyshop.presenter.SignupPresenter;

public class SignUpActivity extends AppCompatActivity implements SignupInterface {
    private ActivitySignUpBinding binding;
    private SignupPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new SignupPresenter(this);
        init();
    }
    private void init() {
        binding.btnSignUp.setOnClickListener(v -> {
            String email = String.valueOf(binding.edEmail.getText());
            String name = String.valueOf(binding.edFullName.getText());
            String pass = String.valueOf(binding.edPass.getText());
            String rePas = String.valueOf(binding.edRePass.getText());
            presenter.signUp(email,pass,rePas,name);
        });
        binding.btnGotoLogin.setOnClickListener(v -> {
            onBackPressed();
        });
    }
    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void signUpSucess() {
        Toast.makeText(this, "Sign up successfully", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    public void signUpFailed() {
        Toast.makeText(this, "Sign up failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void emailExist() {
        binding.tilEmail.setError("Email is exist");
    }

    @Override
    public void nameError() {
        binding.tilFullname.setError("Enter your name");
    }

    @Override
    public void emailError() {
        binding.tilEmail.setError("Email is invalid");
    }

    @Override
    public void passError() {
        binding.tilPass.setError("Password must be at least 6 characters with 1 uppercase letter !");
    }

    @Override
    public void passNotMatch() {
        binding.tilPass.setError("Not match !");
        binding.tilRePass.setError("Not match !");
    }

    @Override
    public void setLoading() {
        binding.btnSignUp.setVisibility(View.INVISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopLoading() {
        binding.btnSignUp.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void clearErr() {
        binding.tilEmail.setError(null);
        binding.tilFullname.setError(null);
        binding.tilPass.setError(null);
        binding.tilRePass.setError(null);
    }
}