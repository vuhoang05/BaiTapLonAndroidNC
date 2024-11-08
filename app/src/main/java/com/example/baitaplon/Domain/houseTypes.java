package com.example.baitaplon.Domain;

public class houseTypes {

    private int houseTypeId;
    private String houseTypeName;

    public houseTypes() {
    }

    public houseTypes(int houseTypeId, String houseTypeName) {
        this.houseTypeId = houseTypeId;
        this.houseTypeName = houseTypeName;
    }

    public int getHouseTypeId() {
        return houseTypeId;
    }

    public void setHouseTypeId(int houseTypeId) {
        this.houseTypeId = houseTypeId;
    }

    public String getHouseTypeName() {
        return houseTypeName;
    }

    public void setHouseTypeName(String houseTypeName) {
        this.houseTypeName = houseTypeName;
    }
}
