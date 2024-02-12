package com.spring.boot.smartcontact.controller.user;

import com.spring.boot.smartcontact.model.Contact;
import com.spring.boot.smartcontact.model.User;
import com.spring.boot.smartcontact.service.ContactService;
import com.spring.boot.smartcontact.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class ContactShowController {

    private final UserService userService;
    private final ContactService contactService;

    public ContactShowController(UserService userService, ContactService contactService) {
        this.userService = userService;
        this.contactService = contactService;
    }

    @ModelAttribute
    public User addCommonAttribute(Principal principal) {
        return userService.getUserByUserName(principal.getName());
    }
    @GetMapping("/show-contacts")
    public String showContactsByPage(@RequestParam("page") Optional<Integer> page, Model model, Principal principal) {
        User user = (User) model.getAttribute("user");
        if(!user.isEnabled())
            return "redirect:/logout";

        int currentPage = page.orElse(1);
        Page<Contact> contactList = contactService.getContactsByUserId(user.getId(), currentPage-1);
        System.out.println(contactList);

        model.addAttribute("title", "All Contacts");
        model.addAttribute("contactList", contactList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", contactList.getTotalPages());
        model.addAttribute("showSidebar", true);
        return "user/show_contacts";
    }

    @GetMapping("/contact/{cId}")
    public String contactDetails(@PathVariable int cId,
                                 Model model) {
        User user = (User) model.getAttribute("user");
        if(!user.isEnabled())
            return "redirect:/logout";

        model.addAttribute("contact", contactService.findById(cId));
        model.addAttribute("showSidebar", true);
        System.out.println(contactService.findById(cId));
        return "show_contact_details";
    }
}
