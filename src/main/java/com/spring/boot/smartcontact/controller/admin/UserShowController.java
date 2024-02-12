package com.spring.boot.smartcontact.controller.admin;

import com.spring.boot.smartcontact.model.User;
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
@RequestMapping("/admin")
public class UserShowController {
    private final UserService userService;

    public UserShowController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void addCommonAttribute(Model model, Principal principal) {
        User user = userService.getUserByUserName(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("showSidebar", true);
    }

    @GetMapping("/show-users")
    public String showContacts(@RequestParam("page") Optional<Integer> page, Model model) {
        int currentPage = page.orElse(1);
        Page<User> userList = userService.findAllUser(currentPage-1);

        model.addAttribute("title",  "All Contacts");
        model.addAttribute("userList", userList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", userList.getTotalPages());


        return "admin/show_users";
    }
}
