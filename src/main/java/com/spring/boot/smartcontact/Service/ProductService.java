package com.spring.boot.smartcontact.Service;

import com.spring.boot.smartcontact.Repository.ProductRepository;
import com.spring.boot.smartcontact.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> getProductsByUserId(int userId, int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 2);
        return productRepository.getProductsByUserId(userId, pageable);
    }

    public void save(Product product) {
        productRepository.save(product);
    }
}
