package com.trendyshopteam.trendyshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.model.Notification;

public class Notification_Adapter extends FirebaseRecyclerAdapter<Notification, Notification_Adapter.MyViewHolder> {

    public Notification_Adapter(@NonNull FirebaseRecyclerOptions<Notification> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int i, @NonNull Notification model) {
        holder.text_notification.setText(model.getContent());
        holder.date_notification.setText(model.getTimestamp());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new MyViewHolder(view);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView text_notification, date_notification;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text_notification = itemView.findViewById(R.id.text_notification);
            date_notification = itemView.findViewById(R.id.date_notification);
        }
    }

}
