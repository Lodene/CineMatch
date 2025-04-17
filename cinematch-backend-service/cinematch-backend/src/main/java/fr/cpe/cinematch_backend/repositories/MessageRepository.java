package fr.cpe.cinematch_backend.repositories;

import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.entities.MessageEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findBySender(AppUser sender);

}
