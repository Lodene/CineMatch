package fr.cpe.cinematch_backend.repositories;

import fr.cpe.cinematch_backend.entities.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface MovieRepository extends JpaRepository<MovieEntity, Long> {
    List<MovieEntity> findMovieEntitiesByReleaseDateBetween(Date releaseDateAfter, Date releaseDateBefore);
    List<MovieEntity> findMovieEntitiesByTitleContaining(String title);
}
