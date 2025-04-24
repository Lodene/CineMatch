package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.dtos.MovieDto;
import fr.cpe.cinematch_backend.dtos.MovieDetailsWithReviewsDto;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.services.WatchedMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/watched-movie")
public class WatchedMovieController {

    @Autowired
    private WatchedMovieService watchedMovieService;

    @PostMapping("/{movieId}")
    public ResponseEntity addOrRemoveWatchedMovie(@PathVariable Long movieId) throws GenericNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) auth.getPrincipal();
        watchedMovieService.addOrRemoveWatchedMovie(movieId, user.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<MovieDetailsWithReviewsDto>> getMyWatchedMovies() throws GenericNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) auth.getPrincipal();
        return ResponseEntity.ok(watchedMovieService.getWatchedMoviesByUsername(user.getUsername()));
    }


    @GetMapping("/user/{username}")
    public ResponseEntity<List<MovieDetailsWithReviewsDto>> getWatchedMoviesByUsername(@PathVariable String username) throws GenericNotFoundException {
        return ResponseEntity.ok(watchedMovieService.getWatchedMoviesByUsername(username));
    }
}
