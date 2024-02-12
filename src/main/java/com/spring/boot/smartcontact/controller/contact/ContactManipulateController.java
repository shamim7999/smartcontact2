package com.spring.boot.smartcontact.controller.contact;

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

    @PostMapping("/update-contact/{id}")
    public String updateContact(@PathVariable("id") Integer contactId, Model model) {
        User user = (User) model.getAttribute("user");
        if(!user.isEnabled())
            return "redirect:/logout";

        model.addAttribute("contact", contactService.findById(contactId));
        model.addAttribute("showSidebar", true);
        return "user/update_contact";
    }

    @PostMapping("/process-update-contact")
    public String processUpdateContact(@ModelAttribute("contact") Contact contact,
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

    @PostMapping("/delete-contact/{id}")
    public String deleteContact(@PathVariable("id") Integer contactId, Model model) {
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
