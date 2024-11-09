package com.example.baitaplon.Domain;

public class houseTypes {

    private String houseTypeId;
    private String houseTypeName;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public houseTypes() {
    }

    public houseTypes(String houseTypeId, String houseTypeName, String imageUrl) {
        this.houseTypeId = houseTypeId;
        this.houseTypeName = houseTypeName;
        this.imageUrl = imageUrl;
    }

    public String getHouseTypeId() {
        return houseTypeId;
    }

    public void setHouseTypeId(String houseTypeId) {
        this.houseTypeId = houseTypeId;
    }

    public String getHouseTypeName() {
        return houseTypeName;
    }

    public void setHouseTypeName(String houseTypeName) {
        this.houseTypeName = houseTypeName;
    }
}
