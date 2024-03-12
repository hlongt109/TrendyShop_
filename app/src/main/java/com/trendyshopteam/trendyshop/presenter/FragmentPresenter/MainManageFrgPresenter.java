package com.trendyshopteam.trendyshop.presenter.FragmentPresenter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trendyshopteam.trendyshop.adapter.OrderComfirmAdapter;
import com.trendyshopteam.trendyshop.interfaces.FragmentInterface.MainManageFrgInterface;
import com.trendyshopteam.trendyshop.interfaces.FragmentInterface.OrderClickInterface;
import com.trendyshopteam.trendyshop.model.Order;

import java.util.ArrayList;

public class MainManageFrgPresenter {
    private MainManageFrgInterface frgInterface;
    private DatabaseReference databaseReference;

    private ArrayList<Order> list = new ArrayList<>();
    private OrderComfirmAdapter adapter;
    public MainManageFrgPresenter(MainManageFrgInterface frgInterface) {
        this.frgInterface = frgInterface;
    }

    public void setDataOnRcv(RecyclerView rcv, MainManageFrgPresenter presenter) {
        frgInterface.showLoading();
        databaseReference = FirebaseDatabase.getInstance().getReference("Order");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              list.clear();
              for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                  Order order = dataSnapshot.getValue(Order.class);
                  if(order != null){
                      if(order.getStatus() == 0){
                          list.add(order);
                      }
                  }else {
                      frgInterface.hideLoading();
                      return;
                  }
              }
              adapter.notifyDataSetChanged();
              frgInterface.hideLoading();
              if(list.isEmpty()){
                  frgInterface.showImageEmpty();
              }else {
                  frgInterface.hideImageEmpty();
              }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                frgInterface.hideLoading();
            }
        });
        adapter = new OrderComfirmAdapter(frgInterface.getContext(), list,presenter);
        rcv.setLayoutManager(new LinearLayoutManager(frgInterface.getContext()));
        rcv.setAdapter(adapter);
        adapter.clickToComfirmOrder(position -> {

        });
    }
}
