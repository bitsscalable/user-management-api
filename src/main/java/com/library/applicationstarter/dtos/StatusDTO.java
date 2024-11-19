package com.library.applicationstarter.dtos;

import java.util.Map;

import lombok.Data;

@Data
public class StatusDTO {

    private int status = 200;
    private String message;
    private Map<?, ?> data;

}
