package com.example.warkopzara.data;

import com.example.warkopzara.data.model.LoggedInUser;
import com.example.warkopzara.data.model.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that handles all database query for products.
 */
public class ProductDataSource {

    public Result<List<Product>> getAll() {
        ArrayList<Product> products = new ArrayList<>();
        try {
            return new Result.Success<>(getDummyData());
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public static ArrayList<Product> getDummyData() {
        ArrayList<Product> dummyData = new ArrayList<>();
        dummyData.add(new Product("","Barang 1", 1, 1000));
        dummyData.add(new Product("","Barang 2", 2, 2000));
        dummyData.add(new Product("","Barang 3", 3, 3000));
        dummyData.add(new Product("","Barang 4", 4, 4000));
        return dummyData;
    }
}