package com.trendyshopteam.trendyshop.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.model.Bill;
import com.trendyshopteam.trendyshop.model.User;
import com.trendyshopteam.trendyshop.view.activities.BillDetailsManage_Activity;

import java.text.NumberFormat;
import java.util.Locale;

public class Bill_Adapter extends FirebaseRecyclerAdapter<Bill, Bill_Adapter.myViewHolder> {

    public Bill_Adapter(@NonNull FirebaseRecyclerOptions<Bill> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int i, @NonNull Bill modal) {
        holder.billId_Manager.setText(modal.getBillId());

        Locale vietnamLocale = new Locale("vi", "VN");
        NumberFormat vietnamFormat = NumberFormat.getCurrencyInstance(vietnamLocale);
        String priceFormatted = vietnamFormat.format(modal.getTotalAmount());
        holder.Total_Manager.setText(priceFormatted);

        holder.DateTime_Manager.setText(modal.getTimestamp());

        String userId = modal.getUserId();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");
        userRef.child(userId).get()
                .addOnCompleteListener(task -> {
                    User user = task.getResult().getValue(User.class);
                    if(user != null){
                        holder.userId_Manager.setText(user.getFullname());
                    }
                });

        int status = modal.getStatus();
        if(status == 0){
            holder.statusBill_Manager.setText("wait for confirmation");
        }else if(status == 1){
            holder.statusBill_Manager.setText("confirmed");
        }else{
            holder.statusBill_Manager.setText("Has paid");
            holder.statusBill_Manager.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
        }

        holder.itemView.setOnClickListener(view -> {
            String billId = modal.getBillId();
            Intent intent = new Intent(view.getContext(), BillDetailsManage_Activity.class);
            intent.putExtra("billId", billId);
            view.getContext().startActivity(intent);
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill,parent,false);
        return new myViewHolder(view);
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        TextView billId_Manager,Total_Manager,DateTime_Manager,userId_Manager,statusBill_Manager;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            billId_Manager = itemView.findViewById(R.id.billId_Manager);
            Total_Manager = itemView.findViewById(R.id.Total_Manager);
            DateTime_Manager = itemView.findViewById(R.id.DateTime_Manager);
            userId_Manager = itemView.findViewById(R.id.userId_Manager);
            statusBill_Manager = itemView.findViewById(R.id.statusBill_Manager);
        }

    }
    
}
