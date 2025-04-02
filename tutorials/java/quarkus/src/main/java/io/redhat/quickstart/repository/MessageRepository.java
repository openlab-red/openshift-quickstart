package io.redhat.quickstart.repository;

import java.util.List;

public interface MessageRepository<Message> {

    List<Message> listAll();
    void persist(Message message);

}
