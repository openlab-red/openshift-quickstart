package io.redhat.quickstart.repository;

import java.util.List;

import io.redhat.quickstart.entity.SQLMessage;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SQLMessageRepository implements MessageRepository<SQLMessage>, PanacheRepository<SQLMessage> {

    @Override
    public List<SQLMessage> listAll() {
        return SQLMessage.listAll();
    }

    @Override
    public void persist(SQLMessage message) {
        PanacheRepository.super.persist(message);
    }
}