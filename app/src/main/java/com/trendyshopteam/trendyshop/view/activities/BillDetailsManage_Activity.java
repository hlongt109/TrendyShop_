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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.adapter.BillDetail_Adapter;
import com.trendyshopteam.trendyshop.databinding.ActivityBillDetailsManageBinding;
import com.trendyshopteam.trendyshop.model.BillDetail;

public class BillDetailsManage_Activity extends AppCompatActivity {
    private ActivityBillDetailsManageBinding binding;
    private String billId;
    private BillDetail_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBillDetailsManageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        billId = getIntent().getStringExtra("billId");
        binding.rcvBillDetailManage.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<BillDetail> options = new FirebaseRecyclerOptions.Builder<BillDetail>()
                .setQuery(FirebaseDatabase.getInstance().getReference()
                        .child("BillDetail")
                        .orderByChild("billId")
                        .equalTo(billId), BillDetail.class)
                .build();
        adapter = new BillDetail_Adapter(options);
        binding.rcvBillDetailManage.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        binding.backBillDetailManage.setOnClickListener(view -> {
            startActivity(new Intent(BillDetailsManage_Activity.this, BillManage_Activity.class));
        });

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