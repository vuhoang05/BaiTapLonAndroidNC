package com.example.baitaplon.Domain;

public class Blog {
    private String postID;
    private String title;
    private String description;
    private String imageUrl;
    private String area;
    private String price;
    private String numberOfRooms;
    private String address;
    private String userID;
    private String sdt;
    private String houseType;

    public Blog() {
    }

    public Blog(String postID, String houseType, String sdt, String userID, String address, String numberOfRooms, String price, String area, String imageUrl, String description, String title) {
        this.postID = postID;
        this.houseType = houseType;
        this.sdt = sdt;
        this.userID = userID;
        this.address = address;
        this.numberOfRooms = numberOfRooms;
        this.price = price;
        this.area = area;
        this.imageUrl = imageUrl;
        this.description = description;
        this.title = title;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(String numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
