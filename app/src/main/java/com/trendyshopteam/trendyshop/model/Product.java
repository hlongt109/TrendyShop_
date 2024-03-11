package com.trendyshopteam.trendyshop.model;

import java.util.HashMap;

public class Product {
    private String productId, productTypeId, productName, description;
    private double rate;
    private String imgProduct;
    private HashMap<String, SizeInfo> sizeInfoMap;
    public Product() {
    }

    public Product(String productId, String productTypeId, String productName, String description, double rate, String imgProduct, HashMap<String, SizeInfo> sizeInfoMap) {
        this.productId = productId;
        this.productTypeId = productTypeId;
        this.productName = productName;
        this.description = description;
        this.rate = rate;
        this.imgProduct = imgProduct;
        this.sizeInfoMap = sizeInfoMap;
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

    public HashMap<String, SizeInfo> getSizeInfoMap() {
        return sizeInfoMap;
    }

    public void setSizeInfoMap(HashMap<String, SizeInfo> sizeInfoMap) {
        this.sizeInfoMap = sizeInfoMap;
    }
}
