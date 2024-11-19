package com.library.applicationstarter.entitys;


import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Document(collection = "books")
@Data
public class Books {

    @Id
    private ObjectId id;


    @Field("bookId")
    private int bookId;

    @Field("title")
    private String title;

    @Field("genres")
    private List<String> genres;

    @Field("author")
    private String author;

    @Field("summary")
    private String summary;

    @Field("categories")
    private List<String> categories;

    @Field("bookCondition")
    private int bookCondition;

    @Field("lendBookFor")
    private int lendBookFor;

    @Field("pincode")
    private String pincode;

    @Field("isDeliverable")
    private String isDeliverable;

    @Field("images")
    private List<String> images;

    @Field("createdDate")
    private Date createdDate = new Date(); // Initialize to current date

    @Field("isBorrowed")
    private boolean isBorrowed = false;

    @Field("isActive")
    private boolean isActive = true;

    @Field("uploadedBy")
    private String uploadedBy;

}
