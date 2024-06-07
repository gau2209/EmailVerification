package com.gauu.emailverification.event;

import com.gauu.emailverification.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleEvent extends ApplicationEvent {
    private User user;
    private String confirmationCode;

    public RegistrationCompleEvent(User user, String confirmationUrl) {
        super(user);
        this.user = user;
        this.confirmationCode = confirmationUrl;
    }



}
