package com.spring.boot.smartcontact.service;

import com.spring.boot.smartcontact.repository.AdminProductRepository;
import com.spring.boot.smartcontact.repository.ProductRepository;
import com.spring.boot.smartcontact.repository.UserRepository;
import com.spring.boot.smartcontact.model.AdminProduct;
import com.spring.boot.smartcontact.model.Contact;
import com.spring.boot.smartcontact.model.Product;
import com.spring.boot.smartcontact.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final AdminProductRepository adminProductRepository;

    private final ProductRepository productRepository;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                       AdminProductRepository adminProductRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.adminProductRepository = adminProductRepository;
        this.productRepository = productRepository;
    }

    public User getUserByUserName(String userEmail) {
        return userRepository.getUserByUserName(userEmail);
    }

    public Page<User> findAllUser(Integer currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 2);
        return userRepository.findAllUser(pageable);
    }

    public void save(User user) {

        user.setEnabled(true);
        user.setRole("ROLE_USER");
        if(user.getImageUrl().isEmpty())
            user.setImageUrl("profile.jpg");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        logger.info("User: {}", user);


        userRepository.save(user);
    }

    public void saveUserProduct(User user, List<Integer> itemsIds) {
        Product product = new Product();

        for(Integer id : itemsIds) {
            AdminProduct adminProduct = adminProductRepository.findById(id).get();
            product.setProductId(adminProduct.getId());
            product.setProductName(adminProduct.getProductName());
            product.setUser(user);

            productRepository.save(product);

            adminProduct.setProductStatus(1);
            adminProductRepository.save(adminProduct);
        }
    }

    public User findUserById(int id) {
        return userRepository.findById(id).get();
    }
    public void enableUserById(int id) {
        User user = findUserById(id);
        user.setEnabled(true);
        userRepository.save(user);
    }
    public void suspendUserById(int id) {
        User user = findUserById(id);
        user.setEnabled(false);
        userRepository.save(user);
        logger.info("USER: {}", user);
    }
    public String encode(String userPassword) {
        return bCryptPasswordEncoder.encode(userPassword);
    }

    public void loadImageAndSave(Contact contact, MultipartFile file, String userEmail) {
        try {
            logger.info("CONTACT: {}", contact);
            User user = userRepository.getUserByUserName(userEmail);

            String originalFileName = "profile.jpg";
            if(!file.isEmpty())
                originalFileName = file.getOriginalFilename().toString();
            // Processing and Uploading file

            contact.setImage(originalFileName);
            File saveFile = new ClassPathResource("static/image").getFile();
            Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+originalFileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);


            contact.setUser(user);
            user.getContacts().add(contact);

            userRepository.save(user);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
