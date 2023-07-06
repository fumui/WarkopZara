package com.example.warkopzara.ui.checkout;

import android.content.Intent;
import android.os.Bundle;

import com.example.warkopzara.data.model.Transaction;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.warkopzara.databinding.ActivityTransactionBinding;

import com.example.warkopzara.R;

public class CheckoutActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityTransactionBinding binding;

    private Transaction cart;

    public void setCart(Transaction cart) {
        this.cart = cart;
    }

    public Transaction getCart() {
        return cart;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        cart = intent.getParcelableExtra("cart");
        binding = ActivityTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_transaction);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_transaction);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}