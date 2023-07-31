package com.vermeg.chtiba.entities;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_client;

    private String lastname;

    private String firstname;

    private String cin;

    private String address;

    private String phonenumber;

    private String email;

    private String pwd;

    private String token;
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return null;
    }

    public String getUsername() {
        return null;
    }

    public boolean isAccountNonExpired() {
        return false;
    }

    public boolean isAccountNonLocked() {
        return false;
    }

    public boolean isCredentialsNonExpired() {
        return false;
    }

    public boolean isEnabled() {
        return false;
    }
}


