package com.gauu.emailverification.security.password;

import com.gauu.emailverification.security.token.TokenExpirationTime;
import com.gauu.emailverification.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class ResetPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String token;
    private Date expiryDate;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ResetPasswordToken(User user,String tokenn) {
        this.user = user;
        this.token = tokenn;
        this.expiryDate = TokenExpirationTime.getExpirationTime();
    }
}
