package com.spring.boot.smartcontact.Repository;

import com.spring.boot.smartcontact.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    public Page<Product> getProductsByUserId(int userId, Pageable pageable);
}
