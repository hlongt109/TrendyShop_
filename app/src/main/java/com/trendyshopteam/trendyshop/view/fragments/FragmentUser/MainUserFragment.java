package com.trendyshopteam.trendyshop.view.fragments.FragmentUser;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.adapter.ProductAdapter_User;
import com.trendyshopteam.trendyshop.adapter.ProductTypeAdapter_User;
import com.trendyshopteam.trendyshop.databinding.FragmentUserMainBinding;
import com.trendyshopteam.trendyshop.model.Product;
import com.trendyshopteam.trendyshop.model.ProductType;

import java.util.ArrayList;


public class MainUserFragment extends Fragment implements ProductTypeAdapter_User.OnClickItemType {
    private FragmentUserMainBinding binding;
    private ProductTypeAdapter_User adapterUser;
    private ProductAdapter_User adapterProduct;
    private FirebaseDatabase database, databaseProduct;
    private ArrayList<ProductType> list = new ArrayList<>();
    private String productTypeId;
    private ArrayList<Product> listProduct = new ArrayList<>();
    private int selectedPisition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_main, container, false);
        binding = FragmentUserMainBinding.bind(view);


        database = FirebaseDatabase.getInstance();
        databaseProduct = FirebaseDatabase.getInstance();

        getAllProduct();
        binding.tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPisition = -1;
                adapterUser.selectedItem(selectedPisition);
                getAllProduct();
            }
        });

        DatabaseReference productType = database.getReference("ProductType");
        productType.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ProductType productType1 = dataSnapshot.getValue(ProductType.class);
                    if (productType1 != null) {
                        list.add(productType1);
                    }
                }
                adapterUser.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rcvTypeProduct.setLayoutManager(linearLayoutManager);
        adapterUser = new ProductTypeAdapter_User(getContext(), list, this);
        binding.rcvTypeProduct.setAdapter(adapterUser);


        //


        DatabaseReference product = databaseProduct.getReference("Product");

        Query query = product.orderByChild("productTypeId").equalTo(productTypeId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProduct.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product1 = dataSnapshot.getValue(Product.class);
                    if (product1 != null) {
                        listProduct.add(product1);
                    }
                }
                adapterProduct.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.rcvProduct.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapterProduct = new ProductAdapter_User(getContext(), listProduct);
        binding.rcvProduct.setAdapter(adapterProduct);
        return view;
    }


    @Override
    public void onClickType(String typeId) {
        productTypeId = typeId;
        Log.d("TypeID", "id: " + typeId);
        refreshProductList();
    }

    private void refreshProductList() {
        // Query products based on the selected product type
        DatabaseReference product = databaseProduct.getReference("Product");
        Query query = product.orderByChild("productTypeId").equalTo(productTypeId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProduct.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product1 = dataSnapshot.getValue(Product.class);
                    if (product1 != null) {
                        listProduct.add(product1);
                    }
                }
                adapterProduct.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void getAllProduct(){
        DatabaseReference allProduct = databaseProduct.getReference("Product");
        allProduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProduct.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product != null) {
                        listProduct.add(product);
                    }
                }
                adapterProduct.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}