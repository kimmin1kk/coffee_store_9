package com.db.coffeestore9.user.controller;

import com.db.coffeestore9.user.repository.UserRepository;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserRepository userRepository;

    @RequestMapping("/login")
    public String login() {
        return "account/login";
    }

    @RequestMapping("/logout")
    public String logout() {
        return "index";
    }

    @GetMapping("/user-list")
    public String userList(Model model, Principal principal) {
        model.addAttribute("users", userRepository.findAll());
        return "/account/userList";
    }
}
