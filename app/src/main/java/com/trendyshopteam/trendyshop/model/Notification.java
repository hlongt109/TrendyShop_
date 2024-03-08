package com.trendyshopteam.trendyshop.model;

public class Notification {
    private String notificationId, userId, content, timestamp;
    private int status;

    public Notification() {
    }

    public Notification(String notificationId, String userId, String content, String timestamp, int status) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.content = content;
        this.timestamp = timestamp;
        this.status = status;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
