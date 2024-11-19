package com.library.applicationstarter.entitys;


import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;

@Document(collection = "users")
@Data
@AllArgsConstructor
public class Users {

    @Id
    private long id;
    @Field(name = "first_name")
    private String firstName;
    @Field(name = "middle_name")
    private String middleName;
    @Field(name = "last_name")
    private String lastName;
    @Indexed(unique = true)
    @Field(name = "username")
    private String email;
    @Field(name = "is_active")
    private int isActive;
    @Field(name = "created_on")
    private Date createdOn;
    @Field(name = "last_updated_on")
    private Date lastUpdatedOn;
    // @Field(name = "path")
    // private String profilePicPath;



}
