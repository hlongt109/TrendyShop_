package com.trendyshopteam.trendyshop.view.activities.UserActivitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.adapter.ProductTypeAdapter_User;
import com.trendyshopteam.trendyshop.databinding.ActivityMainManagerBinding;
import com.trendyshopteam.trendyshop.databinding.ActivityMainUserBinding;
import com.trendyshopteam.trendyshop.utilities.SharePreferencesManage;
import com.trendyshopteam.trendyshop.view.activities.CartActivity;
import com.trendyshopteam.trendyshop.view.activities.Favorite_Activity;
import com.trendyshopteam.trendyshop.view.activities.LoginActivity;
import com.trendyshopteam.trendyshop.view.activities.Notification_Activity;
import com.trendyshopteam.trendyshop.view.fragments.AboutFragment;
import com.trendyshopteam.trendyshop.view.fragments.DeliverAddressFragment;
import com.trendyshopteam.trendyshop.view.fragments.DetailsFragment;
import com.trendyshopteam.trendyshop.view.fragments.FragmentUser.MainUserFragment;
import com.trendyshopteam.trendyshop.view.fragments.FragmentUser.OrderHistoryFragment;
import com.trendyshopteam.trendyshop.view.fragments.HelpFragment;
import com.trendyshopteam.trendyshop.view.fragments.MainManageFragment;
import com.trendyshopteam.trendyshop.view.fragments.NotificationFragment;
import com.trendyshopteam.trendyshop.view.fragments.OrderFragment;
import com.trendyshopteam.trendyshop.view.fragments.PaymentFragment;
import com.trendyshopteam.trendyshop.view.fragments.PromoFragment;

public class MainUserActivity extends AppCompatActivity {
    private ActivityMainUserBinding binding;
    private DrawerLayout drawerLayout;
    private Context context = this;
    private SharePreferencesManage preferencesManage;

    String productTypeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        binding = ActivityMainUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.toolBar;
        toolbar.setTitle(R.string.title_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = binding.navigatinDrawer;
        NavigationView navigationView = binding.navView;

        //flagment

        productTypeId = getIntent().getStringExtra("ProductTypeId");
        Fragment fragment = new MainUserFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TypeId", productTypeId);
        fragment.setArguments(bundle);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, fragment).commit();
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemID = menuItem.getItemId();

                if(itemID == R.id.item_home){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, fragment).commit();
                    toolbar.setTitle(R.string.title_home);
                } else if (itemID == R.id.itemOder) {
                    startActivity(new Intent(context, CartActivity.class));
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, new OrderFragment()).commit();
                    toolbar.setTitle(R.string.title_order);
                } else if (itemID == R.id.itemDetails) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, new DetailsFragment()).commit();
                    toolbar.setTitle(R.string.title_Details);
                } else if (itemID == R.id.itemAddress) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, new DeliverAddressFragment()).commit();
                    toolbar.setTitle(R.string.title_Delivety);
                } else if (itemID == R.id.itemPayment) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, new PaymentFragment()).commit();
                    toolbar.setTitle(R.string.title_payment);
                } else if (itemID == R.id.itemPromo) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, new PromoFragment()).commit();
                    toolbar.setTitle(R.string.title_promo);
                } else if (itemID == R.id.itemNotification) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, new NotificationFragment()).commit();
                    toolbar.setTitle(R.string.title_Notification);
                } else if (itemID == R.id.itemHelp) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, new HelpFragment()).commit();
                    toolbar.setTitle(R.string.title_help);
                } else if (itemID == R.id.itemAbout) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, new AboutFragment()).commit();
                    toolbar.setTitle(R.string.title_about);
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
                } else if (itemID == R.id.item_orderhistory) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, new OrderHistoryFragment()).commit();
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
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
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
        } else if (id == R.id.icon_notification) {
            startActivity(new Intent(context, Notification_Activity.class));
        } else if (id == R.id.icon_favorite) {
            startActivity(new Intent(context, Favorite_Activity.class));
        } else if (id == R.id.icon_cart) {
            startActivity(new Intent(context, CartActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

}