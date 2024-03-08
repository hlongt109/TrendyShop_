package com.trendyshopteam.trendyshop.model;

public class BillDetail {
    private String billDetailId, billId, productId;
    private int quantity;
    private Double priceProduct, total;

    public BillDetail() {
    }

    public BillDetail(String billDetailId, String billId, String productId, int quantity, Double priceProduct, Double total) {
        this.billDetailId = billDetailId;
        this.billId = billId;
        this.productId = productId;
        this.quantity = quantity;
        this.priceProduct = priceProduct;
        this.total = total;
    }

    public String getBillDetailId() {
        return billDetailId;
    }

    public void setBillDetailId(String billDetailId) {
        this.billDetailId = billDetailId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(Double priceProduct) {
        this.priceProduct = priceProduct;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
