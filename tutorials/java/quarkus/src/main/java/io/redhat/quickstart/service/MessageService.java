package io.redhat.quickstart.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

import io.redhat.quickstart.entity.Message;
import io.redhat.quickstart.repository.MessageRepository;

@ApplicationScoped
public class MessageService {

    @Inject
    MessageRepository messageRepository;
    
    @Inject
    Message message;

    public List<Message> listMessages() {
        return messageRepository.listAll();
    }

    @Transactional
    public void addMessage(String text) {
        message.setText(text);
        messageRepository.persist(message);
    }
}
