package com.library.applicationstarter.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.library.applicationstarter.entitys.UserCreds;
import java.util.Optional;


@Repository
public interface UserCredsRepo extends MongoRepository<UserCreds,ObjectId> {

    @Query("{ 'username' : ?0}")
    Optional<UserCreds> findByUsername(String username);

}
