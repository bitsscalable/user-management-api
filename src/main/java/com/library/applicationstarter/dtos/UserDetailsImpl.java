package com.library.applicationstarter.dtos;
import java.util.Collection;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.library.applicationstarter.entitys.UserCreds;
import com.library.applicationstarter.service.impl.UserCredServiceImpl;

public class UserDetailsImpl implements UserDetails {

    private String username;
    private String password;

    public UserDetailsImpl(UserCreds user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // Or return roles/authorities if applicable
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Bean
    public UserDetailsService userDetailsService() {
    return new UserCredServiceImpl(); // Replace with your actual implementation
}

}
