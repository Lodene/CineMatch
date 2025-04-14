package fr.cpe.cinematch_backend.repositories;

import fr.cpe.cinematch_backend.entities.FriendUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.Optional;

public interface FriendUserRepository extends JpaRepository<FriendUser, Long> {
    Optional<FriendUser> findByUserId1AndUserId2(Long userId1, Long userId2);
    void deleteByUserId1AndUserId2(Long userId1, Long userId2);

    List<FriendUser> findByUserId1OrUserId2(Long userId1, Long userId2);

}
