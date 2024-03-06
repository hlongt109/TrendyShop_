package com.trendyshopteam.trendyshop.model;

public class ProductType {
    private String productTypeId, typeName, imgProductType;

    public ProductType() {
    }

    public ProductType(String productTypeId, String typeName, String imgProductType) {
        this.productTypeId = productTypeId;
        this.typeName = typeName;
        this.imgProductType = imgProductType;
    }

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getImgProductType() {
        return imgProductType;
    }

    public void setImgProductType(String imgProductType) {
        this.imgProductType = imgProductType;
    }
}
