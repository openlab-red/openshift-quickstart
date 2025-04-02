package io.redhat.quickstart.jpa.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.redhat.quickstart.data.repository.MessageRepository;
import io.redhat.quickstart.jpa.entity.SQLMessage;

@Repository
@Primary
public interface SQLMessageRepository extends JpaRepository<SQLMessage, Long>, MessageRepository<SQLMessage> {

}