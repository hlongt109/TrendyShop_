package com.trendyshopteam.trendyshop.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.adapter.UserAdapter;
import com.trendyshopteam.trendyshop.database.UserDAO;
import com.trendyshopteam.trendyshop.interfaces.UserManageInterface;
import com.trendyshopteam.trendyshop.model.User;
import com.trendyshopteam.trendyshop.view.activities.CreateUserActivity;

import java.util.ArrayList;

public class UserManagerPresenter {
    private UserManageInterface userManageInterface;
    private UserDAO userDAO;
    private ArrayList<User> list = new ArrayList<>();
    private UserAdapter userAdapter;
    private DatabaseReference databaseReference;

    public UserManagerPresenter(UserManageInterface userManageInterface) {
        this.userManageInterface = userManageInterface;
    }

    public void loadDataOnView(Context context, RecyclerView rcv, UserManagerPresenter presenter){
          databaseReference = FirebaseDatabase.getInstance().getReference("User");
          databaseReference.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  list.clear();
                  for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                      User user = dataSnapshot.getValue(User.class);
                      if(user != null){
                          list.add(user);
                      }
                  }
                  userAdapter.notifyDataSetChanged();
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });
          userAdapter = new UserAdapter(context,list,presenter);
          rcv.setLayoutManager(new LinearLayoutManager(context));
          rcv.setAdapter(userAdapter);
          userAdapter.showMenu((user, view) -> {
              showMenuUtities(user,view);
          });

     }
     public void addNewUser(){
         userManageInterface.getContext().startActivity(new Intent(userManageInterface.getContext(), CreateUserActivity.class));
     }
     private void showMenuUtities(User user, View view){
         PopupMenu popupMenu = new PopupMenu(userManageInterface.getContext(), view);
         popupMenu.inflate(R.menu.option_menu);
         popupMenu.setGravity(Gravity.CENTER_VERTICAL);
         popupMenu.setOnMenuItemClickListener(item -> {
             if(item.getItemId() == R.id.navUpdate){
                 openUpdate(user);
                 return true;
             }else if (item.getItemId() == R.id.navDelete){
                 openDelete(user);
                 return true;
             }else {
                 return false;
             }
         });
         popupMenu.show();
     }
    private void openUpdate(User user) {

    }

    private void openDelete(User user) {
    }
}
