package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.requests.ReviewRequest;
import fr.cpe.cinematch_backend.dtos.ProfileDto;
import fr.cpe.cinematch_backend.dtos.ReviewDto;
import fr.cpe.cinematch_backend.dtos.ReviewWithFriendFlagDto;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.entities.MovieEntity;
import fr.cpe.cinematch_backend.entities.ProfileEntity;
import fr.cpe.cinematch_backend.entities.ReviewEntity;
import fr.cpe.cinematch_backend.entities.enums.MovieActionType;
import fr.cpe.cinematch_backend.exceptions.BadEndpointException;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.mappers.ReviewMapper;
import fr.cpe.cinematch_backend.mappers.ReviewWithFriendFlagMapper;
import fr.cpe.cinematch_backend.repositories.ReviewRepository;
import fr.cpe.cinematch_backend.repositories.AppUserRepository;
import fr.cpe.cinematch_backend.repositories.ProfilRepository;
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

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private ProfilRepository profilRepository;

    @Autowired
    private MovieActionHistoryService movieActionHistoryService;

    public void createReview(ReviewRequest reviewRequest, String username)
            throws GenericNotFoundException, BadEndpointException {

        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                        "username '" + username + "' does not exist"));

        MovieEntity movieEntity = movieService.getMovieEntityById(reviewRequest.getIdMovie())
                .orElseThrow(() -> new GenericNotFoundException(404, "Movie not found",
                        "movie with id '" + reviewRequest.getIdMovie() + "' does not exist"));

        // Empêcher les doublons
        if (!reviewRepository.findByUserAndMovie(user, movieEntity).isEmpty()) {
            throw new BadEndpointException(400, "Duplicate review", "User has already reviewed this movie");
        }

        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setUser(user);
        reviewEntity.setMovie(movieEntity);
        reviewEntity.setNote(reviewRequest.getNote());
        reviewEntity.setDescription(reviewRequest.getDescription());
        reviewEntity.setCreatedAt(LocalDateTime.now());
        reviewEntity.setModifiedAt(LocalDateTime.now());

        reviewRepository.save(reviewEntity);
        movieActionHistoryService.logAction(user.getId(), movieEntity.getId(), MovieActionType.REVIEW_ADD);
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
        movieActionHistoryService.logAction(
                reviewEntity.get().getUser().getId(),
                reviewEntity.get().getMovie().getId(),
                MovieActionType.REVIEW_REMOVE);
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

    public List<ReviewWithFriendFlagDto> getReviewByMovieWithFriendFlag(Long requesterId, Long movieId)
            throws GenericNotFoundException {
        AppUser requester = appUserRepository.findById(requesterId)
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                        "User with id '" + requesterId + "' not found"));

        MovieEntity movie = movieService.getMovieEntityById(movieId)
                .orElseThrow(() -> new GenericNotFoundException(404, "Movie not found",
                        "Movie with id '" + movieId + "' not found"));

        List<ReviewEntity> reviews = reviewRepository.findByMovie(movie);

        return reviews.stream().map(review -> {
            ReviewDto baseDto = ReviewMapper.INSTANCE.toReviewDto(review);

            ProfileDto profileDto = null;
            try {
                ProfileEntity profile = profilRepository.findByUserId(review.getUser().getId())
                        .orElseThrow(() -> new GenericNotFoundException(404, "Profil manquant",
                                "Profil de l'auteur non trouvé"));
                profileDto = ReviewWithFriendFlagMapper.INSTANCE.profileEntityToDto(profile);
            } catch (GenericNotFoundException e) {
                throw new RuntimeException(e);
            }

            ReviewWithFriendFlagDto enrichedDto = ReviewWithFriendFlagMapper.INSTANCE.fromReviewDtoAndProfile(baseDto,
                    profileDto);

            boolean isFriend = friendshipService.isFriend(requesterId, review.getUser().getId());
            enrichedDto.setWrittenByFriend(isFriend);

            return enrichedDto;
        }).toList();
    }
}
