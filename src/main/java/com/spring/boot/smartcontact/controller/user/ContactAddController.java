package com.spring.boot.smartcontact.controller.user;

import com.spring.boot.smartcontact.helper.Message;
import com.spring.boot.smartcontact.model.Contact;
import com.spring.boot.smartcontact.model.User;
import com.spring.boot.smartcontact.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class ContactAddController {

    private final UserService userService;

    public ContactAddController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public User addCommonAttribute(Principal principal) {
        return userService.getUserByUserName(principal.getName());
    }
    @ModelAttribute
    public Principal sendPrincipal(Principal principal) {
        return principal;
    }
    @GetMapping("/add-contact")
    public String addContact(Model model) {
        User user = (User) model.getAttribute("user");
        if(!user.isEnabled())
            return "redirect:/logout";

        model.addAttribute("contact", new Contact());
        model.addAttribute("title", "Add Contact");
        model.addAttribute("showSidebar", true);

        return "user/add_contact_form";
    }

    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute Contact contact,
                                 @RequestParam("profileImage") MultipartFile file,
                                 Principal principal, RedirectAttributes redirectAttributes, Model model) {
        User user = (User) model.getAttribute("user");
        if(!user.isEnabled())
            return "redirect:/logout";

        redirectAttributes.addFlashAttribute("message",  new Message("Successfully added!!", "alert-success"));
        userService.loadImageAndSave(contact, file, principal.getName());

        return "redirect:/user/add-contact";
    }
}
