package com.gauu.emailverification.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.Collection;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    @NaturalId(mutable = true)
    private String email;
    private String password;
    private boolean isEnabled = false;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name="user_roles",
            joinColumns = @JoinColumn(name = "id_user",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_role",referencedColumnName = "id"))
    private Collection<Role> roles;

    public User(String firstName,String lastName,String email, String password, Collection<Role> roles){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password=password;
        this.roles=roles;
    }



}
