package com.spring.boot.smartcontact.dao;

import com.spring.boot.smartcontact.model.AdminProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminProductRepository extends JpaRepository<AdminProduct, Integer> {

    @Query(value = "Select * from admin_product where admin_product_status = 0", nativeQuery = true)
    List<AdminProduct> findAllByAdminStatusProductSetToZero();
}
