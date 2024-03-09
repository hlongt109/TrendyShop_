package com.trendyshopteam.trendyshop.view.fragments.FragmentUser;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.adapter.OrderHistory_User_Adapter;
import com.trendyshopteam.trendyshop.databinding.FragmentOrderHistoryBinding;
import com.trendyshopteam.trendyshop.model.Bill;
import com.trendyshopteam.trendyshop.utilities.SharePreferencesManage;

import java.util.ArrayList;


public class OrderHistoryFragment extends Fragment {

    private FragmentOrderHistoryBinding binding;
    private ArrayList<Bill> list = new ArrayList<>();
    private FirebaseDatabase database;
    private OrderHistory_User_Adapter adapter;
    private String userId;
    private SharePreferencesManage sharePreferencesManage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        binding = FragmentOrderHistoryBinding.bind(view);

        sharePreferencesManage = new SharePreferencesManage(getContext());
        userId = sharePreferencesManage.getUserId();
        database = FirebaseDatabase.getInstance();
        DatabaseReference data = database.getReference("Bill");
        data.orderByChild("userId").equalTo("u6FdmgC2KzOgD3FLoLRnyjyNent2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Bill bill = dataSnapshot.getValue(Bill.class);
                    if (bill != null) {
                        list.add(bill);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.rcvOrderDetails.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderHistory_User_Adapter(list, getContext(), database);
        binding.rcvOrderDetails.setAdapter(adapter);
        return view;
    }
}