package com.trendyshopteam.trendyshop.model;

public class OrderDetail {
    private String id;
    private String orderId;
    private String idProduct;
    private String imageProduct;
    private String nameProduct;
    private double price;
    private int quantity;
    private String size;

    public OrderDetail() {
    }

    public OrderDetail(String id, String orderId, String idProduct, String imageProduct, String nameProduct, double price, int quantity,String size) {
        this.id = id;
        this.orderId = orderId;
        this.idProduct = idProduct;
        this.imageProduct = imageProduct;
        this.nameProduct = nameProduct;
        this.price = price;
        this.quantity = quantity;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public OrderDetail setId(String id) {
        this.id = id;
        return this;
    }

    public String getSize() {
        return size;
    }

    public OrderDetail setSize(String size) {
        this.size = size;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public OrderDetail setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public OrderDetail setIdProduct(String idProduct) {
        this.idProduct = idProduct;
        return this;
    }

    public String getImageProduct() {
        return imageProduct;
    }

    public OrderDetail setImageProduct(String imageProduct) {
        this.imageProduct = imageProduct;
        return this;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public OrderDetail setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public OrderDetail setPrice(double price) {
        this.price = price;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderDetail setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }
}
