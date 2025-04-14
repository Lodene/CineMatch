package fr.cpe.cinematch_backend.repositories;

import fr.cpe.cinematch_backend.entities.FriendRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepository extends JpaRepository<FriendRequestEntity, Long> {
}
