package fr.cpe.cinematch_backend.repositories;

import fr.cpe.cinematch_backend.entities.MovieEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface MovieRepository extends JpaRepository<MovieEntity, Long> {
    List<MovieEntity> findMovieEntitiesByReleaseDateBetween(Date releaseDateAfter, Date releaseDateBefore);

    List<MovieEntity> findMovieEntitiesByTitleContaining(String title);

    Page<MovieEntity> findAll(Pageable pageable);

    Page<MovieEntity> findMovieEntitiesByTitleContaining(String title, Pageable pageable);

    Page<MovieEntity> findMovieEntitiesByReleaseDateBetween(Date releaseDateAfter, Date releaseDateBefore,
            Pageable pageable);
}
