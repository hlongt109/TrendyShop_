package com.trendyshopteam.trendyshop.model;

public class Product {
    private String productId, productTypeId, productName, description;
    private double price;
    private double rate;
    private String imgProduct;
    public Product() {
    }

    public Product(String productId, String productTypeId, String productName, String description, double price, double rate, String imgProduct) {
        this.productId = productId;
        this.productTypeId = productTypeId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.rate = rate;
        this.imgProduct = imgProduct;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getImgProduct() {
        return imgProduct;
    }

    public void setImgProduct(String imgProduct) {
        this.imgProduct = imgProduct;
    }
}
