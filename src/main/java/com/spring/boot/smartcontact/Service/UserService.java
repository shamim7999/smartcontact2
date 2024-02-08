package com.spring.boot.smartcontact.Service;

import com.spring.boot.smartcontact.Repository.UserRepository;
import com.spring.boot.smartcontact.model.Contact;
import com.spring.boot.smartcontact.model.User;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

    public void save(User user) {

        user.setEnabled(true);
        user.setRole("ROLE_ADMIN");
        user.setImageUrl("profile.jpg");
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));

        System.out.println("User: "+user);


        this.userRepository.save(user);
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
