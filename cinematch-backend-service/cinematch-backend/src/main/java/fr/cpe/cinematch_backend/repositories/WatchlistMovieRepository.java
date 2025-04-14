package fr.cpe.cinematch_backend.repositories;

import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.entities.MovieEntity;
import fr.cpe.cinematch_backend.entities.WatchlistMovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchlistMovieRepository extends JpaRepository<WatchlistMovieEntity, Long> {

    Optional<WatchlistMovieEntity> findByUserAndMovie(AppUser user, MovieEntity movie);

    List<WatchlistMovieEntity> findByUser(AppUser user);
}
