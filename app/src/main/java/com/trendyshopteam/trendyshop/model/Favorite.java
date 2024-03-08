package com.trendyshopteam.trendyshop.model;

public class Favorite {
    private String favoriteId, userId, productId;
    public Favorite() {
    }
    public Favorite(String favoriteId, String userId, String productId) {
        this.favoriteId = favoriteId;
        this.userId = userId;
        this.productId = productId;
    }
    public String getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(String favoriteId) {
        this.favoriteId = favoriteId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
