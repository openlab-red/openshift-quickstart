package io.redhat.quickstart.data.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import io.redhat.quickstart.data.MongoMessage;

@Profile("mongodb")
@Repository
public interface MongoMessageRepository extends MongoRepository<MongoMessage, String>, MessageRepository<MongoMessage> {
}
