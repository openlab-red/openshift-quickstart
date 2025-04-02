package io.redhat.quickstart.data.repository;

import java.util.List;

public interface MessageRepository<Message> {

    List<Message> findAll();
    Message save(Message message);

}
