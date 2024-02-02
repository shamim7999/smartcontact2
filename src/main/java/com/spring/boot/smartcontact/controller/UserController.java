package com.spring.boot.smartcontact.controller;

import com.spring.boot.smartcontact.dao.AdminProductRepository;
import com.spring.boot.smartcontact.dao.ContactRepository;
import com.spring.boot.smartcontact.dao.ProductRepository;
import com.spring.boot.smartcontact.dao.UserRepository;
import com.spring.boot.smartcontact.helper.Message;
import com.spring.boot.smartcontact.model.AdminProduct;
import com.spring.boot.smartcontact.model.Contact;
import com.spring.boot.smartcontact.model.Product;
import com.spring.boot.smartcontact.model.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AdminProductRepository adminProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @ModelAttribute
    public void addCommonAttribute(Model model, Principal principal) {
        User user = this.userRepository.getUserByUserName(principal.getName());
        model.addAttribute("user", user);


    }

    @GetMapping("/index")
    public String dashBoard(Model model, Principal principal) {
        model.addAttribute("title", "User Dashboard");

        List<AdminProduct> adminProductList = this.adminProductRepository.findAll();
        model.addAttribute("adminProductList", adminProductList);

        //model.addAttribute("message", "")
        return "normal/user_dashboard";
    }

    @GetMapping("/add-contact")
    public String dashBoard(Model model) {
        model.addAttribute("contact", new Contact());
        model.addAttribute("title", "Add Contact");
        return "normal/add_contact_form";
    }

    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute("contact") Contact contact,
                                 @RequestParam("profileImage") MultipartFile file,
                                 Principal principal, Model model) {
            model.addAttribute("message",  "Successfully added!!");
            model.addAttribute("type", "success");
            try {
            System.out.println(contact);
            User user = this.userRepository.getUserByUserName(principal.getName());

            // Processing and Uploading file
            if(file.isEmpty())  {
                model.addAttribute("message",  "Something Wrong!!");
                model.addAttribute("type", "danger");
                throw new Exception();
            } else {
                contact.setImage(file.getOriginalFilename());
                File saveFile = new ClassPathResource("static/image").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }

            contact.setUser(user);
            user.getContacts().add(contact);
            this.userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "normal/add_contact_form";
    }

    // Show Contacts Handler
    @GetMapping("/show-contacts")
    public String showContacts(Model model, Principal principal) {
        User user = this.userRepository.getUserByUserName(principal.getName());
        List<Contact> contactList = this.contactRepository.getContactsByUserId(user.getId());
        System.out.println(contactList);
        model.addAttribute("title", "All Contacts");
        model.addAttribute("contactList", contactList);
        return "normal/show_contacts";
    }

    @PostMapping("/update-contact/{id}")
    public String updateContact(@PathVariable("id") Integer contactId, Model model) {
        model.addAttribute("contact", (Contact) this.contactRepository.findById(contactId).get());
        return "normal/update_contact";
    }

    @PostMapping("/process-update-contact")
    public String processUpdateContact(@ModelAttribute("contact") Contact contact, Principal principal) {
        System.out.println(contact);
        contact.setUser(this.userRepository.getUserByUserName(principal.getName()));
        this.contactRepository.save(contact);

        return "redirect:/user/show-contacts";
    }

    @PostMapping("/delete-contact/{id}")
    public String deleteContact(@PathVariable("id") Integer contactId, Principal principal) {
        //model.addAttribute("contact", (Contact) this.contactRepository.findById(contactId).get());
        Contact contact = this.contactRepository.findById(contactId).get();
        User user = this.userRepository.getUserByUserName(principal.getName());
        user.getContacts().remove(contact);
        this.userRepository.save(user);
        return "redirect:/user/show-contacts";
    }


    ///////////////////////////// Product ////////////////////////

    @GetMapping("/show-products")
    public String showProducts(Model model) {
        List<Product> productList = this.productRepository.findAll();
        model.addAttribute("productList", productList);
        return "normal/show_products";
    }

    @PostMapping("/add-product")
    public String addProduct(@RequestParam("dropDownList") String item, Principal principal) {
        Product product = new Product();
        product.setProductName(item);
        product.setUser(this.userRepository.getUserByUserName(principal.getName()));
        this.productRepository.save(product);
        return "redirect:/user/index";
    }


}
