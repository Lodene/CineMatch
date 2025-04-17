package fr.cpe.cinematch_backend.repositories;

import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.entities.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<ConversationEntity, Long> {
    List<ConversationEntity> findByFromOrTo(AppUser from, AppUser to);

    Optional<ConversationEntity> findByFromAndTo(AppUser from, AppUser to);

}
