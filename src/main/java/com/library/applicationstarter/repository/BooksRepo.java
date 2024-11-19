package com.library.applicationstarter.repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.library.applicationstarter.entitys.Books;

@Repository
public interface BooksRepo extends MongoRepository<Books,ObjectId> {

    @Query("{ 'pincode' : ?0}")
    List<Books> findByFilters(String pincode);

    @Query("{ 'bookId' : ?0}")
    Optional<Books> findByBookId(int bookId);

    @Query("{ 'bookId' : { $in: ?0 } }")
    List<Books> findBooksByBookIds(List<Integer> bookIds);

    @Query("{ 'uploadedBy' : ?0 }")
    List<Books> findBooksByUploadedBy(String uploadedBy);

}
