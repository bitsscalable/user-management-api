package com.library.applicationstarter.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.library.applicationstarter.dtos.LoginRequestDTO;


@Component
public interface UserCredService {

    UserDetails getUserByUserName(String username);

    boolean doesUsernameExists(String username);

    boolean verifyEmail(String email, int templateId);
   
    boolean registerUser( LoginRequestDTO registerRequest) throws Exception;
}
