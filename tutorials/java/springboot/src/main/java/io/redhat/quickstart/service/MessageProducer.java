package io.redhat.quickstart.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import io.redhat.quickstart.data.Message;
import io.redhat.quickstart.data.MongoMessage;
import io.redhat.quickstart.jpa.entity.SQLMessage;
import jakarta.annotation.PostConstruct;

@Service
public class MessageProducer {

    @Autowired
    private Environment environment;

    private List<String> profiles;

    @PostConstruct
    public void init() {
        profiles = Arrays.asList(this.environment.getActiveProfiles());
    }

    public Message produceMessage() {
        if ((profiles).contains("mongodb")) {
            return new MongoMessage();
        }
        return new SQLMessage();
    }
}