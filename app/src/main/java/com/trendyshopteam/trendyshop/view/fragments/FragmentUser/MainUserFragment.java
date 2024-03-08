package com.trendyshopteam.trendyshop.view.fragments.FragmentUser;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.adapter.ProductTypeAdapter_User;
import com.trendyshopteam.trendyshop.databinding.FragmentUserMainBinding;
import com.trendyshopteam.trendyshop.model.ProductType;

import java.util.ArrayList;


public class MainUserFragment extends Fragment {
    private FragmentUserMainBinding binding;
    ProductTypeAdapter_User adapterUser;
    FirebaseDatabase database;
    ArrayList<ProductType> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_main, container, false);
        binding = FragmentUserMainBinding.bind(view);


        database = FirebaseDatabase.getInstance();
//        DatabaseReference productTypeRef = database.getReference("ProductType");
//
//        productTypeRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                list.clear();
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    ProductType productType = dataSnapshot.getValue(ProductType.class);
//                    if(productType != null){
//                        list.add(productType);
//                    }
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        DatabaseReference productType = database.getReference("ProductType");
        productType.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    ProductType productType1 = dataSnapshot.getValue(ProductType.class);
                    if(productType1 != null){
                        list.add(productType1);
                    }
                }
                adapterUser.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.rcvTypeProduct.setLayoutManager(linearLayoutManager);
        adapterUser = new ProductTypeAdapter_User(getContext(), list);
        binding.rcvTypeProduct.setAdapter(adapterUser);

        return view;
    }
}