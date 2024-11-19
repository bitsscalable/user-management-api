package com.library.applicationstarter.entitys;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Data;

@Document(collection = "email_verifications")
@Data
public class UserVerifications {

    @MongoId
    private ObjectId id;
    private String email;
    @Field(name = "initiated_on")
    private Date initiatedOn;
    private String code;
    @Field(name = "is_approved")
    private int isApproved;
    @Field(name = "approved_on")
    private Date approvedOn;
    @Field(name = "last_updated_on")
    private Date lastUpdatedOn;

}
