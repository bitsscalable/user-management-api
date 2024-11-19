package com.library.applicationstarter.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.library.applicationstarter.dtos.SettingPreferenceDTO;
import com.library.applicationstarter.service.SettingPreferenceService;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/settings")
public class SettingsController {

    private static final Logger logger = LoggerFactory.getLogger(SettingsController.class);

    @Autowired
    private SettingPreferenceService preferenceService;

    @GetMapping("/getPreferences")
    public SettingPreferenceDTO getPreferences() {
        logger.info("In getPreferences controller");
        return preferenceService.getSettings();
    }

    @PostMapping("/saveSettingPreferences")
    public void getSettingPreferences(@RequestParam List<String> genres,@RequestParam List<String> languages ) {
        try {
            preferenceService.saveSettings(genres,languages);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Error saving preferences");
        }
        
    }

}
