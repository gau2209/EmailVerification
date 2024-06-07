package com.gauu.emailverification.security.token;


import com.gauu.emailverification.user.IUserRepository;
import com.gauu.emailverification.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class VerificationTokenService implements IVerificationTokenService {

    private final IVerificationTokenRepository VerificationTokenRepository;
    private final IUserRepository UserRepository;

    @Override
    public String validateToken(String token) {
        Optional<Token> theToken = this.VerificationTokenRepository.findByToken(token);
       if(theToken.isEmpty())
       {
           return "INVALID";
       }
       User user = theToken.get().getUser();
        Calendar calendar = Calendar.getInstance();
        if(theToken.get().getExpiryDate().getTime()-calendar.getTime().getTime()<=0){
            return "EXPIRED";
        }
        user.setEnabled(true);
        this.UserRepository.save(user);
        return "VALID";
    }

    @Override
    public void saveVerificationToken(User user, String token) {
        var VerificationToken = new Token(token, user);
        this.VerificationTokenRepository.save(VerificationToken);
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return this.VerificationTokenRepository.findByToken(token);
    }
}
