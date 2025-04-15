package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.requests.ReviewRequest;
import fr.cpe.cinematch_backend.dtos.ReviewDto;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.entities.MovieEntity;
import fr.cpe.cinematch_backend.entities.ProfileEntity;
import fr.cpe.cinematch_backend.entities.ReviewEntity;
import fr.cpe.cinematch_backend.exceptions.BadEndpointException;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.mappers.ReviewMapper;
import fr.cpe.cinematch_backend.repositories.ReviewRepository;
import fr.cpe.cinematch_backend.repositories.AppUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AppUserRepository userRepository;
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private MovieService movieService;

    public void createReview(ReviewRequest reviewRequest, String username) throws GenericNotFoundException {
        Optional<AppUser> user = appUserRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new GenericNotFoundException(404, "User not found", "username '" + username + "' does not exist");
        }
        Optional<MovieEntity> movieEntity = movieService.getMovieEntityById(reviewRequest.getIdMovie());
        if (movieEntity.isEmpty()) {
            throw new GenericNotFoundException(404, "Movie not found",
                    "movie with id'" + reviewRequest.getIdMovie() + "' does not exist");
        }
        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setUser(user.get());
        reviewEntity.setMovie(movieEntity.get());
        reviewEntity.setNote(reviewRequest.getNote());
        reviewEntity.setDescription(reviewRequest.getDescription());
        reviewEntity.setCreatedAt(LocalDateTime.now());
        reviewEntity.setModifiedAt(LocalDateTime.now());
        reviewRepository.save(reviewEntity);
    }

    public ReviewDto updateReview(Long id, ReviewRequest reviewRequest)
            throws GenericNotFoundException, BadEndpointException {

        ReviewEntity reviewEntity = reviewRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException(404,
                        "Review not found", "ReviewEntity with id '" + id + "' does not exist"));
        if (reviewRequest.getNote() == null) {
            throw new BadEndpointException(400, "Error while updating review", "No note found");
        }
        if (reviewRequest.getDescription() == null) {
            throw new BadEndpointException(400, "Error while updating review", "No description found");
        }
        reviewEntity.setNote(reviewRequest.getNote());
        reviewEntity.setDescription(reviewRequest.getDescription());
        reviewEntity.setModifiedAt(LocalDateTime.now());

        return ReviewMapper.INSTANCE.toReviewDto(reviewRepository.save(reviewEntity));
    }

    public List<ReviewDto> getUserReviews(String username) throws GenericNotFoundException {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                        "User with username '" + username + "' couldn't be found"));
        return reviewRepository.findByUser(user).stream()
                .map(ReviewMapper.INSTANCE::toReviewDto)
                .collect(Collectors.toList());
    }

    public boolean deleteReview(long id) {
        Optional<ReviewEntity> reviewEntity = reviewRepository.findById(id);
        if (reviewEntity.isEmpty()) {
            return false;
        }
        reviewRepository.deleteById(id);
        return true;
    }

    public List<ReviewDto> getReviewsByUsername(String username) throws GenericNotFoundException {
        Optional<AppUser> appUser = appUserRepository.findByUsername(username);
        if (appUser.isEmpty()) {
            throw new GenericNotFoundException(404, "User not found", "username '" + username + "' does not exist");
        }
        return reviewRepository.findByUser(appUser.get()).stream()
                .map(ReviewMapper.INSTANCE::toReviewDto).collect(Collectors.toList());
    }

    public void deleteAllByUserId(Long userId) throws GenericNotFoundException {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                        "User with id '" + userId + "' not found"));

        List<ReviewEntity> reviews = reviewRepository.findByUser(user);
        reviewRepository.deleteAll(reviews);
    }
}
