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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.ArrayList;
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

        List<AdminProduct> adminProductList = this.adminProductRepository.findAllByAdminStatusProductSetToZero();
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
                this.userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "normal/add_contact_form";
    }

    // Show Contacts Handler
    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {
        User user = this.userRepository.getUserByUserName(principal.getName());

        Pageable pageable = PageRequest.of(page, 2);

        Page <Contact> contactList = this.contactRepository.getContactsByUserId(user.getId(), pageable);
        System.out.println(contactList);
        model.addAttribute("title", "All Contacts");
        model.addAttribute("contactList", contactList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contactList.getTotalPages());
        model.addAttribute("userRole", "USER");
        return "normal/show_contacts";
    }

    @PostMapping("/update-contact/{id}")
    public String updateContact(@PathVariable("id") Integer contactId, Model model) {
        model.addAttribute("contact", (Contact) this.contactRepository.findById(contactId).get());
        return "normal/update_contact";
    }

    @PostMapping("/process-update-contact")
    public String processUpdateContact(@ModelAttribute("contact") Contact contact,
                                       @RequestParam("profileImage") MultipartFile file,
                                       Principal principal) {

        User user = this.userRepository.getUserByUserName(principal.getName());
        contact.setUser(user);
        if(!file.isEmpty())
            contact.setImage(file.getOriginalFilename());
        System.out.println(contact);
        this.contactRepository.save(contact);

        return "redirect:/user/show-contacts/0";
    }

    @PostMapping("/delete-contact/{id}")
    public String deleteContact(@PathVariable("id") Integer contactId, Principal principal) {
        //model.addAttribute("contact", (Contact) this.contactRepository.findById(contactId).get());
        Contact contact = this.contactRepository.findById(contactId).get();
        User user = this.userRepository.getUserByUserName(principal.getName());
        user.getContacts().remove(contact);
        this.userRepository.save(user);
        return "redirect:/user/show-contacts/0";
    }


    ///////////////////////////// Product ////////////////////////



    @GetMapping("/show-products/{page}")
    public String showProducts(@PathVariable("page") Integer page, Model model,
                               Principal principal) {

        Pageable pageable = PageRequest.of(page, 2);

        User user = this.userRepository.getUserByUserName(principal.getName());
        //List<Product> productList = this.productRepository.findAll();
        Page<Product> productList = this.productRepository.getProductsByUserId(user.getId(), pageable);

        model.addAttribute("title", "All Products");
        model.addAttribute("productList", productList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productList.getTotalPages());
        return "normal/show_products";
    }

    @PostMapping("/add-product")
    public String addProduct(@RequestParam("dropDownList") Integer item, Principal principal) {
        Product product = new Product();
        AdminProduct adminProduct = this.adminProductRepository.findById(item).get();

        product.setProductName(adminProduct.getAdminProductName());
        product.setProductId(item);
        product.setUser(this.userRepository.getUserByUserName(principal.getName()));
        this.productRepository.save(product);

        adminProduct.setAdminProductStatus(1);
        this.adminProductRepository.save(adminProduct);

        return "redirect:/user/index";
    }

    @GetMapping("/select-products")
    public String selectProducts(Model model) {
        List<AdminProduct> adminProductList = this.adminProductRepository.findAllByAdminStatusProductSetToZero();
        model.addAttribute("adminProductList", adminProductList);
        return "normal/select_products";
    }
    @PostMapping("/add-multiple-products")
    public String addMultipleProduct(@RequestParam("dropDownList") List<Integer> items, Principal principal) {
        //List<Product> productList = new ArrayList<>();
        Product product = new Product();

        User user = this.userRepository.getUserByUserName(principal.getName());

        for(Integer id : items) {
            AdminProduct adminProduct = this.adminProductRepository.findById(id).get();
            product.setProductId(adminProduct.getAdminProductId());
            product.setProductName(adminProduct.getAdminProductName());
            product.setUser(user);

            //productList.add(product);
            this.productRepository.save(product);

            adminProduct.setAdminProductStatus(1);
            this.adminProductRepository.save(adminProduct);
        }

        return "redirect:/user/index";
    }

}


/*

    //process-contact
    if(file.isEmpty())  {
                //model.addAttribute("message",  "Something Wrong!!");
                //model.addAttribute("type", "danger");
                contact.setImage("profile.jpg");
                //throw new Exception();

            } else {
                contact.setImage(file.getOriginalFilename());
                File saveFile = new ClassPathResource("static/image").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }


 */