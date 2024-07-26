package com.aadhil.springemailverification.registration;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.aadhil.springemailverification.email.EmailSender;
import com.aadhil.springemailverification.event.RegistrationCompleteEvent;
import com.aadhil.springemailverification.user.User;
import com.aadhil.springemailverification.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class RegistrationController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final TokenRepository tokenRepository;
    private final EmailSender emailSender;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request) {
        User user = userService.registerUser(registrationRequest);

        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));

        return ResponseEntity.ok("Success! Please, check your email to verify your account.");
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String token, final HttpServletRequest request) {
        return userService.validateToken(token, applicationUrl(request));
    }

    @GetMapping("/resend-token")
    public String requestNewToken(@RequestParam("token") String oldToken, final HttpServletRequest request) {
        Token newToken = userService.generateNewToken(oldToken);
        User theUser = newToken.getUser();
        String url = applicationUrl(request) + "/api/verify?token=" + newToken.getToken();

        emailSender.send(theUser, url);

        return "A new verification link has been sent to your email, please check to verify your account";
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
