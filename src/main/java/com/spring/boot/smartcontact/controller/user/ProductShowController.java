package com.spring.boot.smartcontact.controller.user;

import com.spring.boot.smartcontact.model.Product;
import com.spring.boot.smartcontact.model.User;
import com.spring.boot.smartcontact.service.ProductService;
import com.spring.boot.smartcontact.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class ProductShowController {
    private final UserService userService;
    private final ProductService productService;

    public ProductShowController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @ModelAttribute
    public User addCommonAttribute(Principal principal) {
        return userService.getUserByUserName(principal.getName());
    }


    @GetMapping("/show-products")
    public String showProducts(@RequestParam("page") Optional<Integer> page, Model model) {

        int currentPage = page.orElse(1);

        User user = (User) model.getAttribute("user");
        if(!user.isEnabled())
            return "redirect:/logout";

        Page<Product> productList = productService.getProductsByUserId(user.getId(), currentPage-1);

        model.addAttribute("title", "All Products");
        model.addAttribute("productList", productList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", productList.getTotalPages());
        model.addAttribute("showSidebar", true);
        return "user/show_products";
    }
}
