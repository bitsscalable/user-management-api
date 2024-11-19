package com.library.applicationstarter.service;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.library.applicationstarter.entitys.MailTemplates;

@Component
public interface EmailService {

    public void sendEmail(String to, String subject, String templatePath,Map<String, Object> mailVariables);

    public void triggerMail(String toMail, int templateId);

    public MailTemplates getMailDetails(Integer templateId);

}
