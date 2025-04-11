package fr.cpe.cinematch_backend.repositories;

import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.entities.MovieEntity;
import fr.cpe.cinematch_backend.entities.WatchedMovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchedMovieRepository extends JpaRepository<WatchedMovieEntity, Long> {
    boolean existsByUserAndMovie(AppUser user, MovieEntity movie);

    Optional<WatchedMovieEntity> findByUserAndMovie(AppUser user, MovieEntity movie);

    List<WatchedMovieEntity> findByUser(AppUser user);
}
