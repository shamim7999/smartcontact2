package com.spring.boot.smartcontact.controller.admin;

import com.spring.boot.smartcontact.model.AdminProduct;
import com.spring.boot.smartcontact.model.User;
import com.spring.boot.smartcontact.service.AdminProductService;
import com.spring.boot.smartcontact.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalTime;

@Controller
@RequestMapping("/admin")
public class ProductAddController {

    private final UserService userService;
    private final AdminProductService adminProductService;

    public ProductAddController(UserService userService,
                                AdminProductService adminProductService) {
        this.userService = userService;
        this.adminProductService = adminProductService;
    }
    @ModelAttribute
    public Principal sendPrincipal(Principal principal) {
        return principal;
    }
    @ModelAttribute
    public void addCommonAttribute(Model model, Principal principal) {
        User user = userService.getUserByUserName(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("showSidebar", true);
    }

    @GetMapping("/add-product")
    public String addProduct(Model model) {
        model.addAttribute("showSidebar", false);
        return "admin/add_product_form";
    }

    @PostMapping("/process-product")
    public String processProduct(@Valid @ModelAttribute AdminProduct adminProduct,
                                 BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            System.out.println(bindingResult);
        }
        String productName = adminProduct.getProductName() + "-" + LocalTime.now();
        adminProduct.setProductName(productName);
        adminProductService.save(adminProduct);

        return "redirect:/admin/add-product";
    }
}
