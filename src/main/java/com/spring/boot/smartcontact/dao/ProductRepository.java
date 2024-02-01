package com.spring.boot.smartcontact.dao;

import com.spring.boot.smartcontact.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
