package fr.cpe.cinematch_backend.services.review;

import fr.cpe.cinematch_backend.dtos.review.ReviewRequestDto;
import fr.cpe.cinematch_backend.dtos.review.ReviewDto;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.entities.MovieEntity;
import fr.cpe.cinematch_backend.entities.review.ReviewEntity;
import fr.cpe.cinematch_backend.exceptions.BadEndpointException;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.mappers.ReviewMapper;
import fr.cpe.cinematch_backend.repositories.review.ReviewRepository;
import fr.cpe.cinematch_backend.repositories.AppUserRepository;
import fr.cpe.cinematch_backend.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AppUserRepository userRepository;
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private MovieService movieService;

    @Override
    public void createReview(ReviewRequestDto reviewRequestDto, String username) throws GenericNotFoundException {
        Optional<AppUser> user =  appUserRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new GenericNotFoundException(404, "User not found", "username '" + username + "' does not exist");
        }
        Optional<MovieEntity> movieEntity = movieService.getMovieEntityById(reviewRequestDto.getIdMovie());
        if (movieEntity.isEmpty()) {
            throw new GenericNotFoundException(404, "Movie not found", "movie with id'" + reviewRequestDto.getIdMovie() + "' does not exist");
        }
        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setUser(user.get());
        reviewEntity.setMovie(movieEntity.get());
        reviewEntity.setNote(reviewRequestDto.getNote());
        reviewEntity.setDescription(reviewRequestDto.getDescription());
        reviewEntity.setCreatedAt(LocalDateTime.now());
        reviewEntity.setModifiedAt(LocalDateTime.now());
        reviewRepository.save(reviewEntity);
    }

    @Override
    public ReviewDto updateReview(Long id, ReviewRequestDto reviewRequestDto) throws GenericNotFoundException, BadEndpointException {

        ReviewEntity reviewEntity = reviewRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException(404,
                        "Review not found", "ReviewEntity with id '" + id + "' does not exist"));
        if (reviewRequestDto.getNote() == null) {
            throw new BadEndpointException(400, "Error while updating review", "No note found");
        }
        if (reviewRequestDto.getDescription() == null) {
            throw new BadEndpointException(400, "Error while updating review", "No description found");
        }
        reviewEntity.setNote(reviewRequestDto.getNote());
        reviewEntity.setDescription(reviewRequestDto.getDescription());
        reviewEntity.setModifiedAt(LocalDateTime.now());

        return ReviewMapper.INSTANCE.toReviewDto(reviewRepository.save(reviewEntity));
    }

    @Override
    public List<ReviewDto> getUserReviews(String username) throws GenericNotFoundException {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found", "User with username '" + username + "' couldn't be found"));
        return reviewRepository.findByUser(user).stream()
                .map(ReviewMapper.INSTANCE::toReviewDto)
                .collect(Collectors.toList());
    }
    @Override
    public boolean deleteReview(long id) {
        Optional<ReviewEntity> reviewEntity = reviewRepository.findById(id);
        if (reviewEntity.isEmpty()) {
            return false;
        }
        reviewRepository.deleteById(id);
        return  true;
    }
}
