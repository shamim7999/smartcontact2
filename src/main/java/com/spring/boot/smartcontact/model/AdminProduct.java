package com.spring.boot.smartcontact.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class AdminProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Size(min = 3, max = 40, message = "Minimum 3 characters.")
    private String productName;

    private int productStatus;

    public AdminProduct(int id, String productName, int productStatus) {
        this.id = id;
        this.productName = productName;
        this.productStatus = productStatus;
    }

    public AdminProduct() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(int productStatus) {
        this.productStatus = productStatus;
    }

   // @Override
//    public String toString() {
//        return "AdminProduct{" +
//                "id=" + id +
//                ", productName='" + productName + '\'' +
//                ", productStatus=" + productStatus +
//                '}';
//    }
}
