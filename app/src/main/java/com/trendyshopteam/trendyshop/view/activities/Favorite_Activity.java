package com.trendyshopteam.trendyshop.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.adapter.Favorite_Adapter;
import com.trendyshopteam.trendyshop.databinding.ActivityFavoriteBinding;
import com.trendyshopteam.trendyshop.model.Favorite;
import com.trendyshopteam.trendyshop.utilities.SharePreferencesManage;
import com.trendyshopteam.trendyshop.view.activities.UserActivitys.MainUserActivity;

import java.util.ArrayList;
import java.util.List;

public class Favorite_Activity extends AppCompatActivity {
    private ActivityFavoriteBinding binding;
    private Favorite_Adapter adapter;
    private String userId;
    private SharePreferencesManage sharePreferencesManage ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharePreferencesManage = new SharePreferencesManage(this);
        userId = sharePreferencesManage.getUserId();

        binding.rcvFavorite.setLayoutManager(new GridLayoutManager(this, 2));
        FirebaseRecyclerOptions<Favorite> options = new FirebaseRecyclerOptions.Builder<Favorite>()
                .setQuery(FirebaseDatabase.getInstance().getReference()
                        .child("Favorite")
                        .orderByChild("userId")
                        .equalTo(userId), Favorite.class)
                .build();
        adapter = new Favorite_Adapter(options);
        binding.rcvFavorite.setAdapter(adapter);
        binding.btnBackBill.setOnClickListener(v -> {
            startActivity(new Intent(Favorite_Activity.this, MainUserActivity.class));
        });
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}