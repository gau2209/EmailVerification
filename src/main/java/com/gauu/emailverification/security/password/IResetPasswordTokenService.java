package com.gauu.emailverification.security.password;

import com.gauu.emailverification.user.User;

import java.util.Optional;

public interface IResetPasswordTokenService {

    String validatePasswordResetToken(String theToken);

    Optional<User> findUserByPasswordResetToken(String theToken);

    void resetPassword(User user, String password);

    void createPasswordResetTokenForUser(User user, String passwordResetToken);
}
