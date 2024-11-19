package com.library.applicationstarter.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.applicationstarter.dtos.SettingPreferenceDTO;
import com.library.applicationstarter.entitys.SettingPreferences;
import com.library.applicationstarter.repository.SettingPreferenceRepo;
import com.library.applicationstarter.service.SecurityContext;
import com.library.applicationstarter.service.SettingPreferenceService;

@Service
public class SettingPreferenceServImpl implements SettingPreferenceService {

    private static final Logger logger = LoggerFactory.getLogger(SettingPreferenceServImpl.class);

    @Autowired
    private SettingPreferenceRepo preferenceRepo;

    @Autowired
    private SecurityContext securityContext;

    @Override
    public SettingPreferenceDTO getSettings() {
        logger.info("inside getSettings method");
        try {

          Optional<SettingPreferences> preference =   preferenceRepo.findByUsername(securityContext.getLoggedInUsername());

          if(preference.isPresent()){
            SettingPreferenceDTO settings = new SettingPreferenceDTO();
            BeanUtils.copyProperties(preference.get(), settings);

            return settings;
          }else{
            throw new Error("No Preferences found");
          }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Error while retriving settings");
        }

    }

    @Override
    public void saveSettings(List<String> genres, List<String> languages) {
      
      try {
        SettingPreferences preferences = new SettingPreferences();
        Optional<SettingPreferences> settings =  preferenceRepo.findByUsername(securityContext.getLoggedInUsername());

        if(settings.isPresent()){

          preferences = settings.get();
          preferences.setFavGenres(genres);
          preferences.setPreferredLanguages(languages);

        }else{

          preferences.setUsername(securityContext.getLoggedInUsername());
          preferences.setFavGenres(genres);
          preferences.setPreferredLanguages(languages);

        }
          preferenceRepo.save(preferences);

      } catch (Exception e) {
        e.printStackTrace();
        throw new Error("Error in saveSettings method");
      }

    }

}
