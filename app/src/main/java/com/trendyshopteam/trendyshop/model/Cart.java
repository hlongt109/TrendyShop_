package com.trendyshopteam.trendyshop.model;

public class Cart {
    private String id;
    private String idProduct;
    private String idKhachHang;
    private String productName;
    private String productImage;
    private int soluong;
    private double productPrice;
    private boolean checked;
    private String size;

    public Cart() {
    }

    public Cart(String id, String idProduct, String idKhachHang, String productName, String productImage, int soluong, double productPrice, boolean checked, String size) {
        this.id = id;
        this.idProduct = idProduct;
        this.idKhachHang = idKhachHang;
        this.productName = productName;
        this.productImage = productImage;
        this.soluong = soluong;
        this.productPrice = productPrice;
        this.checked = checked;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getIdKhachHang() {
        return idKhachHang;
    }

    public void setIdKhachHang(String idKhachHang) {
        this.idKhachHang = idKhachHang;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
