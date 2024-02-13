package com.spring.boot.smartcontact.controller.user;

import com.spring.boot.smartcontact.model.Contact;
import com.spring.boot.smartcontact.model.User;
import com.spring.boot.smartcontact.service.ContactService;
import com.spring.boot.smartcontact.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class ContactManipulateController {
    private final UserService userService;
    private final ContactService contactService;

    public ContactManipulateController(UserService userService, ContactService contactService) {
        this.userService = userService;
        this.contactService = contactService;
    }

    @ModelAttribute
    public User addCommonAttribute(Principal principal) {
        return userService.getUserByUserName(principal.getName());
    }
    @ModelAttribute
    public Principal sendPrincipal(Principal principal) {
        return principal;
    }
    @PostMapping("/update-contact/{contactId}")
    public String updateContact(@PathVariable Integer contactId, Model model) {
        User user = (User) model.getAttribute("user");
        if(!user.isEnabled())
            return "redirect:/logout";

        model.addAttribute("contact", contactService.findById(contactId));
        model.addAttribute("showSidebar", true);
        return "user/update_contact";
    }

    @PostMapping("/process-update-contact")
    public String processUpdateContact(@ModelAttribute Contact contact,
                                       @RequestParam("profileImage") MultipartFile file,
                                       Principal principal,
                                       Model model) {

        User user = (User) model.getAttribute("user");
        if(!user.isEnabled())
            return "redirect:/logout";

        model.addAttribute("showSidebar", true);
        contact.setUser(user);
        if(!file.isEmpty())
            contact.setImage(file.getOriginalFilename());
        System.out.println(contact);
        contactService.save(contact);

        return "redirect:/user/show-contacts";
    }

    @PostMapping("/delete-contact/{contactId}")
    public String deleteContact(@PathVariable Integer contactId, Model model) {
        Contact contact = contactService.findById(contactId);
        User user = (User) model.getAttribute("user");
        if(!user.isEnabled())
            return "redirect:/logout";

        model.addAttribute("showSidebar", true);
        user.getContacts().remove(contact);
        userService.save(user);
        return "redirect:/user/show-contacts";
    }

}
