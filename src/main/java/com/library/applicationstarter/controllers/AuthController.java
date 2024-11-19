package com.library.applicationstarter.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.applicationstarter.dtos.JwtResponseDTO;
import com.library.applicationstarter.dtos.LoginRequestDTO;
import com.library.applicationstarter.service.AuthService;
import com.library.applicationstarter.service.UserCredService;
import com.library.applicationstarter.utils.JwtUtil;




@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtils;

    @Autowired
    private UserCredService credService;

    @Autowired
    private AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {
        logger.info("in authenticateUser method..");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return ResponseEntity.ok(new JwtResponseDTO(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody LoginRequestDTO registerRequest) throws Exception {
        logger.info("in registerUser method..");

        try {
            if(credService.registerUser(registerRequest)){
                return ResponseEntity.ok("User registered successfully!");
            }
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        } catch (Exception e) {
            throw new Exception("Error while registering user");
        }
    
    }

    @GetMapping("/userExists")
    public ResponseEntity<Boolean> doesUsernameExists( String username) {
        logger.info("in doesUsernameExists method..");
        return ResponseEntity.ok(credService.doesUsernameExists(username));
        
    }

    @GetMapping("/verifyEmail")
    public void sendVerificationCode( String email) {
        logger.info("in sendVerificationCode method..");
        authService.sendVerificationCode(email);
    }

    @PostMapping("/updatePassword")
    public void updateUserPassword(String password, String email) throws Exception {
        logger.info("In updateUserPassword methos...");
        try {
            authService.updateUserPassword(password, email);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error while updating password. Try Email Verification again.");
        }
    }

    @PostMapping("/addNewUser")
    public void addNewUser(@RequestBody LoginRequestDTO request) throws Exception {
        
        try {
            
          authService.addNewUser(request);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error while adding new user");
        }
        
    }

    @GetMapping("/getUserName")
    public ResponseEntity<String> getUserName() throws Exception {
        logger.info("in getUserName method..");
        return ResponseEntity.ok(authService.getUserName());
    }
    
    

    
}
