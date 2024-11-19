package com.library.applicationstarter.entitys;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "sequences")
@Data
public class DatabaseSequence {
    @Id
    private String id; 
    private int seq;

  
}
