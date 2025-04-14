package fr.cpe.cinematch_backend.repositories;

import fr.cpe.cinematch_backend.entities.FriendRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequestEntity, Long> {
    Optional<FriendRequestEntity> findByToIdAndAskedById(Long toId, Long askedId);
}
