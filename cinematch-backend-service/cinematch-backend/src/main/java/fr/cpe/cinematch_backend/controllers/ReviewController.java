package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.dtos.requests.ReviewRequest;
import fr.cpe.cinematch_backend.dtos.ReviewDto;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.exceptions.BadEndpointException;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.services.review.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public void createReview(@RequestBody @Valid ReviewRequest reviewRequest) throws GenericNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser uE = (AppUser) authentication.getPrincipal();
        reviewService.createReview(reviewRequest, uE.getUsername());
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable(value = "reviewId") String reviewId,
            @RequestBody ReviewRequest reviewRequest) throws GenericNotFoundException, BadEndpointException {
        long id;
        try {
            id = Long.parseLong(reviewId);
        } catch (NumberFormatException e) {
            throw new BadEndpointException(400, "Error while formating review id", "Id could not be parsed to 'Long'");
        }
        return ResponseEntity.ok(reviewService.updateReview(id, reviewRequest));
    }

    @GetMapping
    public List<ReviewDto> getUserReviews() throws GenericNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser uE = (AppUser) authentication.getPrincipal();
        return reviewService.getUserReviews(uE.getUsername());
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Boolean> deleteReview(@PathVariable(value = "reviewId") String reviewId)
            throws BadEndpointException {
        long id;
        try {
            id = Long.parseLong(reviewId);
        } catch (NumberFormatException e) {
            throw new BadEndpointException(400, "Error while formating review id", "Id could not be parsed to 'Long'");
        }
        return ResponseEntity.ok(reviewService.deleteReview(id));
    }

    @GetMapping("/getByUsername/{username}")
    public ResponseEntity<List<ReviewDto>> getReviewsByUsername(@PathVariable String username)
            throws GenericNotFoundException {
        return ResponseEntity.ok(reviewService.getReviewsByUsername(username));
    }

}
