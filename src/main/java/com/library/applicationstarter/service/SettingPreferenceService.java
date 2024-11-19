package com.library.applicationstarter.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.library.applicationstarter.dtos.SettingPreferenceDTO;

@Component
public interface SettingPreferenceService {

    public SettingPreferenceDTO getSettings();

    public void saveSettings(List<String> genres, List<String> languages );

}
