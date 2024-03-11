package com.trendyshopteam.trendyshop.view.activities.UserActivitys;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.databinding.ActivityBillDetailsBinding;
import com.trendyshopteam.trendyshop.model.Bill;
import com.trendyshopteam.trendyshop.model.BillDetail;
import com.trendyshopteam.trendyshop.model.Product;

import java.text.DecimalFormat;

public class BillDetailsActivity extends AppCompatActivity {

    private ActivityBillDetailsBinding binding;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private String productId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);
        binding = ActivityBillDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        String billId = intent.getStringExtra("billId");

        databaseReference = FirebaseDatabase.getInstance().getReference("OrderDetails");
        databaseReference.child(billId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    productId = snapshot.child("productId").getValue(String.class);

                    DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Product");
                    productRef.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                DecimalFormat format = new DecimalFormat("###.###");
                                Product product = snapshot.getValue(Product.class);
                                binding.nameProductUser.setText(product.getProductName());
                                Glide.with(getApplicationContext()).load(product.getImgProduct()).into(binding.imgImageProduct);
//                                binding.priceProductUser.setText(format.format(product.getPrice()));
                            }else{

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    DatabaseReference billRef = FirebaseDatabase.getInstance().getReference("Bill");
                    billRef.child(billId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Bill bill = snapshot.getValue(Bill.class);

                                binding.TotalUser.setText(String.valueOf(bill.getTotalAmount()));
                                binding.DateTimeUser.setText(bill.getTimestamp());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogRate();
            }
        });

        binding.backBillManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainUserActivity.class));
                finish();
            }
        });
    }



    private void openDialogRate(){
        AlertDialog.Builder builder = new AlertDialog.Builder(BillDetailsActivity.this);
        LayoutInflater inflater = LayoutInflater.from(BillDetailsActivity.this);
        View view = inflater.inflate(R.layout.dialog_rate,null);
        builder.setView(view);

        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        Button btnSubmit = view.findViewById(R.id.buttonSubmit);

        AlertDialog dialog = builder.create();
        dialog.setTitle("Bạn hài lòng với sản phẩn này chứ?");
        dialog.show();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double rating = ratingBar.getRating();
                DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Product");
                productRef.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                          Product product = snapshot.getValue(Product.class);
                          double rate = product.getRate();

                          if(rate == 0.0){
                              DatabaseReference reference = database.getReference("Product").child(productId).child("rate");
                              reference.setValue(rating).addOnSuccessListener(new OnSuccessListener<Void>() {
                                  @Override
                                  public void onSuccess(Void unused) {
                                      Toast.makeText(getApplicationContext(), "Đánh giá thành công cảm ơn bạn đã ủng hộ", Toast.LENGTH_SHORT).show();
                                  }
                              }).addOnFailureListener(new OnFailureListener() {
                                  @Override
                                  public void onFailure(@NonNull Exception e) {
                                      Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                  }
                              });
                          }else{
                              double newRate = (rate + rating) /2 ;
                              DatabaseReference reference = database.getReference("Product").child(productId).child("rate");
                              reference.setValue(newRate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                  @Override
                                  public void onSuccess(Void unused) {
                                      Toast.makeText(getApplicationContext(), "Đánh giá thành công cảm ơn bạn đã ủng hộ", Toast.LENGTH_SHORT).show();
                                  }
                              }).addOnFailureListener(new OnFailureListener() {
                                  @Override
                                  public void onFailure(@NonNull Exception e) {
                                      Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                  }
                              });
                          }

                        }else{

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                dialog.dismiss();
            }
        });
    }
}