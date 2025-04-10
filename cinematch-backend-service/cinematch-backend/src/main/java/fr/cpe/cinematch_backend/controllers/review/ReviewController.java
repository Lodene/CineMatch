package fr.cpe.cinematch_backend.controllers.review;

import fr.cpe.cinematch_backend.dtos.review.ReviewRequestDto;
import fr.cpe.cinematch_backend.dtos.review.ReviewResponseDto;
import fr.cpe.cinematch_backend.services.review.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    public ReviewController() {
        System.out.println("ReviewController chargé !");
    }

    @PostMapping("/create")
    public void createReview(@RequestBody @Valid ReviewRequestDto dto) {
        System.out.println("✅ Requête reçue dans createReview");
        reviewService.createReview(dto);
    }

    @PutMapping("/update")
    public void updateReview(@RequestBody @Valid ReviewRequestDto dto) {
        reviewService.updateReview(dto);
    }

    @GetMapping("/user/{idUser}")
    public List<ReviewResponseDto> getUserReviews(@PathVariable Long idUser) {
        return reviewService.getUserReviews(idUser);
    }
}
