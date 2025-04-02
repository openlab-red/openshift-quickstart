package io.redhat.quickstart.service;


import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import io.redhat.quickstart.entity.Message;
import io.redhat.quickstart.entity.MongoMessage;
import io.redhat.quickstart.entity.SQLMessage;
import io.quarkus.arc.DefaultBean;
import io.quarkus.arc.profile.IfBuildProfile;

@RequestScoped
public class MessageProducer {

    @Produces
    @IfBuildProfile("mongodb")
    public Message produceMongoMessage() {
        return new MongoMessage();
    }

    @Produces
    @DefaultBean
    public Message produceSqlMessage() {
        return new SQLMessage();
    }
}