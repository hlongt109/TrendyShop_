package com.trendyshopteam.trendyshop.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.trendyshopteam.trendyshop.adapter.OrderAdapter;
import com.trendyshopteam.trendyshop.databinding.LayoutSelectPaymentMethodBinding;
import com.trendyshopteam.trendyshop.interfaces.PayOrderInterface;
import com.trendyshopteam.trendyshop.model.Cart;
import com.trendyshopteam.trendyshop.model.Order;
import com.trendyshopteam.trendyshop.model.OrderDetail;
import com.trendyshopteam.trendyshop.utilities.SharePreferencesManage;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class PayOrderPresenter {
    private PayOrderInterface payOrderInterface;
    private String paymentMethod = "";
    double totalPrice = 0;
    private OrderAdapter adapter;
    private DatabaseReference databaseReference;
    private SharePreferencesManage preferencesManage;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public PayOrderPresenter(PayOrderInterface payOrderInterface) {
        this.payOrderInterface = payOrderInterface;
    }

    public void setDataOnRcv(RecyclerView rcv, PayOrderPresenter presenter) {
        ArrayList<Cart> list = payOrderInterface.getIntent().getParcelableArrayListExtra("selectedCartList");
        adapter = new OrderAdapter(payOrderInterface.getContext(), list, presenter);
        rcv.setLayoutManager(new LinearLayoutManager(payOrderInterface.getContext()));
        rcv.setAdapter(adapter);
        for (Cart cart : list) {
            totalPrice += cart.getSoluong() * cart.getProductPrice();
        }
        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        String price = numberFormat.format(totalPrice);
        payOrderInterface.returnTotalPrice(price);
    }

    public void setUserInformation(TextView name, TextView phone, TextView address) {
        preferencesManage = new SharePreferencesManage(payOrderInterface.getContext());
        String userId = preferencesManage.getUserId();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    name.setText(snapshot.child("fullname").getValue(String.class));
                    phone.setText(snapshot.child("phone").getValue(String.class));
                    address.setText(snapshot.child("address").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void handleOrder(String name, String phone, String address) {
        if (validate(name, phone, address, paymentMethod)) {
            saveOrder(name, phone, address);
        }
    }

    public void selectPaymentMethod() {
        AlertDialog.Builder builder = new AlertDialog.Builder(payOrderInterface.getContext());
        LayoutInflater inflater = ((Activity) payOrderInterface.getContext()).getLayoutInflater();
        LayoutSelectPaymentMethodBinding binding = LayoutSelectPaymentMethodBinding.inflate(inflater);
        View view = binding.getRoot();
        builder.setTitle("Choose payment method");
        builder.setView(view);
        builder.setCancelable(false);
        Dialog dialog = builder.create();
        dialog.show();

        binding.tvOffline.setOnClickListener(v -> {
            payOrderInterface.returnSelectedPayment(binding.tvOffline.getText().toString());
            paymentMethod = binding.tvOffline.getText().toString();
            dialog.dismiss();
        });
        binding.tvZaloPay.setOnClickListener(v -> {
            // xu ly thanh toan zlPay
            Toast.makeText(payOrderInterface.getContext(), "Feature under development.Please select another payment method.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        binding.tvMomo.setOnClickListener(v -> {
            // xu ly thanh toan momo
            Toast.makeText(payOrderInterface.getContext(), "Feature under development.Please select another payment method.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        binding.tvPaypal.setOnClickListener(v -> {
            // xu ly thanh toan paypal
            Toast.makeText(payOrderInterface.getContext(), "Feature under development.Please select another payment method.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }

    private void saveOrder(String name, String phone, String address) {
        loading(true);
        ArrayList<Cart> list = payOrderInterface.getIntent().getParcelableArrayListExtra("selectedCartList");
        databaseReference = FirebaseDatabase.getInstance().getReference("Order");
        preferencesManage = new SharePreferencesManage(payOrderInterface.getContext());
        String userId = preferencesManage.getUserId();
        String orderId = UUID.randomUUID().toString();
        String time = dateFormat.format(calendar.getTime());

        Order order = new Order(orderId, userId, name, phone, address, totalPrice, time, 0);
        databaseReference.child(orderId).setValue(order).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<OrderDetail> orderDetailsList = new ArrayList<>();
                for (Cart cart : list) {
                    OrderDetail orderDetail = new OrderDetail();
                    String id = UUID.randomUUID().toString();
                    orderDetail.setId(id);
                    orderDetail.setOrderId(orderId);
                    orderDetail.setIdProduct(cart.getIdProduct());
                    orderDetail.setImageProduct(cart.getProductImage());
                    orderDetail.setNameProduct(cart.getProductName());
                    orderDetail.setPrice(cart.getProductPrice());
                    orderDetail.setQuantity(cart.getSoluong());
                    orderDetail.setSize(cart.getSize());
                    orderDetailsList.add(orderDetail);
                }

                saveOrderDetails(orderDetailsList);
            }
        }).addOnFailureListener(e -> {
            Log.e("Error order", "Error : " + e.getMessage());
            Toast.makeText(payOrderInterface.getContext(), "Error, please try again", Toast.LENGTH_SHORT).show();
        });

    }

    private void saveOrderDetails(ArrayList<OrderDetail> orderDetailsList) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("OrderDetails");
        for (OrderDetail orderDetail : orderDetailsList) {
            reference.child(orderDetail.getId()).setValue(orderDetail)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            deleteCart();
                            loading(false);
                            payOrderInterface.orderSuccess();
                        } else {
                            Toast.makeText(payOrderInterface.getContext(), "Order failed", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {

                    });
        }
    }

    private boolean validate(String name, String phone, String address, String payMethod) {
        if (TextUtils.isEmpty(name)) {
            payOrderInterface.customerNameEmpty();
            return false;
        } else {
            payOrderInterface.clearError();
        }
        if (TextUtils.isEmpty(phone)) {
            payOrderInterface.customerPhoneEmpty();
            return false;
        } else {
            payOrderInterface.clearError();
        }
        if (TextUtils.isEmpty(address)) {
            payOrderInterface.customerAddressEmpty();
            return false;
        } else {
            payOrderInterface.clearError();
        }
        if (TextUtils.isEmpty(payMethod)) {
            payOrderInterface.payMethodEmpty();
            return false;
        } else {
            payOrderInterface.clearError();
        }
        return true;
    }

    private void loading(boolean isLoading) {
        if (isLoading) {
            payOrderInterface.showLoading();
        } else {
            payOrderInterface.hideLoading();
        }
    }

    private void deleteCart() {
        preferencesManage = new SharePreferencesManage(payOrderInterface.getContext());
        String userId = preferencesManage.getUserId();
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart");
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Cart cart = dataSnapshot.getValue(Cart.class);
                    if(cart != null && cart.getIdKhachHang().equals(userId)&& cart.isChecked()){
                        dataSnapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DeleteCartChecked", "Cancelled", error.toException());
            }
        });
    }

}
