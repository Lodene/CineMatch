package fr.cpe.cinematch_backend.repositories;

import fr.cpe.cinematch_backend.entities.MovieEntity;
import fr.cpe.cinematch_backend.entities.RelatedMoviesEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RelatedMoviesRepository extends JpaRepository<RelatedMoviesEntity, Long> {
    List<RelatedMoviesEntity> findAllByMovie1(MovieEntity movie1);

}
