package com.library.applicationstarter.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.library.applicationstarter.entitys.Users;

@Repository
public interface UsersRepo extends MongoRepository<Users,Long>{

    @Query("{ 'email' : ?0}")
    Optional<Users> findByUsername(String email);

}
