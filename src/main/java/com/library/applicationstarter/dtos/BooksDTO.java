package com.library.applicationstarter.dtos;

import java.io.File;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class BooksDTO {

    private int bookId;
    private String title;
private List<String> genres;
    private String author;
    private String summary;
    private List<String> categories;
    private int bookCondition;
    private int lendBookFor;
    private String pincode;
    private String isDeliverable;
    private List<MultipartFile> images;
    private String ownedBy;
    private String uploadedBy;

}
