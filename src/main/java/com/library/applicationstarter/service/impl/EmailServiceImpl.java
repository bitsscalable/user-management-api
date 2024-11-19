package com.library.applicationstarter.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.library.applicationstarter.entitys.EmailTransaction;
import com.library.applicationstarter.entitys.MailTemplates;
import com.library.applicationstarter.repository.EmailTransactionsRepo;
import com.library.applicationstarter.repository.MailTemplateRepo;
import com.library.applicationstarter.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private EmailTransactionsRepo emailTransactionRepository;

    @Autowired
    private MailTemplateRepo mailTemplateRepo;

    @Override
    public void sendEmail(String to, String subject, String templatePath,Map<String, Object> mailVariables) {
        Context context = new Context();
        context.setVariables(mailVariables);
        int status = 0 ;
        String errorMessage = null;
        logger.info("getting template details");
        String process = templateEngine.process(templatePath, context);

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            logger.info("setting values in email template");
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(process, true); // Set to true to send HTML emails
            emailSender.send(mimeMessage);
            status = 1;  // Email sent successfully
        } catch (MessagingException e) {
            logger.info("error while sending email");
            errorMessage = e.getMessage();
            status = 0;  // Email failed
            e.printStackTrace();
        } finally {
            logger.info("Saving mail transaction");
            saveTransaction(to, subject, status, errorMessage); // save all success and failed transactions to db
        }
        
    }

    private void saveTransaction(String to, String subject, int status, String errorMessage) {
        logger.info("in saveTransaction method");
        EmailTransaction transaction = new EmailTransaction(to, subject, status, errorMessage, new Date()); //set values in email transactions dto
        try {
            emailTransactionRepository.save(transaction); //save in db
            logger.info("Mail transaction saved successfully");
        } catch (Exception e) {
            logger.error("Error while saving transaction");
            e.printStackTrace();
        }
        
    }
    @Override
    public void triggerMail(String toMail, int templateId){
        logger.info("in sendEmail using templateId method");

        try {
            //get template details
            MailTemplates template= getMailDetails(templateId);
            if(template!=null){
                logger.info("invalid templateId:"+templateId);
                sendEmail(toMail, template.getSubject(), template.getTemplatePath(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }



    }

    @Override
    public MailTemplates getMailDetails(Integer templateId){
        logger.info("in getMailDetails method. template Id:"+templateId);

        try {
           Optional<MailTemplates> template= mailTemplateRepo.findByTemplateId(templateId);
           if(template.isPresent()){
            return template.get();
           }else{
            logger.warn("Invalid template Id:"+templateId);
           }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return null;
    }
}
