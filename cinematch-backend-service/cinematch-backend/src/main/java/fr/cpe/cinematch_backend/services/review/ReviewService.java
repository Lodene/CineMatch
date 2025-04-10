package fr.cpe.cinematch_backend.services.review;

import fr.cpe.cinematch_backend.dtos.review.ReviewRequestDto;
import fr.cpe.cinematch_backend.dtos.review.ReviewResponseDto;

import java.util.List;

public interface ReviewService {
    void createReview(ReviewRequestDto dto);

    void updateReview(ReviewRequestDto dto);

    List<ReviewResponseDto> getUserReviews(Long idUser);
}
