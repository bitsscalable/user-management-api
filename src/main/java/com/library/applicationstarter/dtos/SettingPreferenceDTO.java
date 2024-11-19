package com.library.applicationstarter.dtos;

import java.util.List;

import lombok.Data;

@Data
public class SettingPreferenceDTO {

    private String username;
    private List<String> preferredLanguages;
    private List<String> favGenres;

}
