package com.aadhil.springemailverification.email;

import java.io.UnsupportedEncodingException;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.aadhil.springemailverification.user.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailSenderImpl implements EmailSender {
    private final JavaMailSender mailSender;

    @Override
    public void send(User theUser, String url) {
        try {
            String from = "aadhil2001ahamed@gmail.com";
            String subject = "Spring Email Verification Code";
            String senderName = "Aadhil MR";
            String body = "<p>Hi, "+ theUser.getFirstName() +", </p><p>Click <a href=\""+ url +"\">here</a> to verify your account.</p>";

            MimeMessage message = mailSender.createMimeMessage();
            var messageHelper = new MimeMessageHelper(message);
            messageHelper.setFrom(from, senderName);
            messageHelper.setTo(theUser.getEmail());
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);
            mailSender.send(message);
        } catch (UnsupportedEncodingException | MessagingException ex) {
            throw new IllegalStateException("Failed to send email");
        }
    }
}
