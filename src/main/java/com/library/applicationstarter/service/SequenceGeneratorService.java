package com.library.applicationstarter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.library.applicationstarter.entitys.DatabaseSequence;

import org.springframework.data.mongodb.core.FindAndModifyOptions;

@Service
public class SequenceGeneratorService {
    
    @Autowired
    private MongoOperations mongoOperations;

    public int getNextSequenceId(String collectionName) {
        Query query = new Query(Criteria.where("id").is(collectionName));
        Update update = new Update().inc("seq", 1);

        DatabaseSequence counter = mongoOperations.findAndModify(query,
                update, FindAndModifyOptions.options().returnNew(true).upsert(true),
                DatabaseSequence.class);

        return counter != null ? counter.getSeq() : 1;
    }
}
