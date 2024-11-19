package com.library.applicationstarter.entitys;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "setting_preferences")
public class SettingPreferences {

    @Id
    private ObjectId id;
    
    @Field("username")
    private String username;

    @Field("preferred_languages")
    private List<String> preferredLanguages;

    @Field("fav_genres")
    private List<String> favGenres;

}
