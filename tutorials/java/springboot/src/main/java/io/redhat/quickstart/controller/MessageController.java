package io.redhat.quickstart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.redhat.quickstart.data.Message;
import io.redhat.quickstart.service.MessageService;

@RestController
@RequestMapping("/api")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/messages")
    public List<Message> listMessages() {
        return messageService.listMessages();
    }

    @PostMapping("/add/{message}")
    public ResponseEntity<String> add(@PathVariable("message") String text) {
        messageService.addMessage(text);
        return ResponseEntity.status(201).body("Message added successfully!");
    }

    // Demo purpose
    @GetMapping("/add/{message}")
    public ResponseEntity<String> addGET(@PathVariable("message") String text) {
        return add(text);
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("{\"message\":\"pong\"}");
    }

}
