package com.example.warkopzara.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Transaction implements Parcelable {
    private String id;
    private Date date;
    private Product[] products;
    private int totalAmount;

    public Transaction() {
    }

    public Transaction(String id, Date date, Product[] products, int totalAmount) {
        this.id = id;
        this.date = date;
        this.products = products;
        this.totalAmount = totalAmount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Product[] getProducts() {
        return products;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void addProduct(Product product) {
        if (products == null){
            products = new Product[0];
        }
        // Check if the product already exists in the transaction
        for (Product existingProduct : products) {
            if (existingProduct.getId() == product.getId()) {
                existingProduct.addOrderCount();
                return;
            }
        }

        // If the product doesn't exist, add it to the transaction
        int currentLength = products.length;
        Product[] updatedProducts = new Product[currentLength + 1];
        System.arraycopy(products, 0, updatedProducts, 0, currentLength);
        updatedProducts[currentLength] = product;
        products = updatedProducts;
        this.calculateTotalAmount();
    }
    public void removeProduct(Product product) {
        // Find the index of the product in the transaction
        int index = -1;
        for (int i = 0; i < products.length; i++) {
            if (products[i].getId() == product.getId()) {
                index = i;
                break;
            }
        }

        // If the product is found, decrement the order count or remove it
        if (index != -1) {
            Product existingProduct = products[index];
            if (existingProduct.getOrderCount() > 1) {
                existingProduct.subtractOrderCount();
            } else {
                // Remove the product from the transaction
                int currentLength = products.length;
                Product[] updatedProducts = new Product[currentLength - 1];
                System.arraycopy(products, 0, updatedProducts, 0, index);
                System.arraycopy(products, index + 1, updatedProducts, index, currentLength - index - 1);
                products = updatedProducts;
            }
        }
        this.calculateTotalAmount();
    }

    public void calculateTotalAmount() {
        int sum = 0;
        if (products == null){
            return;
        }
        for (Product product : this.products) {
            sum += (product.getPrice() * product.getOrderCount());
        }
        this.totalAmount = sum;
    }

    protected Transaction(Parcel in) {
        id = in.readString();
        long tmpDate = in.readLong();
        date = tmpDate != -1 ? new Date(tmpDate) : null;
        products = in.createTypedArray(Product.CREATOR);
        totalAmount = in.readInt();
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeLong(date != null ? date.getTime() : -1);
        dest.writeTypedArray(products, flags);
        dest.writeInt(totalAmount);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
