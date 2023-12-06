package com.db.coffeestore9.user.controller;


import com.db.coffeestore9.user.common.RegistrationForm;
import com.db.coffeestore9.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;

    @GetMapping
    public String registerForm() {
        log.info("RegistrationController -> registerForm : OK");
        return "account/register";
    }
    @PostMapping
    public String processRegistration(RegistrationForm registrationForm) {
        log.info("registrationController/processRegistration : OK");
        userService.processRegistration(registrationForm);
        return "redirect:/login";
    }

}
