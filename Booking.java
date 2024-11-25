package com.example.baitaplon.Domain;

public class Booking {
    private String tenantName; // Tên người thuê
    private String ownerName;  // Tên chủ sở hữu
    private String selectedTime; // Thời gian đã chọn

    // Constructor mặc định (bắt buộc cho Firebase)
    public Booking() {
        // Không cần làm gì ở đây
    }

    // Constructor có tham số
    public Booking(String tenantName, String ownerName, String selectedTime) {
        this.tenantName = tenantName;
        this.ownerName = ownerName;
        this.selectedTime = selectedTime;
    }

    // Getters và Setters
    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
    }
}
