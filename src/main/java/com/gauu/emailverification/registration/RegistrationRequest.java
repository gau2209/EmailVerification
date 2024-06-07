package com.gauu.emailverification.registration;

import com.gauu.emailverification.user.Role;
import lombok.Data;


import java.util.Collection;
@Data
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean isEnabled = false;
    private Collection<Role> roles;
}
