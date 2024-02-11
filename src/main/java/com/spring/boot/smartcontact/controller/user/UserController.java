package com.spring.boot.smartcontact.controller.user;

import com.spring.boot.smartcontact.service.AdminProductService;
import com.spring.boot.smartcontact.service.ContactService;
import com.spring.boot.smartcontact.service.ProductService;
import com.spring.boot.smartcontact.service.UserService;
import com.spring.boot.smartcontact.model.AdminProduct;
import com.spring.boot.smartcontact.model.Contact;
import com.spring.boot.smartcontact.model.Product;
import com.spring.boot.smartcontact.model.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final ContactService contactService;
    private final AdminProductService adminProductService;
    private final ProductService productService;

    public UserController(UserService userService,
                          ContactService contactService,
                          AdminProductService adminProductService,
                          ProductService productService) {
        this.userService = userService;
        this.contactService = contactService;
        this.adminProductService = adminProductService;
        this.productService = productService;
    }

//    @ModelAttribute
//    public String addCommonAttribute(Model model, Principal principal) {
//        User user = userService.getUserByUserName(principal.getName());
//        if(!user.isEnabled())
//            return "redirect:/logout";
//        model.addAttribute("user", user);
//        model.addAttribute("showSidebar", true);
//        return null;
//    }
    @ModelAttribute
    public User addCommonAttribute(Principal principal) {
        return userService.getUserByUserName(principal.getName());
    }
    @GetMapping("/index")
    public String dashBoard(Model model) {

        User user = (User) model.getAttribute("user");
        if(!user.isEnabled())
            return "redirect:/logout";

        model.addAttribute("title", "User Dashboard");
        List<AdminProduct> adminProductList = adminProductService.findAllByAdminStatusProductSetToZero();
        model.addAttribute("adminProductList", adminProductList);
        model.addAttribute("showSidebar", true);
        return "user_dashboard";
    }


    ///////////////////////////// Product ////////////////////////



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
        return "normal/show_products";
    }

    @PostMapping("/add-product")
    public String addProduct(@RequestParam("dropDownList") int productId, Model model) {
        User user = (User) model.getAttribute("user");
        if(!user.isEnabled())
            return "redirect:/logout";

        model.addAttribute("showSidebar", true);

        Product product = new Product();
        AdminProduct adminProduct = adminProductService.findById(productId);

        product.setProductName(adminProduct.getAdminProductName());
        product.setProductId(productId);
        product.setUser(user);

        productService.save(product);

        adminProduct.setAdminProductStatus(1);
        adminProductService.save(adminProduct);

        return "redirect:/user/index";
    }

    @GetMapping("/select-products")
    public String selectProducts(Model model) {
        List<AdminProduct> adminProductList = adminProductService.findAllByAdminStatusProductSetToZero();
        User user = (User) model.getAttribute("user");
        if(!user.isEnabled())
            return "redirect:/logout";

        model.addAttribute("adminProductList", adminProductList);
        model.addAttribute("showSidebar", true);
        return "normal/select_products";
    }
    @PostMapping("/add-multiple-products")
    public String addMultipleProduct(@RequestParam("dropDownList") List<Integer> itemsIds, Model model) {
        User user = (User) model.getAttribute("user");
        if(!user.isEnabled())
            return "redirect:/logout";

        model.addAttribute("showSidebar", true);
        userService.saveUserProduct(user, itemsIds);
        return "redirect:/user/index";
    }
    /////////////////////////// Profile Details ///////////////////////

    @GetMapping("/profile")
    public String profileDetails(Model model) {
        User user = (User) model.getAttribute("user");
        if(!user.isEnabled())
            return "redirect:/logout";

        model.addAttribute("showSidebar", true);
        return "profile_details";
    }
}