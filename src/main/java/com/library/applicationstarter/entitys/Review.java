package com.library.applicationstarter.entitys;

import lombok.Data;

@Data
public class Review {

    private String reviewer;
    private double ratingScore;
    private String comments;

}
