package com.library.applicationstarter.entitys;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Document(collection = "mail_templates")
@Data
public class MailTemplates {

    @Field(name = "template_id")
    private int templateId;
    private String subject;
    private int active;
    @Field(name = "template_path")
    private String templatePath;
    @Field(name = "start_date")
    private Date startDate;
    @Field(name = "last_updated_on")
    private Date lastUpdatedOn;

}
