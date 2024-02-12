package com.spring.boot.smartcontact.controller.admin;


import com.spring.boot.smartcontact.service.AdminProductService;
import com.spring.boot.smartcontact.service.UserService;
import com.spring.boot.smartcontact.model.AdminProduct;
import com.spring.boot.smartcontact.model.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalTime;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminProductService adminProductService;
    private final UserService userService;

    public AdminController(AdminProductService adminProductService,
                           UserService userService) {
        this.adminProductService = adminProductService;
        this.userService = userService;
    }

    @ModelAttribute
    public void addCommonAttribute(Model model, Principal principal) {
        User user = userService.getUserByUserName(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("showSidebar", true);
    }
    @GetMapping("/index")
    public String adminHome(Model model) {
        model.addAttribute("title", "Admin Dashboard");
        return "user_dashboard";
    }

    // Show Contacts Handler
    @GetMapping("/show-users")
    public String showContacts(@RequestParam("page") Optional <Integer> page, Model model) {
        int currentPage = page.orElse(1);
        Page <User> userList = userService.findAllUser(currentPage-1);

        model.addAttribute("title",  "All Contacts");
        model.addAttribute("userList", userList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", userList.getTotalPages());


        return "admin/show_users";
    }


    @PostMapping("/suspend-user/{id}")
    public String suspendUser(@PathVariable int id) {
        userService.suspendUserById(id);
        return "redirect:/admin/show-users";
    }

    @PostMapping("/enable-user/{id}")
    public String enableUser(@PathVariable int id) {
        userService.enableUserById(id);
        return "redirect:/admin/show-users";
    }
    @GetMapping("/add-product")
    public String addProduct(Model model) {
        model.addAttribute("showSidebar", false);
        return "admin/add_product_form";
    }

    @PostMapping("/process-product")
    public String processProduct(@ModelAttribute AdminProduct adminProduct,
                                 Model model) {
        String productName = adminProduct.getAdminProductName() + "-" + LocalTime.now();
        adminProduct.setAdminProductName(productName);
        adminProductService.save(adminProduct);

        return "redirect:/admin/add-product";
    }

    @GetMapping("/show-products")
    public String showProducts() {
        System.out.println(adminProductService.findAll());
        return "redirect:/index";
    }

    /////////////////////////// Profile Details ///////////////////////

    @GetMapping("/profile")
    public String profileDetails() {
        return "profile_details";
    }
}