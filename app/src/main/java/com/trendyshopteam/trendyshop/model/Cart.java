package com.trendyshopteam.trendyshop.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Cart implements Parcelable{
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

    public Cart(String id, String idProduct, String idKhachHang, String productName, String productImage, int soluong,double productPrice, boolean checked, String size) {
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
    public static final Creator<Cart> CREATOR = new Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };
    protected Cart(Parcel in){
        id = in.readString();
        idProduct = in.readString();
        idKhachHang = in.readString();
        productName = in.readString();
        productImage = in.readString();
        soluong = in.readInt();
        productPrice = in.readDouble();
        checked = in.readByte() != 0;
        size = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest,int flags ){
        dest.writeString(id);
        dest.writeString(idProduct);
        dest.writeString(idKhachHang);
        dest.writeString(productName);
        dest.writeString(productImage);
        dest.writeInt(soluong);
        dest.writeDouble(productPrice);
        dest.writeByte((byte) (checked?1:0));
        dest.writeString(size);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public String getId() {
        return id;
    }

    public Cart setId(String id) {
        this.id = id;
        return this;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public Cart setIdProduct(String idProduct) {
        this.idProduct = idProduct;
        return this;
    }

    public String getIdKhachHang() {
        return idKhachHang;
    }

    public Cart setIdKhachHang(String idKhachHang) {
        this.idKhachHang = idKhachHang;
        return this;
    }

    public String getSize() {
        return size;
    }

    public Cart setSize(String size) {
        this.size = size;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public Cart setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public String getProductImage() {
        return productImage;
    }

    public Cart setProductImage(String productImage) {
        this.productImage = productImage;
        return this;
    }

    public int getSoluong() {
        return soluong;
    }

    public Cart setSoluong(int soluong) {
        this.soluong = soluong;
        return this;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public Cart setProductPrice(double productPrice) {
        this.productPrice = productPrice;
        return this;
    }

    public boolean isChecked() {
        return checked;
    }

    public Cart setChecked(boolean checked) {
        this.checked = checked;
        return this;
    }

}
