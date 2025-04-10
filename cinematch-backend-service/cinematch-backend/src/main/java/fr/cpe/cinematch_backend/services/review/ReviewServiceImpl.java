package fr.cpe.cinematch_backend.services.review;

import fr.cpe.cinematch_backend.dtos.review.ReviewRequestDto;
import fr.cpe.cinematch_backend.dtos.review.ReviewResponseDto;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.entities.review.Review;
import fr.cpe.cinematch_backend.repositories.review.ReviewRepository;
import fr.cpe.cinematch_backend.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AppUserRepository userRepository;

    @Override
    public void createReview(ReviewRequestDto dto) {
        AppUser user = userRepository.findById(dto.getIdUser())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Utilisateur introuvable"));

        Review review = new Review();
        review.setUser(user);
        review.setIdMovie(dto.getIdMovie());
        review.setNote(dto.getNote());
        review.setDescription(dto.getDescription());
        review.setCreatedAt(LocalDateTime.now());
        review.setModifiedAt(LocalDateTime.now());

        reviewRepository.save(review);
    }

    @Override
    public void updateReview(ReviewRequestDto dto) {
        AppUser user = userRepository.findById(dto.getIdUser())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Utilisateur introuvable"));

        Review review = reviewRepository.findByUserAndIdMovie(user, dto.getIdMovie())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                        "Review introuvable pour cet utilisateur et film"));

        if (dto.getNote() != null)
            review.setNote(dto.getNote());
        if (dto.getDescription() != null)
            review.setDescription(dto.getDescription());
        review.setModifiedAt(LocalDateTime.now());

        reviewRepository.save(review);
    }

    @Override
    public List<ReviewResponseDto> getUserReviews(Long idUser) {
        AppUser user = userRepository.findById(idUser)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Utilisateur introuvable"));

        return reviewRepository.findByUser(user).stream()
                .map(review -> new ReviewResponseDto(
                        review.getId(),
                        review.getIdMovie(),
                        review.getNote(),
                        review.getDescription(),
                        review.getCreatedAt(),
                        review.getModifiedAt()))
                .collect(Collectors.toList());
    }
}
