package com.library.applicationstarter.service;

import org.springframework.stereotype.Component;

import com.library.applicationstarter.dtos.LoginRequestDTO;

@Component
public interface AuthService {

    public void sendVerificationCode(String email);

    public int verifyEmail(String email, String code) ;

    public void updateUserPassword(String password,String email)  throws Exception ;

    public void addNewUser(LoginRequestDTO request);

    public String getUserName() throws Exception;
}
