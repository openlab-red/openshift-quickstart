package io.redhat.quickstart.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;


@ApplicationScoped
public class MessageRepositoryProducer {

    @ConfigProperty(name = "quarkus.profile")
    String activeProfile;

    @Inject
    MongoMessageRepository mongoMessageRepository;

    @Inject
    SQLMessageRepository sqlMessageRepository;

    @Produces
    public MessageRepository produceRepository() {
        if ("mongodb".equals(activeProfile)) {
            return mongoMessageRepository;
        } else {
            return sqlMessageRepository;
        }
    }
}