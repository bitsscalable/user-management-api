package com.library.applicationstarter.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.library.applicationstarter.entitys.UserVerifications;


@Repository
public interface UserVerificationsRepo extends MongoRepository<UserVerifications,ObjectId>{

@Query("{ 'email' : ?0,'is_approved' : 0 }")
Optional<UserVerifications> getCode(String email);

}
