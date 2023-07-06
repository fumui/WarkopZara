package com.example.warkopzara.ui.checkout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.warkopzara.Config;
import com.example.warkopzara.MainActivity;
import com.example.warkopzara.R;
import com.example.warkopzara.data.CheckoutProductAdapter;
import com.example.warkopzara.data.ProductAdapter;
import com.example.warkopzara.data.ProductDataSource;
import com.example.warkopzara.data.model.Product;
import com.example.warkopzara.data.model.Transaction;
import com.example.warkopzara.databinding.FragmentConfirmCheckoutBinding;
import com.example.warkopzara.providers.MyUrlRequestCallback;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.chromium.net.CronetEngine;
import org.chromium.net.UploadDataProviders;
import org.chromium.net.UrlRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CheckoutConfirmationFragment extends Fragment {

    private static final String TAG = "CheckoutConfirmationFragment";
    private FragmentConfirmCheckoutBinding binding;
    private Transaction cart;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentConfirmCheckoutBinding.inflate(inflater, container, false);
        CheckoutActivity checkoutActivity = (CheckoutActivity) getActivity();
        final ListView listView = binding.listviewConfirmProduct;
        cart = checkoutActivity.getCart();
        ArrayList<Product> data = new ArrayList<>();
        for (Product product: cart.getProducts()) {
            data.add(product);
        }
        CheckoutProductAdapter adapter = new CheckoutProductAdapter(checkoutActivity, data);
        listView.setAdapter(adapter);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String totalAmountTemplate = "{{total_amount}}";
        String currentTotalAmount = binding.textViewTotalAmount.getText().toString();
        binding.textViewTotalAmount.setText(currentTotalAmount.replace(totalAmountTemplate,""+cart.getTotalAmount()));
        binding.buttonCheckout.setOnClickListener(v -> this.createTransaction());
    }

    public void createTransaction(){
        Activity currentActivity = getActivity();
        SharedPreferences sharedPreferences = currentActivity.getSharedPreferences(Config.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString(Config.SHARED_PREF_USER_ID_KEY, "");

        JsonArray productsJSON = new JsonArray();
        for (Product product: cart.getProducts()) {
            JsonObject productJSON = new JsonObject();
            productJSON.addProperty("product_id", product.getId());
            productJSON.addProperty("quantity", product.getOrderCount());
            productJSON.addProperty("total_amount", product.getOrderCount()*product.getPrice());
            productsJSON.add(productJSON);
        }

        JsonObject requestBodyJson = new JsonObject();
        requestBodyJson.addProperty("user_id", Integer.parseInt(userId));
        requestBodyJson.add("products", productsJSON);
        String jsonBody = requestBodyJson.toString();

        CronetEngine.Builder myBuilder = new CronetEngine.Builder(currentActivity);
        CronetEngine cronetEngine = myBuilder.build();
        Executor executor = Executors.newSingleThreadExecutor();
        MyUrlRequestCallback requestCallback = new MyUrlRequestCallback(result -> {
            try {
                JSONObject data = new JSONObject(result.toString());
                int statusCode = data.getInt("statusCode");
                String responseBody = data.getString("body");
                currentActivity.setResult(statusCode);
                if (statusCode == 200) { //Some kind of success
                    currentActivity.finishActivity(MainActivity.CHECKOUT_REQUEST);
                    currentActivity.finish();
                } else {
                    Log.e(TAG, "Failed to create transaction: status "+statusCode);
                    Log.e(TAG, "Failed to create transaction: responseBody "+responseBody);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("msg", "Failed to create transaction: status "+statusCode);
                    currentActivity.setIntent(resultIntent);
                    currentActivity.finishActivity(MainActivity.CHECKOUT_REQUEST);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        UrlRequest.Builder requestBuilder = cronetEngine
                .newUrlRequestBuilder(
                        Config.BE_URL+"/api/v1/transaction/create",
                        requestCallback,
                        executor
                )
                .setHttpMethod("POST")
                .addHeader("Content-Type","application/json")
                .setUploadDataProvider(UploadDataProviders.create(jsonBody.getBytes()),executor);

        UrlRequest request = requestBuilder.build();
        request.start();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}