package com.gauu.emailverification.security.password;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {
    Optional<ResetPasswordToken> findByToken(String theToken);
}
