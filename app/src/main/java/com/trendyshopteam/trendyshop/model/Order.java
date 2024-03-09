package com.trendyshopteam.trendyshop.model;

public class Order {
    private String orderId;
    private String userId;
    private String customerName;
    private String customerPhone;
    private String address;
    private double totalAmount;
    private String timestamp;
    private int status;

    public Order() {
    }

    public Order(String orderId, String userId, String customerName, String customerPhone, String address, double totalAmount, String timestamp, int status) {
        this.orderId = orderId;
        this.userId = userId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.address = address;
        this.totalAmount = totalAmount;
        this.timestamp = timestamp;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public Order setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public Order setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Order setCustomerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public Order setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Order setAddress(String address) {
        this.address = address;
        return this;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public Order setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Order setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Order setStatus(int status) {
        this.status = status;
        return this;
    }
}
