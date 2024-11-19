package com.library.applicationstarter.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.library.applicationstarter.entitys.MailTemplates;



@Repository
public interface MailTemplateRepo extends MongoRepository<MailTemplates, Integer> {

    Optional<MailTemplates> findByTemplateId(int templateId);

}
