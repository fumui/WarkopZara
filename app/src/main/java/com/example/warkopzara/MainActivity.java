package com.example.warkopzara;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.example.warkopzara.data.model.Transaction;
import com.example.warkopzara.databinding.ActivityMainBinding;
import com.example.warkopzara.ui.checkout.CheckoutActivity;
import com.example.warkopzara.ui.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int LOGIN_REQUEST_CODE = 999;
    private static final String TAG = "MainActivity";
    public static final int CHECKOUT_REQUEST = 999;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private boolean isLoggedIn = false;

    private Transaction inCart = new Transaction();

    public void setInCart(Transaction inCart) {
        this.inCart = inCart;
    }

    public Transaction getInCart() {
        return inCart;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        binding.appBarMain.fab.setOnClickListener(v -> {
            inCart.calculateTotalAmount();
            inCart.setDate(new Date());
            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.putExtra("cart", inCart);
            startActivityForResult(intent, CHECKOUT_REQUEST);
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHECKOUT_REQUEST && resultCode == 200){
            inCart = new Transaction();
        } else {
            if (data != null){
                String msg = data.getStringExtra("msg");
                if (msg != null && !msg.isEmpty()){
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences(Config.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        isLoggedIn = sharedPreferences.getBoolean(Config.SHARED_PREF_IS_LOGGED_IN_KEY, false);
        if (!isLoggedIn) {
            Intent intent = new Intent(this, LoginActivity.class);
            Log.d(TAG, "onCreate: redirect to login");
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}