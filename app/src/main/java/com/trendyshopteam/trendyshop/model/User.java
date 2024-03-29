package com.trendyshopteam.trendyshop.model;

public class User {
    private String id;
    private String email;
    private String phone;
    private String password;

    private String fullname;
    private String address;
    private String role;
    private String photo;
    private String lastLogin;

    public User(String id, String email, String phone,String password, String fullname, String address, String role, String photo, String lastLogin) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.fullname = fullname;
        this.address = address;
        this.role = role;
        this.photo = photo;
        this.lastLogin = lastLogin;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public User setId(String id) {
        this.id = id;
        return this;
    }


    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getFullname() {
        return fullname;
    }

    public User setFullname(String fullname) {
        this.fullname = fullname;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public User setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getRole() {
        return role;
    }

    public User setRole(String role) {
        this.role = role;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public User setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public User setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
        return this;
    }
}
