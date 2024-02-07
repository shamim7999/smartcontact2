package com.spring.boot.smartcontact.controller;

import com.spring.boot.smartcontact.Service.UserService;
import com.spring.boot.smartcontact.helper.Message;
import com.spring.boot.smartcontact.model.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class HomeController {
    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", "/home"})
    public String dispatch(Model model, Principal principal) {
        model.addAttribute("title", "Home Page");
        model.addAttribute("message", "Hi");
        model.addAttribute("type", "success");
        model.addAttribute("showBottom", false);

        if(principal == null)
            return "redirect:/login";

        User user = this.userService.getUserByUserName(principal.getName());
        model.addAttribute("user", user);

//        System.out.println("------------HOME--------------");
//        System.out.println(user);
//        System.out.println("------------------------------");

        if(user.getRole().equals("ROLE_ADMIN") )
            return "redirect:/admin/index";
        return "redirect:/user/index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Login Page");
        return "login";
    }

    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute("title", "Sign Up - Smart Contact");
        model.addAttribute("user", new User());
        model.addAttribute("showLoginButton", 1);
        model.addAttribute("showSideBar", false);
        return "signup";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute ("user") User user, BindingResult result,
                               @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
                               Model model,
                               HttpSession session) {

        model.addAttribute("showLoginButton", 1);

        try {
            if(!agreement) {
                System.out.println("You haven't agreed to our terms and conditions.");
                throw new Exception("You haven't agreed to our terms and conditions.");
            }
            if(result.hasErrors()) {
                System.out.println("ERROR: "+result.toString());
                model.addAttribute("user", user);
                return "signup";
            }

            this.userService.save(user);

            model.addAttribute("user", new User());

            session.setAttribute("message", new Message("Successfully Registered.. !! ",
                    "alert-success !!"));
            return "signup";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Something went wrong.. !! "+e.getMessage(),
                    "alert-danger !!"));
            return "signup";
        }

    }
}
