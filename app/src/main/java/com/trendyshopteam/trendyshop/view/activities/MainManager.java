package com.trendyshopteam.trendyshop.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.databinding.ActivityMainManagerBinding;
import com.trendyshopteam.trendyshop.databinding.HeaderDrawBinding;
import com.trendyshopteam.trendyshop.databinding.LayoutSelectPaymentMethodBinding;
import com.trendyshopteam.trendyshop.presenter.MainManagePresenter;
import com.trendyshopteam.trendyshop.utilities.SharePreferencesManage;
import com.trendyshopteam.trendyshop.view.fragments.AboutFragment;
import com.trendyshopteam.trendyshop.view.fragments.DeliverAddressFragment;
import com.trendyshopteam.trendyshop.view.fragments.DetailsFragment;
import com.trendyshopteam.trendyshop.view.fragments.FragmentUser.ChangePasswordFragment;
import com.trendyshopteam.trendyshop.view.fragments.HelpFragment;
import com.trendyshopteam.trendyshop.view.fragments.MainManageFragment;
import com.trendyshopteam.trendyshop.view.fragments.NotificationFragment;
import com.trendyshopteam.trendyshop.view.fragments.OrderFragment;
import com.trendyshopteam.trendyshop.view.fragments.PaymentFragment;
import com.trendyshopteam.trendyshop.view.fragments.PromoFragment;

public class MainManager extends AppCompatActivity {
    private ActivityMainManagerBinding binding;
    private DrawerLayout drawerLayout;
    private HeaderDrawBinding headerDrawBinding;
    private Context context = this;
    private SharePreferencesManage preferencesManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.toolBar;
        toolbar.setTitle(R.string.title_toolbar);
        setSupportActionBar(toolbar);


        drawerLayout = binding.navigatinDrawer;
        NavigationView navigationView = binding.navView;

        //flagment
        FragmentTransaction fragment = getSupportFragmentManager().beginTransaction();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, new MainManageFragment()).commit();
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemID = menuItem.getItemId();

                if (itemID == R.id.item_home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, new MainManageFragment()).commit();
                    toolbar.setTitle(R.string.title_home);
                } else if (itemID == R.id.itemDetails) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, new DetailsFragment()).commit();
                    toolbar.setTitle(R.string.title_Details);
                } else if (itemID == R.id.itemNotification) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, new NotificationFragment()).commit();
                    toolbar.setTitle(R.string.title_Notification);
                } else if (itemID == R.id.itemAbout) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, new AboutFragment()).commit();
                    toolbar.setTitle(R.string.title_about);
                } else if (itemID == R.id.itemChangePass) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, new ChangePasswordFragment()).commit();
                    toolbar.setTitle(R.string.title_changePass);
                } else if (itemID == R.id.item_logOut) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Do you want to log out?");
                    builder.setNegativeButton("No",null);
                    builder.setPositiveButton("Yes",(dialog, which) -> {
                        FirebaseAuth.getInstance().signOut();
                        preferencesManage = new SharePreferencesManage(context);
                        preferencesManage.clearUserData();
                        startActivity(new Intent(context, LoginActivity.class));
                        finishAffinity();
                        dialog.dismiss();
                    });
                    builder.setCancelable(false);
                    builder.create().show();
                }
                drawerLayout.closeDrawer(GravityCompat.END);
                return true;
            }
        });


    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_admin, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.icon_menu) {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            }
            drawerLayout.openDrawer(GravityCompat.END);
        }

        if (id == R.id.icon_notification) {
            startActivity(new Intent(context, Notification_Activity.class));
        }
        return super.onOptionsItemSelected(item);
    }

//    @SuppressLint("MissingInflatedId")
//    private void init(){
//        preferencesManage = new SharePreferencesManage(context);
//        String uId = preferencesManage.getUserId();
//        LayoutInflater inflater = ((Activity)context ).getLayoutInflater();
//        headerDrawBinding = HeaderDrawBinding.inflate(inflater);
//        View view = headerDrawBinding.getRoot();
//        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(uId);
//        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    headerDrawBinding.tvFullName.setText(snapshot.child("email").getValue(String.class));
//                    headerDrawBinding.tvFullName.setText(snapshot.child("fullname").getValue(String.class));
//                    Glide.with(context).load(snapshot.child("photo").getValue(String.class)).error(R.drawable.image).into( headerDrawBinding.imageUser);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        setContentView(R.layout.activity_main_manager);
//         ViewGroup mainGroup = findViewById(R.id.toolBar);
//        mainGroup.addView(view,0);
//    }
}