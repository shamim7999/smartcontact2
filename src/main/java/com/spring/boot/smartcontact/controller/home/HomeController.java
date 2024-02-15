package com.spring.boot.smartcontact.controller.home;

import com.spring.boot.smartcontact.helper.Message;
import com.spring.boot.smartcontact.service.UserService;
import com.spring.boot.smartcontact.model.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalTime;

@Controller
public class HomeController {
    private final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void addCommonAttribute(Model model, Principal principal) {
        model.addAttribute("showBottom", false);
        model.addAttribute("time", LocalTime.now());
        model.addAttribute("principal", principal);
    }
    @ModelAttribute
    public Principal sendPrincipal(Principal principal) {
        return principal;
    }
    @GetMapping({"/", "/home"})
    public String dispatch(Model model, Principal principal) {
        model.addAttribute("title", "Home Page");
        model.addAttribute("message", "Hi");
        model.addAttribute("type", "success");


        if(principal == null)
            return "redirect:/login";

        User user = this.userService.getUserByUserName(principal.getName());
        model.addAttribute("user", user);

        if(user.getRole().equals("ROLE_ADMIN") )
            return "redirect:/admin/index";
        return "redirect:/user/index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Login Page");
        model.addAttribute("showSidebar", false);
        return "login";
    }

    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute("title", "Sign Up - Smart Contact");
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute User user, BindingResult result,
                               @RequestParam("profileImage") MultipartFile file,
                               @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
                               RedirectAttributes redirectAttributes) {
        if(!file.isEmpty())
            user.setImageUrl(file.getOriginalFilename());
        logger.info("HOME CONTROLLER USER: {}", user);
        try {
            if(!agreement) {
                result.rejectValue("agreement", "", "You haven't agreed to our terms and conditions.");
                throw new Exception("You haven't agreed to our terms and conditions.");
            }
            if(result.hasErrors()) {
                return "signup";
            }

            this.userService.save(user);
            redirectAttributes.addFlashAttribute("message",
                    new Message("Account created successfully", "alert-success"));
            return "redirect:/signup";

        } catch (Exception e) {
            if(e instanceof DataAccessException) {
                result.rejectValue("email", "", "This email already exists.");
            }
            return "signup";
        }
    }
}