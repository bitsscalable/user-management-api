package com.library.applicationstarter.service.impl;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.library.applicationstarter.dtos.LoginRequestDTO;
import com.library.applicationstarter.entitys.MailTemplates;
import com.library.applicationstarter.entitys.UserCreds;
import com.library.applicationstarter.entitys.UserVerifications;
import com.library.applicationstarter.entitys.Users;
import com.library.applicationstarter.repository.UserCredsRepo;
import com.library.applicationstarter.repository.UserVerificationsRepo;
import com.library.applicationstarter.repository.UsersRepo;
import com.library.applicationstarter.service.AuthService;
import com.library.applicationstarter.service.EmailService;
import com.library.applicationstarter.service.SecurityContext;
import com.library.applicationstarter.service.SequenceGeneratorService;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private EmailService emailServices;

    @Autowired
    private UserVerificationsRepo verificationsRepo;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private UserCredsRepo credsRepo;

    @Autowired
    private SequenceGeneratorService generatorService;

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private PasswordEncoder passwordEncoder;

     private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static String generateVerificationCode(int length) {
        return random.ints(length, 0, CHARACTERS.length())
                     .mapToObj(i -> String.valueOf(CHARACTERS.charAt(i)))
                     .reduce("", String::concat);
    }

    @Override
    public void sendVerificationCode(String email){

        //generate verification code

        String code = generateVerificationCode(15);

        // save verification code and email
        // UserVerifications bean = new UserVerifications();
       Optional<UserVerifications> bean = verificationsRepo.getCode(email);
       UserVerifications record = new UserVerifications();

        if(bean.isPresent()){
            // pending for verification
            record = bean.get();
            record.setCode(code);
            record.setLastUpdatedOn(new Date());
        }else{
            // new verification request
            record.setCode(code);
            record.setEmail(email);
            record.setInitiatedOn(new Date());
        }
        
        verificationsRepo.save(record);

        MailTemplates template =  emailServices.getMailDetails(1);

        Map<String, Object> mailVariables = new HashMap();
        mailVariables.put("verification_link", "http://localhost:8080/api/verification?email="+email+"&code="+code);
        System.out.println("http://localhost:8080/api/verification?email="+email+"&code="+code);
        emailServices.sendEmail(email, template.getSubject(), template.getTemplatePath()
        , mailVariables);

    }

    @Override
    public int verifyEmail(String email, String code) {
        
         //if user verifications details match
         Optional<UserVerifications> bean = verificationsRepo.getCode(email);

         if(bean.isPresent()){
            // update status in email_verifications
            if(bean.get().getCode().equals(code)){
                bean.get().setIsApproved(1);
                bean.get().setApprovedOn(new Date());
                verificationsRepo.save(bean.get());
                return isNewUser(email);
             }
         }

             
             return 0;
    }

    public int isNewUser(String emailid){

        try {
            Optional<UserCreds> bean =  credsRepo.findByUsername(emailid);
            if(bean.isPresent()){
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return 2;
    }

    @Override
    public void updateUserPassword(String password,String email) throws Exception {
        logger.info("Updating User password..");

        //validate password
        if(!validatePassword(password)){
            throw new Error("Invalid password. Password should atleast eb 6 digits");
        }
        //check if any pending verifications. 
        if(anyPendingVerifications(email)){
            throw new Error("Invalid password. Password should atleast eb 6 digits");
        }
        // continue only if no pending verifications

        // check if a record exists in users
        if(!doesUserExist(email)){
            throw new Error("Not an exitsing user. Try signing up first.");
        }

        // update user creds

        try {
            updateUserCreds(email, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Error while updating credentials");
        }
        
        // send password update successful email
        sendPasswordUpdatedMail(email,email);

        // redirect to login page
        
    }

    private boolean validatePassword(String password){

        // validate password length

        if(password.length()>=6){
            return true;
        }

        return false;

    }

    private boolean anyPendingVerifications(String email) throws Exception{

        //check if active pending records
        try {
            Optional<UserVerifications> bean = verificationsRepo.getCode(email);

            if(bean.isPresent()){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error while validating password");
        }
        

        return false;
    }

    private boolean doesUserExist(String email) throws Exception{

        try {
            Optional<Users> bean =  usersRepo.findByUsername(email);
            if(bean.isPresent()){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error while checking if username exists");
        }

        return false;

    }

    private void updateUserCreds(String email, String password) throws Exception{

        try {

            Optional<UserCreds> optionalCreds = credsRepo.findByUsername(email);
            if(optionalCreds.isPresent()){
                // update password
                UserCreds creds =  optionalCreds.get();   
                creds.setPassword(passwordEncoder.encode(password));
                creds.setLastUpdatedOn(new Date());
                credsRepo.save(creds);
            }else{
                throw new Error("User already exists");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error while updating user credentials.");
        }

    }

    private void sendPasswordUpdatedMail(String email,String firstName){
        MailTemplates template =  emailServices.getMailDetails(2);

        Map<String, Object> mailVariables = new HashMap();
        mailVariables.put("firstname",firstName);

        emailServices.sendEmail(email, template.getSubject(), template.getTemplatePath(), mailVariables);
    }

    @Override
    public void addNewUser(LoginRequestDTO request) {
       
        try {
            

            //check if password is valid
            if(!validatePassword(request.getPassword())){
                throw new Error("Invalid password. Password should atleast be 6 digits");
            }

            //check if email already exists
            if(doesUserExist(request.getEmail().toLowerCase())){
                throw new Error("Username/email id already exists");
            }

            // else create new record in users, user creds
            
            Users userBean = new Users(Long.valueOf(generatorService.getNextSequenceId("users")+""),
             request.getFirstName(), request.getMiddleName(), request.getLastName(), request.getEmail().toLowerCase(),
              1, new Date(), new Date());

              usersRepo.save(userBean);

            // update user creds
            UserCreds credsBean = new UserCreds(request.getEmail().toLowerCase(), passwordEncoder.encode(request.getPassword()), new Date());



             credsRepo.save(credsBean);

            // send welcome mail

            // sendWelcomeMail(request.getFirstName(),request.getEmail());

        } catch (Exception e) {
           e.printStackTrace();
           throw new Error("Error while creating new user. Try again");
        }
    }

    private void sendWelcomeMail(String firstName,String email){
        MailTemplates template =  emailServices.getMailDetails(3);

        Map<String, Object> mailVariables = new HashMap();
        mailVariables.put("firstname",firstName);

        emailServices.sendEmail(email, template.getSubject(), template.getTemplatePath(), mailVariables);
    }

    @Override
    public String getUserName() throws Exception {
        
        try {
           Optional<Users> user =  usersRepo.findByUsername(securityContext.getLoggedInUsername());
           if(user.isPresent()){
            return user.get().getFirstName();
           }else{
            throw new Exception("Error while retriving username.");
           }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        

    }

    @Override
    public String getEmail() throws Exception {
        logger.info("in getEmail method");
         
        try {
            return securityContext.getLoggedInUsername();
            
         } catch (Exception e) {
             e.printStackTrace();
             throw e;
         }
 
         
    }

}
