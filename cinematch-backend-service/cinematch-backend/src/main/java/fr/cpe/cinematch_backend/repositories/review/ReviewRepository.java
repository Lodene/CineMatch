package fr.cpe.cinematch_backend.repositories.review;

import fr.cpe.cinematch_backend.entities.review.Review;
import fr.cpe.cinematch_backend.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByUser(AppUser user);

    Optional<Review> findByUserAndIdMovie(AppUser user, Long idMovie);
}
