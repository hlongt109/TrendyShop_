package com.trendyshopteam.trendyshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.databinding.ItemUserBinding;
import com.trendyshopteam.trendyshop.interfaces.OnClickUser;
import com.trendyshopteam.trendyshop.model.User;
import com.trendyshopteam.trendyshop.presenter.UserManagerPresenter;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.myViewHolder>{
    private final Context context;
    private final ArrayList<User> list;
    private UserManagerPresenter presenter;
    OnClickUser onClickUser;
    public UserAdapter(Context context, ArrayList<User> list, UserManagerPresenter presenter) {
        this.context = context;
        this.list = list;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBinding binding = ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new myViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        User user = list.get(position);
        holder.setData(user);
        holder.binding.btnMore.setOnClickListener(v -> {
            onClickUser.onClickUser(user,holder.itemView);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{
       private ItemUserBinding binding;
       myViewHolder(ItemUserBinding itemUserBinding){
           super(itemUserBinding.getRoot());
           binding = itemUserBinding;
       }
       void setData(User user){
           Glide.with(context).load(user.getPhoto()).error(R.drawable.image).into(binding.imgUser);
           binding.tvName.setText(user.getFullname());
           if(user.getRole().equals("Admin")){
               binding.tvRole.setText("Quản lý");
           } else if (user.getRole().equals("NhanVien")) {
               binding.tvRole.setText("Nhân Viên");
           }

       }
   }
   public void showMenu(OnClickUser onClickUser){
        this.onClickUser = onClickUser;
   }
}
