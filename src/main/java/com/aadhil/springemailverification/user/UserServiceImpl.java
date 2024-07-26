package com.aadhil.springemailverification.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aadhil.springemailverification.exception.UserAlreadyExistsException;
import com.aadhil.springemailverification.login.LoginRequest;
import com.aadhil.springemailverification.registration.RegistrationRequest;
import com.aadhil.springemailverification.registration.Token;
import com.aadhil.springemailverification.registration.TokenRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(RegistrationRequest request) {
        Optional<User> user = findByEmail(request.email());

        if(user.isPresent())
            throw new UserAlreadyExistsException("User with email " + request.email() + " already exists.");

        User newUser = new User();
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole(request.role());

        return userRepository.save(newUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public String loginUser(LoginRequest request) {
        String returnMessage = "";
        Optional<User> user = findByEmail(request.email());

        if (user.isPresent()) {
            User theUser = user.get();

            if(passwordEncoder.matches(request.password(), theUser.getPassword())) {
                if (theUser.isEnabled()) {
                    returnMessage = "success";
                } else {
                    returnMessage = "User is disabled. Contact our support team.";
                }
            } else {
                returnMessage = "Invalid email or password!";
            }
        } else {
            returnMessage = "Invalid email or password!";
        }

        return returnMessage;
    }

    @Override
    public void saveUserVerificationToken(User user, String verificationToken) {
        Token token = new Token(verificationToken, user);
        tokenRepository.save(token);
    }

    @Override
    public String validateToken(String token, String applicationUrl) {
        Token theToken = tokenRepository.findByToken(token);

        if(theToken == null) {
            return "Invalid token! Please try again.";
        }

        User theUser = theToken.getUser();

        if(theUser.isEnabled()) {
            return "This account has already been verified!";
        }
        if(theToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            return "This token is already expired! Please try again with <a href='"+ applicationUrl +"/api/resend-token?token="+ token +"'>this</a>.";
        }
        theUser.setEnabled(true);
        userRepository.save(theUser);

//        return "This account verified successfully! Now you can login to your account.";
        return "This account verified successfully! Click <a href='http://localhost:5173/login'>here</a> to login to your account.";
    }

    @Override
    public Token generateNewToken(String oldToken) {
        Token token = tokenRepository.findByToken(oldToken);
        LocalDateTime tokenExpirationTime = new Token(oldToken).getExpirationTime();

        token.setToken(UUID.randomUUID().toString());
        token.setExpirationTime(tokenExpirationTime);
        return tokenRepository.save(token);
    }
}
