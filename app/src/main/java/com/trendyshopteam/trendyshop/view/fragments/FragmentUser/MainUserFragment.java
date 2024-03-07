package com.trendyshopteam.trendyshop.view.fragments.FragmentUser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.databinding.FragmentUserMainBinding;


public class MainUserFragment extends Fragment {
    private FragmentUserMainBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_main, container, false);
        binding = FragmentUserMainBinding.bind(view);
        return view;
    }
}