package com.gauu.emailverification.security.token;

import com.gauu.emailverification.user.User;

import java.util.Optional;

public interface IVerificationTokenService {
    String validateToken(String token);
    void saveVerificationToken(User user,String token);
    Optional<Token> findByToken(String token);
}
