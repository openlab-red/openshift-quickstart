package io.redhat.quickstart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.redhat.quickstart.data.Message;
import io.redhat.quickstart.data.repository.MessageRepository;


@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageProducer messageProducer;

    public List<Message> listMessages() {
        return messageRepository.findAll();
    }

    public void addMessage(String text) {
        Message message = messageProducer.produceMessage();
        message.setText(text);
        messageRepository.save(message);
    }
}
