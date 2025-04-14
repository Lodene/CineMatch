package fr.cpe.cinematch_backend.services.review;

import fr.cpe.cinematch_backend.dtos.requests.ReviewRequest;
import fr.cpe.cinematch_backend.dtos.ReviewDto;
import fr.cpe.cinematch_backend.exceptions.BadEndpointException;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;

import java.util.List;

public interface ReviewService {
    void createReview(ReviewRequest reviewRequest, String username) throws GenericNotFoundException;

    ReviewDto updateReview(Long id, ReviewRequest dto) throws GenericNotFoundException, BadEndpointException;

    List<ReviewDto> getUserReviews(String username) throws GenericNotFoundException;

    boolean deleteReview(long id);

    List<ReviewDto> getReviewsByUsername(String username) throws GenericNotFoundException;
}
