package com.spring.boot.smartcontact.dao;

import com.spring.boot.smartcontact.model.AdminProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminProductRepository extends JpaRepository<AdminProduct, Integer> {
}
