package com.spring.boot.smartcontact.dao;

import com.spring.boot.smartcontact.model.Contact;
import com.spring.boot.smartcontact.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    public Page<Product> getProductsByUserId(int userId, Pageable pageable);
}
