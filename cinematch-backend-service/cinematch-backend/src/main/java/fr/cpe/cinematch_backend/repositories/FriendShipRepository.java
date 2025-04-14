package fr.cpe.cinematch_backend.repositories;

import fr.cpe.cinematch_backend.entities.FriendshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.Optional;

public interface FriendShipRepository extends JpaRepository<FriendshipEntity, Long> {
    Optional<FriendshipEntity> findByUserId1AndUserId2(Long userId1, Long userId2);
    void deleteByUserId1AndUserId2(Long userId1, Long userId2);
    List<FriendshipEntity> findByUserId1OrUserId2(Long userId1, Long userId2);
    List<FriendshipEntity> findByUserId1(Long userId1);
    List<FriendshipEntity> findByUserId2(Long userId2);

}
