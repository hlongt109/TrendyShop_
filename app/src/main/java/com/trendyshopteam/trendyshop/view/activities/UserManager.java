package com.trendyshopteam.trendyshop.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.databinding.ActivityUserManagerBinding;
import com.trendyshopteam.trendyshop.interfaces.UserManageInterface;
import com.trendyshopteam.trendyshop.presenter.UserManagerPresenter;

public class UserManager extends AppCompatActivity implements UserManageInterface {
    private ActivityUserManagerBinding binding;
    private UserManagerPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new UserManagerPresenter(this);
        presenter.loadDataOnView(this, binding.rcvUser,presenter);
        binding.btnAdd.setOnClickListener(v -> {
            presenter.addNewUser();
        });
    }

    @Override
    public Context getContext() {
        return this;
    }
}