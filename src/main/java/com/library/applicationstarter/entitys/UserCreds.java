package com.library.applicationstarter.entitys;


import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "user_credentials")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreds {

    @MongoId
    private ObjectId id;

    @Indexed(unique = true)
    private String username;

    private String password;

    @Field(name="lastUpdatedOn")
    private Date lastUpdatedOn;

    public UserCreds(String username,String password, Date lastUpdatedOn){
        this.username = username;
        this.password = password;
        this.lastUpdatedOn = lastUpdatedOn;
    }


  
}
