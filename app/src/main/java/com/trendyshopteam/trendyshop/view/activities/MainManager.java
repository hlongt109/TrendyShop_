package com.trendyshopteam.trendyshop.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.databinding.ActivityMainManagerBinding;
import com.trendyshopteam.trendyshop.presenter.MainManagePresenter;
import com.trendyshopteam.trendyshop.view.fragments.AboutFragment;
import com.trendyshopteam.trendyshop.view.fragments.DeliverAddressFragment;
import com.trendyshopteam.trendyshop.view.fragments.DetailsFragment;
import com.trendyshopteam.trendyshop.view.fragments.HelpFragment;
import com.trendyshopteam.trendyshop.view.fragments.MainManageFragment;
import com.trendyshopteam.trendyshop.view.fragments.NotificationFragment;
import com.trendyshopteam.trendyshop.view.fragments.OrderFragment;
import com.trendyshopteam.trendyshop.view.fragments.PaymentFragment;
import com.trendyshopteam.trendyshop.view.fragments.PromoFragment;

public class MainManager extends AppCompatActivity {
    private ActivityMainManagerBinding binding;
    private DrawerLayout drawerLayout;

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

                if (itemID == R.id.itemOder) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, new OrderFragment()).commit();
                    toolbar.setTitle(R.string.title_order);
                }
                else if (itemID == R.id.itemDetails) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, new DetailsFragment()).commit();
                    toolbar.setTitle(R.string.title_Details);
                }
                else if (itemID == R.id.itemAddress) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, new DeliverAddressFragment()).commit();
                    toolbar.setTitle(R.string.title_Delivety);
                }
                else if (itemID == R.id.itemPayment) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, new PaymentFragment()).commit();
                    toolbar.setTitle(R.string.title_payment);
                }
                else if (itemID == R.id.itemPromo) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, new PromoFragment()).commit();
                    toolbar.setTitle(R.string.title_promo);
                }
                else if (itemID == R.id.itemNotification) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, new NotificationFragment()).commit();
                    toolbar.setTitle(R.string.title_Notification);
                }
                else if (itemID == R.id.itemHelp) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, new HelpFragment()).commit();
                    toolbar.setTitle(R.string.title_help);
                }
                else if (itemID == R.id.itemAbout) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainFragment, new AboutFragment()).commit();
                    toolbar.setTitle(R.string.title_about);
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
        }

        return super.onOptionsItemSelected(item);
    }


}