package com.aadhil.springemailverification.registration;

import java.time.LocalDateTime;

import com.aadhil.springemailverification.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private LocalDateTime expirationTime;

    private static final int EXPIRATION_TIME = 5;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Token(String token, User user) {
        this.token = token;
        this.user = user;
        expirationTime = LocalDateTime.now().plusMinutes(EXPIRATION_TIME);
    }

    public Token(String token) {
        this.token = token;
        expirationTime = LocalDateTime.now().plusMinutes(EXPIRATION_TIME);
    }
}
