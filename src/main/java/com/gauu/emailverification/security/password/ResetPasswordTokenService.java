package com.gauu.emailverification.security.password;

import org.springframework.stereotype.Service;

@Service
public class ResetPasswordTokenService implements IResetPasswordTokenService {
    private final IResetPasswordTokenRepository paswordRepository;
    public ResetPasswordTokenService(IResetPasswordTokenRepository paswordRepository) {
        this.paswordRepository = paswordRepository;
    }
}
