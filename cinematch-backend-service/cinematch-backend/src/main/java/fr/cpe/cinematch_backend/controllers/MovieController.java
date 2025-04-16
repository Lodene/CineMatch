package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.dtos.MovieDetailsWithReviewsDto;
import fr.cpe.cinematch_backend.dtos.MovieDto;
import fr.cpe.cinematch_backend.dtos.requests.MovieCreationRequest;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.services.MovieService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("movie")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/movies")
    public ResponseEntity<List<MovieDto>> getAllMovies() throws GenericNotFoundException {
        return ResponseEntity.ok(this.movieService.getAllMovies());
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDetailsWithReviewsDto> getMovieById(@PathVariable Long movieId)
            throws GenericNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser currentUser = (AppUser) auth.getPrincipal();
        return ResponseEntity
                .ok(this.movieService.getMovieDetailsWithReviews(movieId, currentUser.getUsername()));
    }

    // Used for testing purpose
    @PostMapping
    public ResponseEntity<MovieDto> createMovie(@RequestBody MovieCreationRequest movieCreationRequest) {
        return ResponseEntity.ok(this.movieService.createMovie(movieCreationRequest));
    }

    @PutMapping("/{movieId}")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable(value = "movieId") String movieId,
            @RequestBody MovieDto movieDto) throws GenericNotFoundException {
        long id;
        try {
            id = Long.parseLong(movieId);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(this.movieService.updateMovie(id, movieDto));
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<Boolean> deleteMovie(@PathVariable(value = "movieId") String movieId)
            throws GenericNotFoundException {
        long id;
        try {
            id = Long.parseLong(movieId);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(this.movieService.deleteMovie(id));
    }

}
