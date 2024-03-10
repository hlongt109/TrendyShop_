package com.trendyshopteam.trendyshop.presenter;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
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
import com.trendyshopteam.trendyshop.adapter.CartAdapter;
import com.trendyshopteam.trendyshop.interfaces.CartInterface;
import com.trendyshopteam.trendyshop.interfaces.InterfaceUpdateCart;
import com.trendyshopteam.trendyshop.model.Cart;
import com.trendyshopteam.trendyshop.utilities.SharePreferencesManage;
import com.trendyshopteam.trendyshop.view.activities.PayOrderActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartPresenter {
    private CartInterface cartInterface;
    private ArrayList<Cart> list = new ArrayList<>();
    private ArrayList<Cart> selectedCartList = new ArrayList<>();
    private CartAdapter cartAdapter;
    private DatabaseReference databaseReference;
    private SharePreferencesManage preferencesManage;

    public CartPresenter(CartInterface cartInterface) {
        this.cartInterface = cartInterface;
    }
    public void setDataOnRecyclerView( RecyclerView rcv,CartPresenter presenter){
         cartInterface.showLoading();
         preferencesManage = new SharePreferencesManage(cartInterface.getContext());
         String userId = preferencesManage.getUserId();
         Log.d("UserId", "UserId: "+userId);
         databaseReference = FirebaseDatabase.getInstance().getReference("Cart");
         databaseReference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 list.clear();
                 selectedCartList.clear();
                 double totalPrice = 0;
                 boolean anyChecked = false;
                 for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                     Cart cart = dataSnapshot.getValue(Cart.class);
                     if(cart != null){
                         if(cart.getIdKhachHang().equals(userId)){
                             list.add(cart);
                             if(cart.isChecked()){
                                 selectedCartList.add(cart);
                                 totalPrice += cart.getSoluong() * cart.getProductPrice();
                                 anyChecked = true;
                             }
                         }

                     }else {
                         cartInterface.hideLoading();
                         return;
                     }
                 }
                 Locale locale = new Locale("vi","VN");
                 NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                 String price = numberFormat.format(totalPrice);
                 cartInterface.showTotalPrice(price);
                 cartAdapter.notifyDataSetChanged();

                 if (anyChecked) {
                     cartInterface.enableBtnDeleteCart();
                 } else {
                     cartInterface.hideBtnDeleteCart();
                 }
                 cartInterface.hideLoading();
                 if(list.isEmpty()) {
                     cartInterface.showImageEmpty();
                 } else {
                     cartInterface.hideImageEmpty();
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {
                 cartInterface.hideLoading();
             }
         });
         cartAdapter = new CartAdapter(cartInterface.getContext(), list,presenter);
         rcv.setLayoutManager(new LinearLayoutManager(cartInterface.getContext()));
         rcv.setAdapter(cartAdapter);
         cartAdapter.clickToUpdate(new InterfaceUpdateCart() {
             @Override
             public void onCongSoLuong(int position) {
                 Cart cart = list.get(position);
                 cart.setSoluong(cart.getSoluong() +1);
                 updateCartQuantity(cart);
             }

             @Override
             public void onTruSoLuong(int position) {
                 Cart cart = list.get(position);
                 if(cart.getSoluong() >1){
                     cart.setSoluong(cart.getSoluong() -1);
                     updateCartQuantity(cart);
                 }else {
                     Toast.makeText(cartInterface.getContext(), "Minimum quantity reached", Toast.LENGTH_SHORT).show();
                 }
             }

             @Override
             public void onSetChecked(int position) {
                 // cap nhat checked
                 Cart cart = list.get(position);
                 updateChecked(cart);
             }
         });
    }
    private void updateChecked(Cart cart){
        cart.setChecked(!cart.isChecked());
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(cart.getId());
        cartRef.setValue(cart);
    }
    private void updateCartQuantity(Cart cart){
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(cart.getId());
        cartRef.setValue(cart);
    }
    public void deleteCartChecked(){
        preferencesManage = new SharePreferencesManage(cartInterface.getContext());
        String userId = preferencesManage.getUserId();
        AlertDialog.Builder builder = new AlertDialog.Builder(cartInterface.getContext());
        builder.setTitle("Message");
        builder.setMessage("Do you want to delete these products?");
        builder.setNegativeButton("No",null);
        builder.setPositiveButton("Yes",(dialog, which) -> {
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
                    dialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(cartInterface.getContext(), "Deleted error", Toast.LENGTH_SHORT).show();
                    Log.e("DeleteCartChecked", "Cancelled", error.toException());
                    dialog.dismiss();
                }
            });
        });
        builder.create().show();
    }
    public void gotoCheckout(){
        if(selectedCartList.isEmpty()){
            Toast.makeText(cartInterface.getContext(), "No product has been selected yet", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(cartInterface.getContext(), PayOrderActivity.class);
            intent.putParcelableArrayListExtra("selectedCartList", selectedCartList);
            cartInterface.getContext().startActivity(intent);
        }
    }
}
