package com.trendyshopteam.trendyshop.view.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.databinding.ActivityProductDetailBinding;
import com.trendyshopteam.trendyshop.model.Cart;
import com.trendyshopteam.trendyshop.model.Product;
import com.trendyshopteam.trendyshop.model.ProductType;
import com.trendyshopteam.trendyshop.utilities.SharePreferencesManage;

import java.text.NumberFormat;
import java.util.Locale;

public class productDetail_Activity extends AppCompatActivity {
    private ActivityProductDetailBinding binding;
    private String productId, userId, productName, productImage;
    private double productPrice;
    private String testId = "nu7e4TnB3VPOSUxRksVMILMblu42";
    private int quantity = 1;
    private View selectedSizeView = null;
    private SharePreferencesManage sharePreferencesManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharePreferencesManage = new SharePreferencesManage(this);
        userId = sharePreferencesManage.getUserId();

        productId = getIntent().getStringExtra("productId");
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Product");
        productRef.child(productId).get()
                .addOnCompleteListener(task -> {
                    Product product = task.getResult().getValue(Product.class);
                    if(product != null){
                        Glide.with(binding.imageFavorite.getContext())
                                .load(product.getImgProduct())
                                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                                .into(binding.imageFavorite);

                        binding.txtNameProduct.setText(product.getProductName());
                        Locale vietnamLocale = new Locale("vi", "VN");
                        NumberFormat vietnamFormat = NumberFormat.getCurrencyInstance(vietnamLocale);
                        String priceFormatted = vietnamFormat.format(product.getPrice());
                        binding.txtPriceProduct.setText(priceFormatted);

                        binding.txtdes.setText(product.getDescription());
                        double rate = product.getRate();
                        if (rate == 0.0) {
                            binding.txtRate.setVisibility(View.GONE);
                        } else {
                            binding.txtRate.setVisibility(View.VISIBLE);
                            binding.txtRate.setText(String.valueOf(rate));
                        }
                        addStarsToLayout(rate);


                        String productTypeId = product.getProductTypeId();
                        DatabaseReference productTypeRef = FirebaseDatabase.getInstance().getReference().child("ProductType");
                        productTypeRef.child(productTypeId).get()
                                .addOnCompleteListener(task1 -> {
                                    ProductType productType = task1.getResult().getValue(ProductType.class);
                                    if(productType != null){
                                        binding.txtNameProductType.setText(productType.getTypeName());
                                    }
                                });

                        productName = product.getProductName();
                        productImage = product.getImgProduct();
                        productPrice = product.getPrice();
                    }
                });

        binding.btnCong.setOnClickListener(view -> {
            quantity++;
            updateQuantityText();
        });

        binding.btnGiam.setOnClickListener(view -> {
            if(quantity > 1){
                quantity--;
                updateQuantityText();
            }
        });

        binding.backFavorite.setOnClickListener(view -> {
            startActivity(new Intent(productDetail_Activity.this, Favorite_Activity.class));
        });

        binding.btnAddToCart.setOnClickListener(view -> {
            addToCart();
        });
    }

    private void addStarsToLayout(double rate) {
        int starCount = 5;
        LinearLayout linearStars = findViewById(R.id.linearStars);

        if (rate == 0.0) {
            TextView noRatingTextView = new TextView(this);
            noRatingTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            noRatingTextView.setText("Chưa có đánh giá");
            linearStars.addView(noRatingTextView);
        } else {
            for (int i = 0; i < starCount; i++) {
                ImageView starImageView = new ImageView(this);
                starImageView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                starImageView.setImageResource(R.drawable.star);
                float fillLevel = (float) (rate - i);
                if (fillLevel >= 1.0) {
                    starImageView.setColorFilter(getResources().getColor(R.color.starExcellent));
                } else if (fillLevel > 0.0) {
                    Drawable drawable = starImageView.getDrawable();
                    if (drawable instanceof BitmapDrawable) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) (bitmap.getWidth() * fillLevel), bitmap.getHeight());
                        starImageView.setImageBitmap(newBitmap);
                    }
                    starImageView.setColorFilter(getResources().getColor(R.color.starExcellent));
                } else {
                    starImageView.setColorFilter(getResources().getColor(R.color.starDefault));
                }

                linearStars.addView(starImageView);
            }
        }
    }
    private void updateQuantityText(){
        binding.txtQuantity.setText(String.valueOf(quantity));
    }
    public void onSizeClick(View view) {
        if (selectedSizeView != null) {
            selectedSizeView.setBackgroundColor(getResources().getColor(R.color.defaultSizeColor));
            selectedSizeView.clearAnimation();
            selectedSizeView.setTag(null);
        }

        TextView sizeTextView = view.findViewById(R.id.size_S);
        if (sizeTextView != null) {
            // Size S is clicked
            view.setBackgroundColor(getResources().getColor(R.color.selectedSizeColor));
            Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
            view.startAnimation(scaleUp);
            selectedSizeView = view;
            selectedSizeView.setTag("S");
        } else {
            sizeTextView = view.findViewById(R.id.size_M);
            if (sizeTextView != null) {
                // Size M is clicked
                view.setBackgroundColor(getResources().getColor(R.color.selectedSizeColor));
                Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
                view.startAnimation(scaleUp);
                selectedSizeView = view;
                selectedSizeView.setTag("M");
            } else {
                sizeTextView = view.findViewById(R.id.size_L);
                if (sizeTextView != null) {
                    // Size L is clicked
                    view.setBackgroundColor(getResources().getColor(R.color.selectedSizeColor));
                    Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
                    view.startAnimation(scaleUp);
                    selectedSizeView = view;
                    selectedSizeView.setTag("L");
                }
            }
        }
    }


    private void addToCart() {
        if (selectedSizeView != null) {
            String selectedSize = (String) selectedSizeView.getTag();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");
            userRef.child(testId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DataSnapshot dataSnapshot = task.getResult();
                            if (dataSnapshot.exists()) {
                                    String address = dataSnapshot.child("address").getValue(String.class);
                                    if (address != null && !address.isEmpty()) {
                                        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart");
                                        String cartId = cartRef.push().getKey();
                                        Cart cart = new Cart(cartId, productId, userId, productName, productImage, quantity, productPrice, false, selectedSize);
                                        cartRef.child(cartId).setValue(cart);
                                        Toast.makeText(this, "Sản phẩm đã được thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent intent = new Intent(productDetail_Activity.this, changeUserInformation_Activity.class);
                                        intent.putExtra("productId", productId);
                                        startActivity(intent);
                                        Toast.makeText(this, "Vui lòng nhập địa chỉ để thêm sản phẩm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                                    }
                            }
                        }
                    });
        }else{
            Toast.makeText(this, "Vui lòng chọn size để thêm sản phẩm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        }
    }
}