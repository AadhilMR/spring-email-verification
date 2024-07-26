package com.aadhil.springemailverification.user;

import java.util.List;
import java.util.Optional;

import com.aadhil.springemailverification.login.LoginRequest;
import com.aadhil.springemailverification.registration.RegistrationRequest;
import com.aadhil.springemailverification.registration.Token;

public interface UserService {
    List<User> getUsers();
    User registerUser(RegistrationRequest request);
    Optional<User> findByEmail(String email);
    String loginUser(LoginRequest request);
    void saveUserVerificationToken(User user, String verificationToken);
    String validateToken(String token, String applicationUrl);
    Token generateNewToken(String oldToken);
}
