package com.example.warkopzara.ui.home;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.warkopzara.Config;
import com.example.warkopzara.MainActivity;
import com.example.warkopzara.data.ProductAdapter;
import com.example.warkopzara.data.ProductRepository;
import com.example.warkopzara.data.Result;
import com.example.warkopzara.data.model.Product;
import com.example.warkopzara.providers.MyUrlRequestCallback;

import org.chromium.net.CronetEngine;
import org.chromium.net.UrlRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomeViewModel extends ViewModel {
    private static final String TAG = "HomeViewModel";

    private final MutableLiveData<ProductAdapter> mProductAdapter;
    private MainActivity activity;
    public HomeViewModel(MainActivity activity) {
        this.activity = activity;
        mProductAdapter = new MutableLiveData<>();
        this.fetchProducts();
    }

    public void fetchProducts(){
        // can be launched in a separate asynchronous job
        CronetEngine.Builder myBuilder = new CronetEngine.Builder(activity);
        CronetEngine cronetEngine = myBuilder.build();
        Executor executor = Executors.newSingleThreadExecutor();
        MyUrlRequestCallback requestCallback = new MyUrlRequestCallback(result -> {
            try {
                JSONObject data = new JSONObject(result.toString());
                String responseBody = data.getString("body");
                int statusCode = data.getInt("statusCode");
                if (statusCode == 200) { //Some kind of success
                    JSONObject body = new JSONObject(responseBody);
                    JSONArray array = body.getJSONArray("result");
                    ArrayList<Product> listProduct = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonProduct = array.getJSONObject(i);
                        listProduct.add(new Product(
                                jsonProduct.getInt("id"),
                                jsonProduct.getString("image"),
                                jsonProduct.getString("name"),
                                jsonProduct.getInt("stock"),
                                jsonProduct.getInt("price"),
                                0
                        ));
                    }

                    mProductAdapter.postValue(new ProductAdapter(activity, listProduct));
                } else {
                    Log.e(TAG, "Failed to fetch data: status "+statusCode +" "+ Config.BE_URL+"/api/v1/product/list");
                    Log.e(TAG, "Failed to fetch data: responseBody "+responseBody);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        UrlRequest.Builder requestBuilder = cronetEngine
                .newUrlRequestBuilder(
                        Config.BE_URL+"/api/v1/product/list",
                        requestCallback,
                        executor
                )
                .setHttpMethod("GET");

        UrlRequest request = requestBuilder.build();
        request.start();
    }

    public LiveData<ProductAdapter> getProducts() {
        return mProductAdapter;
    }
}