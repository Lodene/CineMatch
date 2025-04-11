package fr.cpe.cinematch_backend.repositories.review;

import fr.cpe.cinematch_backend.entities.MovieEntity;
import fr.cpe.cinematch_backend.entities.review.ReviewEntity;
import fr.cpe.cinematch_backend.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    List<ReviewEntity> findByUser(AppUser user);

    List<ReviewEntity> findByUserAndMovie(AppUser user, MovieEntity movie);

    List<ReviewEntity> findByMovie(MovieEntity movie);

}
