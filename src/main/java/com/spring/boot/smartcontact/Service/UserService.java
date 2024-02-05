package com.spring.boot.smartcontact.Service;

import com.spring.boot.smartcontact.Repository.AdminProductRepository;
import com.spring.boot.smartcontact.Repository.ContactRepository;
import com.spring.boot.smartcontact.Repository.ProductRepository;
import com.spring.boot.smartcontact.Repository.UserRepository;
import com.spring.boot.smartcontact.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ContactRepository contactRepository;
    private final AdminProductRepository adminProductRepository;
    private final ProductRepository productRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository,
                       ContactRepository contactRepository,
                       AdminProductRepository adminProductRepository,
                       ProductRepository productRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
        this.adminProductRepository = adminProductRepository;
        this.productRepository = productRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User getUserByUserName(String userEmail) {
        return this.userRepository.getUserByUserName(userEmail);
    }

    public void save(User user) {

        user.setEnabled(true);
        user.setRole("ROLE_USER");
        user.setImageUrl("default.png");
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));

        System.out.println("User: "+user);


        this.userRepository.save(user);
    }

    public String encode(String userPassword) {
        return this.bCryptPasswordEncoder.encode(userPassword);
    }
}
