package com.library.applicationstarter.dtos;

import java.util.Date;
import java.util.List;


import lombok.Data;

@Data
public class BookRequestsDTO {

    private int bookId;

    private String requestedBy;

    private String status;

    private Date lastUpdatedOn;

    private int requestId;
    private String title;
    private List<String> genres;

    private String author;

    private String summary;

    private List<String> categories;

    private int bookCondition;

    private String pincode;
    private boolean isBorrowed = false;

    private boolean isActive = true;

    private String uploadedBy;

    private boolean yourRequest = false;

}
