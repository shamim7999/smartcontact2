package com.spring.boot.smartcontact.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
public class AdminProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_product_id")
    private int adminProductId;

    @Column(name = "admin_product_name")
    private String adminProductName;

    @Column(name = "admin_product_status")
    private int adminProductStatus;

    public AdminProduct(int adminProductId, String adminProductName, int adminProductStatus) {
        this.adminProductId = adminProductId;
        this.adminProductName = adminProductName;
        this.adminProductStatus = adminProductStatus;
    }

    public AdminProduct() {
    }

    public int getAdminProductId() {
        return adminProductId;
    }

    public void setAdminProductId(int adminProductId) {
        this.adminProductId = adminProductId;
    }

    public String getAdminProductName() {
        return adminProductName;
    }

    public void setAdminProductName(String adminProductName) {
        this.adminProductName = adminProductName;
    }

    public int getAdminProductStatus() {
        return adminProductStatus;
    }

    public void setAdminProductStatus(int adminProductStatus) {
        this.adminProductStatus = adminProductStatus;
    }

    @Override
    public String toString() {
        return "AdminProduct{" +
                "adminProductId=" + adminProductId +
                ", adminProductName='" + adminProductName + '\'' +
                ", adminProductStatus=" + adminProductStatus +
                '}';
    }
}
