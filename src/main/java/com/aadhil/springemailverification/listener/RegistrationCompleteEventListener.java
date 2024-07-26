package com.aadhil.springemailverification.listener;

import java.util.UUID;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.aadhil.springemailverification.email.EmailSender;
import com.aadhil.springemailverification.event.RegistrationCompleteEvent;
import com.aadhil.springemailverification.user.User;
import com.aadhil.springemailverification.user.UserService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final UserService userService;
    private final EmailSender emailSender;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User user = event.getUser();

        // Create new token
        String verificationToken = UUID.randomUUID().toString();

        // Save the token
        userService.saveUserVerificationToken(user, verificationToken);

        // Create a new URL for user verification
        String url = event.getApplicationUrl() + "/api/verify?token=" + verificationToken;

        // Send the email
        try {
            emailSender.send(user, url);
        } catch (IllegalStateException ignored) {

        }
    }
}
