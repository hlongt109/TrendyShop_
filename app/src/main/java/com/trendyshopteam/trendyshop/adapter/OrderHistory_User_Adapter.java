package com.trendyshopteam.trendyshop.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.model.Bill;
import com.trendyshopteam.trendyshop.view.activities.UserActivitys.BillDetailsActivity;

import java.lang.reflect.Array;
import java.security.Timestamp;
import java.text.Bidi;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class OrderHistory_User_Adapter extends RecyclerView.Adapter<OrderHistory_User_Adapter.viewHolep> {

    private ArrayList<Bill> list;
    private Context context;
    private FirebaseDatabase database;

    public OrderHistory_User_Adapter(ArrayList<Bill> list, Context context, FirebaseDatabase database) {
        this.list = list;
        this.context = context;
        this.database = database;
    }


    @NonNull
    @Override
    public viewHolep onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill_history, parent, false);
        return new viewHolep(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolep holder, int position) {
        DecimalFormat format = new DecimalFormat("###,###");
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Bill bill = list.get(position);
//        Date date = new Date(bill.getTimestamp());
////        String date = simpleDateFormat.format(bill.getTimestamp());
//        Log.d("DATE", "checkDate: " + date);
        holder.tvBillId.setText(bill.getBillId());
        holder.tvTotal.setText(format.format(bill.getTotalAmount()));
        holder.tvDate.setText(bill.getTimestamp());
        holder.tvUserId.setText(bill.getUserId());

        if (bill.getStatus() == 0) {
            holder.tvWaitComfirm.setText("Wait for confirmation");
            holder.tvWaitComfirm.setTextColor(Color.RED);
        } else if (bill.getStatus() == 1) {
            holder.tvWaitComfirm.setText("Please select item received");
            holder.btnComfirm.setVisibility(View.VISIBLE);
        }

        holder.btnComfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = database.getReference("Bill").child(bill.getBillId()).child("status");
                reference.setValue(2).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Xác nhận thành công", Toast.LENGTH_SHORT).show();
                        // Cập nhật giao diện nếu cần
                        holder.btnComfirm.setVisibility(View.GONE);
                        holder.tvWaitComfirm.setText("Goods received");
                        holder.tvWaitComfirm.setTextColor(Color.GREEN);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String billId = bill.getBillId();
                Intent intent = new Intent(context, BillDetailsActivity.class);
                intent.putExtra("billId", billId);
                Log.d("BILLID", "BillId" + billId);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewHolep extends RecyclerView.ViewHolder {
        TextView tvBillId, tvTotal, tvDate, tvUserId, tvWaitComfirm;
        Button btnComfirm;

        public viewHolep(@NonNull View itemView) {
            super(itemView);
            tvBillId = itemView.findViewById(R.id.billId_User);
            tvTotal = itemView.findViewById(R.id.Total_User);
            tvDate = itemView.findViewById(R.id.DateTime_User);
            tvUserId = itemView.findViewById(R.id.userId_User);
            tvWaitComfirm = itemView.findViewById(R.id.statusBill_User);
            btnComfirm = itemView.findViewById(R.id.btnConfirm);
        }
    }


}
