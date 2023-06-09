package com.example.warkopzara.data.model;

public class Product {
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
    public Product(String image, String name, int stock, int price, int orderCount) {
        this.image = image;
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.orderCount = orderCount;
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
