package fr.cpe.cinematch_backend.repositories;

import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.entities.MovieEntity;
import fr.cpe.cinematch_backend.entities.LovedMovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LovedMovieRepository extends JpaRepository<LovedMovieEntity, Long> {

    boolean existsByUserAndMovie(AppUser user, MovieEntity movie);

    Optional<LovedMovieEntity> findByUserAndMovie(AppUser user, MovieEntity movie);

    List<LovedMovieEntity> findByUser(AppUser user);

    List<LovedMovieEntity> findByUserOrderByLovedAtDesc(AppUser user);
}
