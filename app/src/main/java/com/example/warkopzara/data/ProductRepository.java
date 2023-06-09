package com.example.warkopzara.data;

import androidx.lifecycle.LiveData;

import com.example.warkopzara.data.model.LoggedInUser;
import com.example.warkopzara.data.model.Product;

import java.util.List;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class ProductRepository {

    private static volatile ProductRepository instance;

    private ProductDataSource dataSource;

    private List<Product> products = null;

    // private constructor : singleton access
    private ProductRepository(ProductDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static ProductRepository getInstance(ProductDataSource dataSource) {
        if (instance == null) {
            instance = new ProductRepository(dataSource);
        }
        return instance;
    }

    private void setProducts(List<Product> products) {
        this.products = products;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<List<Product>> getProducts() {
        // handle login
        Result<List<Product>> result = dataSource.getAll();
        if (result instanceof Result.Success) {
            setProducts(((Result.Success<List<Product>>) result).getData());
        }
        return result;
    }
}