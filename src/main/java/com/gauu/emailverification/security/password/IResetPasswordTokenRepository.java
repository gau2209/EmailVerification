package com.gauu.emailverification.security.password;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {
}
