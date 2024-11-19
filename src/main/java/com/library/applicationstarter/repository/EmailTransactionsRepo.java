package com.library.applicationstarter.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.library.applicationstarter.entitys.EmailTransaction;

@Repository
public interface EmailTransactionsRepo extends MongoRepository<EmailTransaction, String> {
}
