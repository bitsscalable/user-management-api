package com.library.applicationstarter.repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import com.library.applicationstarter.entitys.TransactionRequests;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TransactionReqRepo extends  MongoRepository<TransactionRequests,ObjectId>{

    @Query("{ 'bookId' : ?0, 'requestedBy' : ?1 }")
    Optional<TransactionRequests> findPendingRequests(int bookId, String requestedBy);

    @Query("{'requestedBy' : ?0 }")
    List<TransactionRequests> getRequestedBooks(String requestedBy);

    @Query("{ 'bookId' : ?0, 'status' : ?1 }")
    Optional<TransactionRequests> getPendingBookRequests(int bookId, int status);

    @Query("{'bookId' : ?0 }")
    Optional<TransactionRequests> getRequestByBookId(int bookId);

    @Query("{'requestId' : ?0 }")
    Optional<TransactionRequests> findByRequestId(int bookId);

    @Query("{ 'status' : ?0, 'requestedBy' : ?1 }")
    List<TransactionRequests> getBorrowedBooksCount(int status, String requestedBy);

}
