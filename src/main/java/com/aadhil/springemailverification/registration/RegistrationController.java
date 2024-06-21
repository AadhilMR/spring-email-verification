package com.aadhil.springemailverification.registration;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aadhil.springemailverification.user.User;
import com.aadhil.springemailverification.user.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {
    private final UserService userService;

    @PostMapping
    public String register(RegistrationRequest registrationRequest) {
        User user = userService.registerUser(registrationRequest);

        return "Success! Please, check your email to verify your account.";
    }
}
