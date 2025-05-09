package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.dtos.MovieDetailsWithReviewsDto;
import fr.cpe.cinematch_backend.dtos.MovieDto;
import fr.cpe.cinematch_backend.dtos.PaginatedMoviesResponse;
import fr.cpe.cinematch_backend.dtos.requests.MovieCreationRequest;
import fr.cpe.cinematch_backend.dtos.requests.MovieRecommendationRequest;
import fr.cpe.cinematch_backend.dtos.requests.MovieSearchRequest;
import fr.cpe.cinematch_backend.dtos.requests.SocketRequest;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.services.MovieService;
import fr.cpe.cinematch_backend.services.RelatedMoviesService;
import fr.cpe.cinematch_backend.services.SocketService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    SocketService socketService;

    @Autowired
    private RelatedMoviesService relatedMoviesService;
    
    @GetMapping("/movies")
    public ResponseEntity<PaginatedMoviesResponse> getAllMoviesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(this.movieService.getAllMoviesPaginated(page, size));
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDetailsWithReviewsDto> getMovieById(@PathVariable Long movieId)
            throws GenericNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        if (auth.getPrincipal() instanceof String) {
            if (((String) auth.getPrincipal()).equalsIgnoreCase("anonymousUser")) {
                username = null;
            }
        } else {
            AppUser currentUser = (AppUser) auth.getPrincipal();
            username = currentUser.getUsername();
        }

        return ResponseEntity
                .ok(this.movieService.getMovieDetailsWithReviews(movieId, username));
    }

    @GetMapping("/genres")
    public ResponseEntity<Set<String>> getAllGenres() {
        return ResponseEntity.ok(this.movieService.getAllGenres());
    }

    @PostMapping("/search")
    public ResponseEntity<PaginatedMoviesResponse> searchMovies(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestBody MovieSearchRequest request
    ) throws GenericNotFoundException {
        PaginatedMoviesResponse result = movieService.searchMovies(request, page, size);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/getRecommendedFilm")
    public ResponseEntity<String> getRecommendedFilm(@RequestBody MovieRecommendationRequest request) {
        SocketRequest socketRequest = movieService.getRecommendedFilm(request);
        return ResponseEntity.ok(socketService.sendMoviesInfoToSocket(socketRequest));
    }

    @GetMapping("/getMovieCount")
    public ResponseEntity<Long> getMovieCount() {
        return ResponseEntity.ok(movieService.getMovieCount());
    }

    @GetMapping("/trailer/{tmdbId}")
    public ResponseEntity<String> getTrailerUrl(@PathVariable Long tmdbId) {
        String trailerUrl = movieService.fetchTrailerUrl(tmdbId);
        if (trailerUrl != null) {
            return ResponseEntity.ok(trailerUrl);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trailer not found.");
        }
    }



    @GetMapping("/related-movies/{movieId}")
    public ResponseEntity<List<MovieDto>> getRelatedMovies(@PathVariable Long movieId) throws GenericNotFoundException {
        return ResponseEntity.ok(relatedMoviesService.getRelatedMoviesByMovie1(movieId));
    }

    // Used for testing purpose
    /*
     * @PostMapping
     * public ResponseEntity<MovieDto> createMovie(@RequestBody MovieCreationRequest
     * movieCreationRequest) {
     * return
     * ResponseEntity.ok(this.movieService.createMovie(movieCreationRequest));
     * }
     * 
     * @PutMapping("/{movieId}")
     * public ResponseEntity<MovieDto> updateMovie(@PathVariable(value = "movieId")
     * String movieId,
     * 
     * @RequestBody MovieDto movieDto) throws GenericNotFoundException {
     * long id;
     * try {
     * id = Long.parseLong(movieId);
     * } catch (NumberFormatException e) {
     * return ResponseEntity.badRequest().build();
     * }
     * return ResponseEntity.ok(this.movieService.updateMovie(id, movieDto));
     * }
     * 
     * @DeleteMapping("/{movieId}")
     * public ResponseEntity<Boolean> deleteMovie(@PathVariable(value = "movieId")
     * String movieId)
     * throws GenericNotFoundException {
     * long id;
     * try {
     * id = Long.parseLong(movieId);
     * } catch (NumberFormatException e) {
     * return ResponseEntity.badRequest().build();
     * }
     * return ResponseEntity.ok(this.movieService.deleteMovie(id));
     * }
     */
}
