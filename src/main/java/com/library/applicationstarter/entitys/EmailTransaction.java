package com.library.applicationstarter.entitys;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Document(collection = "email_transactions")
@Data
@AllArgsConstructor
public class EmailTransaction {

    @Id
    private String id;
    private String recipient;
    private String subject;
    private int status;
    private String errorMessage;
    private Date sentDate;


    public EmailTransaction(String recipient, String subject, int status,String  errorMessage, Date date){

        this.recipient = recipient;
        this.subject = subject;
        this.status = status;
        this.errorMessage = errorMessage;
        this.sentDate = date;

    }

}
