package com.example.baitaplon.Domain;

public class Profile {
    private String userId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private boolean admin;

    // Constructor đầy đủ
    public Profile(String userId, String address, String phoneNumber, String email, String name, boolean admin) {
        this.userId = userId;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.name = name;
        this.admin = admin;
    }

    // Constructor mặc định
    public Profile() {
        this.admin = false; // Mặc định là false
    }

    // Getter và Setter cho admin
    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    // Các getter và setter khác
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
