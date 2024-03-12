package com.trendyshopteam.trendyshop.view.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.databinding.FragmentMainManageBinding;
import com.trendyshopteam.trendyshop.interfaces.FragmentInterface.MainManageFrgInterface;
import com.trendyshopteam.trendyshop.presenter.FragmentPresenter.MainManageFrgPresenter;
import com.trendyshopteam.trendyshop.view.activities.BillManage_Activity;
import com.trendyshopteam.trendyshop.view.activities.ProductTypeManage_Activity;
import com.trendyshopteam.trendyshop.view.activities.UserManager;

public class MainManageFragment extends Fragment implements MainManageFrgInterface {

    private FragmentMainManageBinding binding;
    private MainManageFrgPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_manage, container, false);
        binding = FragmentMainManageBinding.bind(view);
        presenter = new MainManageFrgPresenter(this);
        presenter.setDataOnRcv(binding.rcvOrder, presenter);
        init();
        return view;
    }

    private void init() {
        binding.btnBillManage.setOnClickListener(view -> startActivity(new Intent(getContext(), BillManage_Activity.class)));

        binding.btnProductManage.setOnClickListener(view -> startActivity(new Intent(getContext(), ProductTypeManage_Activity.class)));

        binding.btnUserManage.setOnClickListener(view -> startActivity(new Intent(getContext(), UserManager.class)));

        binding.btnStatisticalManage.setOnClickListener(view -> {

        });
    }

    @Override
    public void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        binding.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showImageEmpty() {
        binding.imageEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideImageEmpty() {
        binding.imageEmpty.setVisibility(View.INVISIBLE);
    }
}