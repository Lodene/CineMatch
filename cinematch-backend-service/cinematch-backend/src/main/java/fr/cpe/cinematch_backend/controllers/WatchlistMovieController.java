package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.dtos.MovieDetailsWithReviewsDto;
import fr.cpe.cinematch_backend.dtos.MovieDto;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.services.WatchlistMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/watchlist")
public class WatchlistMovieController {

    @Autowired
    private WatchlistMovieService watchlistMovieService;

    @PostMapping("{movieId}")
    public ResponseEntity AddOrRemoveMovieFromWatchlist(@PathVariable Long movieId) throws GenericNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) authentication.getPrincipal();
        watchlistMovieService.AddOrRemoveMovieFromWatchlist(movieId, user.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<MovieDetailsWithReviewsDto>> getMyWatchlist() throws GenericNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) authentication.getPrincipal();
        return ResponseEntity.ok(watchlistMovieService.getWatchListWithReviewsByUsername(user.getUsername()));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<MovieDetailsWithReviewsDto>> getWatchlistByUsername(@PathVariable String username) throws GenericNotFoundException {
        return ResponseEntity.ok(watchlistMovieService.getWatchListWithReviewsByUsername(username));
    }
}
