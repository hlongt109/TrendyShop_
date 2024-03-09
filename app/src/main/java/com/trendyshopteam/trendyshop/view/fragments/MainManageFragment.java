package com.trendyshopteam.trendyshop.view.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.databinding.FragmentMainManageBinding;
import com.trendyshopteam.trendyshop.view.activities.MainActivity;

public class MainManageFragment extends Fragment {

    private FragmentMainManageBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_main_manage,container,false);
       binding = FragmentMainManageBinding.bind(view);

       binding.billManage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //chuyển sang màn hình billManage
           }
       });

       binding.ProductManage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //chuyển sang màn hình Product Manage
           }
       });

       binding.userManage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //chuyển sang màn hình User manage
           }
       });

       binding.Statistical.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //chuyển sang màn hình statistical
           }
       });

       return view;
    }
}