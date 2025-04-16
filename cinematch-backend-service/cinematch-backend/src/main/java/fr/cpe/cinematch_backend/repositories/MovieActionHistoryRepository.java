package fr.cpe.cinematch_backend.repositories;

import fr.cpe.cinematch_backend.entities.MovieActionHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieActionHistoryRepository extends JpaRepository<MovieActionHistoryEntity, Long> {
    Page<MovieActionHistoryEntity> findByUserIdOrderByTimestampDesc(Long userId, Pageable pageable);
}
