package com.spring.boot.smartcontact.Service;

import com.spring.boot.smartcontact.Repository.UserRepository;
import com.spring.boot.smartcontact.model.Contact;
import com.spring.boot.smartcontact.model.User;
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
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User getUserByUserName(String userEmail) {
        return this.userRepository.getUserByUserName(userEmail);
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
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));

        System.out.println("User: "+user);


        this.userRepository.save(user);
    }

    public User findUserById(int id) {
        return this.userRepository.findById(id).get();
    }
    public void enableUserById(int id) {
        User user = findUserById(id);
        user.setEnabled(true);
        this.userRepository.save(user);
    }
    public void suspendUserById(int id) {
        User user = findUserById(id);
        user.setEnabled(false);
        this.userRepository.save(user);
        System.out.println("USER: "+user);
    }
    public String encode(String userPassword) {
        return this.bCryptPasswordEncoder.encode(userPassword);
    }

    public void loadImageAndSave(Contact contact, MultipartFile file, String userEmail) {
        try {
            System.out.println(contact);
            User user = this.userRepository.getUserByUserName(userEmail);

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
            //this.userService.save(user);

            userRepository.save(user);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
