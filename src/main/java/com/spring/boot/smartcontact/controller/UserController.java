package com.spring.boot.smartcontact.controller;

import com.spring.boot.smartcontact.Service.AdminProductService;
import com.spring.boot.smartcontact.Service.ContactService;
import com.spring.boot.smartcontact.Service.ProductService;
import com.spring.boot.smartcontact.Service.UserService;
import com.spring.boot.smartcontact.model.AdminProduct;
import com.spring.boot.smartcontact.model.Contact;
import com.spring.boot.smartcontact.model.Product;
import com.spring.boot.smartcontact.model.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.security.Principal;
import java.util.List;

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

    @ModelAttribute
    public void addCommonAttribute(Model model, Principal principal) {
        User user = this.userService.getUserByUserName(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("showSidebar", true);
    }

    @GetMapping("/index")
    public String dashBoard(Model model) {
        model.addAttribute("title", "User Dashboard");
        List<AdminProduct> adminProductList = this.adminProductService.findAllByAdminStatusProductSetToZero();
        model.addAttribute("adminProductList", adminProductList);
        return "user_dashboard";
    }

    @GetMapping("/add-contact")
    public String addContact(Model model) {
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
        userService.loadImageAndSave(contact, file, principal.getName());

        return "normal/add_contact_form";
    }

    // Show Contacts Handler
    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable("page") Integer currentPage, Model model, Principal principal) {
        User user = (User) model.getAttribute("user");

        Page <Contact> contactList = this.contactService.getContactsByUserId(user.getId(), currentPage);
        System.out.println(contactList);
        model.addAttribute("title", "All Contacts");
        model.addAttribute("contactList", contactList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", contactList.getTotalPages());
        model.addAttribute("userRole", "USER");
        return "normal/show_contacts";
    }

    @PostMapping("/update-contact/{id}")
    public String updateContact(@PathVariable("id") Integer contactId, Model model) {
        model.addAttribute("contact", this.contactService.findById(contactId));
        return "normal/update_contact";
    }

    @PostMapping("/process-update-contact")
    public String processUpdateContact(@ModelAttribute("contact") Contact contact,
                                       @RequestParam("profileImage") MultipartFile file,
                                       Principal principal,
                                       Model model) {

        User user = (User) model.getAttribute("user");
        contact.setUser(user);
        if(!file.isEmpty())
            contact.setImage(file.getOriginalFilename());
        System.out.println(contact);
        this.contactService.save(contact);

        return "redirect:/user/show-contacts/0";
    }

    @PostMapping("/delete-contact/{id}")
    public String deleteContact(@PathVariable("id") Integer contactId, Model model) {
        Contact contact = this.contactService.findById(contactId);
        User user = (User) model.getAttribute("user");
        user.getContacts().remove(contact);
        this.userService.save(user);
        return "redirect:/user/show-contacts/0";
    }


    ///////////////////////////// Product ////////////////////////



    @GetMapping("/show-products/{page}")
    public String showProducts(@PathVariable("page") Integer currentPage, Model model) {



        User user = (User) model.getAttribute("user");
        //List<Product> productList = this.productRepository.findAll();
        Page<Product> productList = this.productService.getProductsByUserId(user.getId(), currentPage);

        model.addAttribute("title", "All Products");
        model.addAttribute("productList", productList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", productList.getTotalPages());
        return "normal/show_products";
    }

    @PostMapping("/add-product")
    public String addProduct(@RequestParam("dropDownList") int productId, Model model) {
        User user = (User) model.getAttribute("user");

        Product product = new Product();
        AdminProduct adminProduct = this.adminProductService.findById(productId);

        product.setProductName(adminProduct.getAdminProductName());
        product.setProductId(productId);
        product.setUser(user);

        this.productService.save(product);

        adminProduct.setAdminProductStatus(1);
        this.adminProductService.save(adminProduct);

        return "redirect:/user/index";
    }

    @GetMapping("/select-products")
    public String selectProducts(Model model) {
        List<AdminProduct> adminProductList = this.adminProductService.findAllByAdminStatusProductSetToZero();
        model.addAttribute("adminProductList", adminProductList);
        return "normal/select_products";
    }
    @PostMapping("/add-multiple-products")
    public String addMultipleProduct(@RequestParam("dropDownList") List<Integer> itemsIds, Model model) {
        //List<Product> productList = new ArrayList<>();
        Product product = new Product();

        User user = (User) model.getAttribute("user");

        for(Integer id : itemsIds) {
            AdminProduct adminProduct = this.adminProductService.findById(id);
            product.setProductId(adminProduct.getAdminProductId());
            product.setProductName(adminProduct.getAdminProductName());
            product.setUser(user);

            //productList.add(product);
            this.productService.save(product);

            adminProduct.setAdminProductStatus(1);
            this.adminProductService.save(adminProduct);
        }

        return "redirect:/user/index";
    }
    /////////////////////////// Profile Details ///////////////////////

    @GetMapping("/profile")
    public String profileDetails() {
        return "profile_details";
    }

    @GetMapping("/contact/{cId}")
    public String contactDetails(@PathVariable("cId") int cId,
                                 Model model) {

        model.addAttribute("contact", this.contactService.findById(cId));
        System.out.println(this.contactService.findById(cId));
        return "show_contact_details";
    }
}