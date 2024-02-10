package com.spring.boot.smartcontact.Service;

import com.spring.boot.smartcontact.Repository.AdminProductRepository;
import com.spring.boot.smartcontact.Repository.ContactRepository;
import com.spring.boot.smartcontact.Repository.UserRepository;
import com.spring.boot.smartcontact.model.AdminProduct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminProductService {
    private final AdminProductRepository adminProductRepository;

    public AdminProductService(AdminProductRepository adminProductRepository) {
        this.adminProductRepository = adminProductRepository;
    }

    public void save(AdminProduct adminProduct) {
        adminProductRepository.save(adminProduct);
    }

    public List<AdminProduct> findAll() {
        return adminProductRepository.findAll();
    }

    public List<AdminProduct> findAllByAdminStatusProductSetToZero() {
        return adminProductRepository.findAllByAdminStatusProductSetToZero();
    }

    public AdminProduct findById(int productId) {
        return adminProductRepository.findById(productId).get();
    }
}
