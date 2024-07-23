package com.aadhil.springemailverification.email;

import com.aadhil.springemailverification.user.User;

public interface EmailSender {
    void send(User theUser, String url);
}
