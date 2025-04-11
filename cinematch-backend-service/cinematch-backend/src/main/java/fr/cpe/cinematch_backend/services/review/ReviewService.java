package fr.cpe.cinematch_backend.services.review;

import fr.cpe.cinematch_backend.dtos.review.ReviewRequestDto;
import fr.cpe.cinematch_backend.dtos.review.ReviewDto;
import fr.cpe.cinematch_backend.exceptions.BadEndpointException;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;

import java.util.List;

public interface ReviewService {
    void createReview(ReviewRequestDto reviewRequestDto, String username) throws GenericNotFoundException;

    ReviewDto updateReview(Long id, ReviewRequestDto dto) throws GenericNotFoundException, BadEndpointException;

    List<ReviewDto> getUserReviews(String username) throws GenericNotFoundException;

    boolean deleteReview(long id);
}
