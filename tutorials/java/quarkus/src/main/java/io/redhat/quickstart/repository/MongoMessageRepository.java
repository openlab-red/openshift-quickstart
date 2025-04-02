package io.redhat.quickstart.repository;

import java.util.List;

import io.redhat.quickstart.entity.MongoMessage;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MongoMessageRepository implements MessageRepository<MongoMessage>, PanacheMongoRepository<MongoMessage> {

    @Override
    public List<MongoMessage> listAll() {
        return MongoMessage.listAll();
    }

    @Override
    public void persist(MongoMessage message) {
        PanacheMongoRepository.super.persist(message);
    }
}