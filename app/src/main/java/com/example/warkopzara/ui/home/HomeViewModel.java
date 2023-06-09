package com.example.warkopzara.ui.home;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.warkopzara.R;
import com.example.warkopzara.data.ProductAdapter;
import com.example.warkopzara.data.ProductRepository;
import com.example.warkopzara.data.Result;
import com.example.warkopzara.data.model.LoggedInUser;
import com.example.warkopzara.data.model.Product;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private static final String TAG = "HomeViewModel";

    private final MutableLiveData<ProductAdapter> mProductAdapter;
    private ProductRepository productRepository;
    private Activity activity;
    public HomeViewModel(Activity activity, ProductRepository productRepository) {
        this.activity = activity;
        this.productRepository = productRepository;
        mProductAdapter = new MutableLiveData<>();
        this.fetchProducts();
    }

    public void fetchProducts(){
        // can be launched in a separate asynchronous job
        Result<List<Product>> result = productRepository.getProducts();
        if (result instanceof Result.Success) {
            List<Product> data = ((Result.Success<List<Product>>) result).getData();
            mProductAdapter.setValue(new ProductAdapter(activity, data));
        } else {
            Log.d(TAG, "fetchProducts: failed");
            //loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public LiveData<ProductAdapter> getProducts() {
        return mProductAdapter;
    }
}