package com.trendyshopteam.trendyshop.model;

public class Bill {
    private String billId, userId, address, timestamp;
    private Double totalAmount;
    private int status;

    public Bill() {
    }

    public Bill(String billId, String userId, String address, String timestamp, Double totalAmount, int status) {
        this.billId = billId;
        this.userId = userId;
        this.address = address;
        this.timestamp = timestamp;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
