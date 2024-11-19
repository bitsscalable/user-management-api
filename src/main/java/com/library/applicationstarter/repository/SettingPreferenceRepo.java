package com.library.applicationstarter.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.library.applicationstarter.entitys.SettingPreferences;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

@Repository
public interface SettingPreferenceRepo extends MongoRepository<SettingPreferences,ObjectId> {

    @Query("{ 'username' : ?0}")
    Optional<SettingPreferences> findByUsername(String username);

}
