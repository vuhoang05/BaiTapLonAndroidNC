package com.example.baitaplon.Domain;

public class Favorite {
    private String favoriteID;

    private String title;
    private String description;
    private String imageUrl;
    private String area;
    private String price;
    private String numberOfRooms;
    private String postID;
    private String address;
    private String userID;
    private String sdt;
    private String houseType;
    private boolean inWishlist; // Thêm thuộc tính inWishlist
    private boolean inListLoved; // Thêm thuộc tính inListLoved
    public Favorite() {
        // Constructor không tham số cần thiết cho Firebase
    }
    public Favorite(String favoriteID, String title, String description, String imageUrl, String area, String price, String numberOfRooms, String postID, String address, String userID, String sdt, String houseType, boolean inWishlist, boolean inListLoved) {
        this.favoriteID = favoriteID;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.area = area;
        this.price = price;
        this.numberOfRooms = numberOfRooms;
        this.postID = postID;
        this.address = address;
        this.userID = userID;
        this.sdt = sdt;
        this.houseType = houseType;
        this.inWishlist = inWishlist;
        this.inListLoved = inListLoved;
    }

    public String getFavoriteID() {
        return favoriteID;
    }

    public void setFavoriteID(String favoriteID) {
        this.favoriteID = favoriteID;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(String numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public boolean isInWishlist() {
        return inWishlist;
    }

    public void setInWishlist(boolean inWishlist) {
        this.inWishlist = inWishlist;
    }

    public boolean isInListLoved() {
        return inListLoved;
    }

    public void setInListLoved(boolean inListLoved) {
        this.inListLoved = inListLoved;
    }


}
