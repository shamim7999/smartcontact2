package com.spring.boot.smartcontact.controller;


import com.spring.boot.smartcontact.Service.AdminProductService;
import com.spring.boot.smartcontact.Service.ContactService;
import com.spring.boot.smartcontact.Service.UserService;
import com.spring.boot.smartcontact.model.AdminProduct;
import com.spring.boot.smartcontact.model.Contact;
import com.spring.boot.smartcontact.model.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalTime;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminProductService adminProductService;
    private final UserService userService;
    private final ContactService contactService;

    public AdminController(AdminProductService adminProductService,
                           UserService userService,
                           ContactService contactService) {
        this.adminProductService = adminProductService;
        this.userService = userService;
        this.contactService = contactService;
    }

    @ModelAttribute
    public void addCommonAttribute(Model model, Principal principal) {
        User user = this.userService.getUserByUserName(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("showSidebar", true);
    }
    @GetMapping("/index")
    public String adminHome(Model model) {
        model.addAttribute("title", "Admin Dashboard");
        model.addAttribute("showSidebar", true);
        return "admin/user_dashboard";
    }

    // Show Contacts Handler
    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable("page") Integer currentPage, Model model) {

        Page <Contact> contactList = this.contactService.findAll(currentPage);
        System.out.println(contactList);
        model.addAttribute("title", "All Contacts");
        model.addAttribute("contactList", contactList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", contactList.getTotalPages());
        model.addAttribute("userRole", "ADMIN");
        model.addAttribute("showSidebar", true);
        return "admin/show_contacts";
    }

    @GetMapping("/add-product")
    public String addProduct(Model model) {
        model.addAttribute("showSidebar", false);
        return "admin/add_product_form";
    }

    @PostMapping("/process-product")
    public String processProduct(@ModelAttribute("adminProduct") AdminProduct adminProduct,
                                 Model model) {
        String productName = adminProduct.getAdminProductName() + "-" + LocalTime.now();
        adminProduct.setAdminProductName(productName);
        this.adminProductService.save(adminProduct);
        model.addAttribute("showSidebar", false);
        return "admin/add_product_form";
    }

    @GetMapping("/show-products")
    public String showProducts() {
        System.out.println(this.adminProductService.findAll());
        return "redirect:/index";
    }
}
