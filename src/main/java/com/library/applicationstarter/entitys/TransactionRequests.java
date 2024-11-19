package com.library.applicationstarter.entitys;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "transaction_requests")
public class TransactionRequests {

    @Id
    private ObjectId id;

    @Field("bookId")
    private int bookId;

    @Field("requestId")
    private int requestId;

    @Field("requestedBy")
    private String requestedBy;

    @Field("status")
    private int status;

    @Field("lastUpdatedOn")
    private Date lastUpdatedOn;


}
