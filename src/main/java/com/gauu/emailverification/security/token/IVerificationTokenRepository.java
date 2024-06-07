package com.gauu.emailverification.security.token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IVerificationTokenRepository extends JpaRepository<Token,Long> {
    Optional<Token> findByToken(String token);
}
