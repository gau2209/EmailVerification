package com.gauu.emailverification.security.token;

import com.gauu.emailverification.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date expiryDate;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Token(String token, User user) {
        this.token = token;
        this.user = user;
        this.expiryDate = TokenExpirationTime.getExpirationTime();
    }
}
