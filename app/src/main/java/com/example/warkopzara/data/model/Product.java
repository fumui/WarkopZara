package com.example.warkopzara.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private int id;
    private String image;
    private String name;
    private int stock;
    private int price;
    private int orderCount;

    public Product() {}

    public Product(String image, String name, int stock, int price) {
        this.image = image;
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.orderCount = 0;
    }

    public Product(int id, String image, String name, int stock, int price, int orderCount) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.orderCount = orderCount;
    }

    protected Product(Parcel in) {
        id = in.readInt();
        image = in.readString();
        name = in.readString();
        stock = in.readInt();
        price = in.readInt();
        orderCount = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(image);
        dest.writeString(name);
        dest.writeInt(stock);
        dest.writeInt(price);
        dest.writeInt(orderCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public void addOrderCount() {
        this.orderCount++;
    }

    public void subtractOrderCount() {
        this.orderCount--;
    }
}
