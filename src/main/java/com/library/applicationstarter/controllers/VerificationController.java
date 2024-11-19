package com.library.applicationstarter.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.library.applicationstarter.service.AuthService;

@Controller()
public class VerificationController {


    @Autowired
    private AuthService authService;

     @GetMapping("/verification")
    public ResponseEntity<?>  verify(String email, String code) throws Exception {
        
        HttpHeaders headers = new HttpHeaders();
        try {
            int response =  authService.verifyEmail(email,code);

        if(response==0){
            // 0 = invalid token
            headers.add("Location", "http://localhost:4200/verificationFailed");
        }else if(response==1){
            // 1 = existing user
            // redirect to password reset page
            headers.add("Location", "http://localhost:4200/resetPassword?email="+email);
        }else if(response==2){
            // 2 = new user
            // redirect to signup page
            headers.add("Location", "http://localhost:4200/signup-user?email="+email);
        }
        } catch (Exception e) {
            e.printStackTrace();;
            throw new Exception("Error while verifying email, please try again after a while");
        }
        
            
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
    
    }

}
