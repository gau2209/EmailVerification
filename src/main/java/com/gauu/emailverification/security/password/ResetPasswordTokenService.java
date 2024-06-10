package com.gauu.emailverification.security.password;

import com.gauu.emailverification.user.IUserRepository;
import com.gauu.emailverification.user.IUserService;
import com.gauu.emailverification.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResetPasswordTokenService implements IResetPasswordTokenService {
    private final IResetPasswordTokenRepository passwordRepository;
    private final IUserRepository UserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String validatePasswordResetToken(String theToken) {
        Optional<ResetPasswordToken> resetPasswordToken = this.passwordRepository.findByToken(theToken);
        if (resetPasswordToken.isEmpty()) {
            return "INVALID";
        }
        Calendar cal = Calendar.getInstance();
        if (resetPasswordToken.get().getExpiryDate().getTime() - cal.getTime().getTime() <= 0) {
            return "EXPIRED";
        }
        return "valid";
    }

    @Override
    public Optional<User> findUserByPasswordResetToken(String theToken) {
        return Optional.ofNullable(passwordRepository.findByToken(theToken).get().getUser());
    }

    @Override
    public void resetPassword(User user, String newpassword) {
        user.setPassword(passwordEncoder.encode(newpassword));
        UserRepository.save(user);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String passwordResetToken) {
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken(user, passwordResetToken);
        passwordRepository.save(resetPasswordToken);
    }
}
