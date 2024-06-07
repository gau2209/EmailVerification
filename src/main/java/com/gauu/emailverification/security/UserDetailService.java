package com.gauu.emailverification.security;

import com.gauu.emailverification.user.IUserRepository;
import com.gauu.emailverification.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Component
public class UserDetailService implements UserDetailsService {
    private final IUserRepository userRepository;

    public UserDetailService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(UserDetail::new)
                .orElseThrow(()-> new UsernameNotFoundException("User not found" + "email"));
    }
}
