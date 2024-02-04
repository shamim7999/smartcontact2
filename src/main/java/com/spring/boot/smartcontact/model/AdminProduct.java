package com.spring.boot.smartcontact.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
