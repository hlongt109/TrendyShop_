package com.trendyshopteam.trendyshop.view.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.adapter.Bill_Adapter;
import com.trendyshopteam.trendyshop.databinding.ActivityBillManageBinding;
import com.trendyshopteam.trendyshop.model.Bill;
import com.trendyshopteam.trendyshop.model.BillDetail;

import java.util.ArrayList;

public class BillManage_Activity extends AppCompatActivity {
    private ActivityBillManageBinding binding;
    private Bill_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBillManageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rcvBillManage.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Bill> options = new FirebaseRecyclerOptions.Builder<Bill>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Bill"), Bill.class)
                .build();
        adapter = new Bill_Adapter(options);
        binding.rcvBillManage.setAdapter(adapter);
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