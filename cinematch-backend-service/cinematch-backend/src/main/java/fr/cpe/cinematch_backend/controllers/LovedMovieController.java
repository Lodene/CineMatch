package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.dtos.MovieDetailsWithReviewsDto;
import fr.cpe.cinematch_backend.dtos.MovieDto;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.entities.LovedMovieEntity;
import fr.cpe.cinematch_backend.entities.MovieEntity;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.services.LovedMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loved-movies")
public class LovedMovieController {

    @Autowired
    private LovedMovieService lovedMovieService;

    // 💡 Mettre cette route EN PREMIER
    @PostMapping("/bulk")
    public ResponseEntity<Void> likeMultipleMovies(@RequestBody List<Long> movieIds) throws GenericNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) authentication.getPrincipal();
        lovedMovieService.likeMultipleMovies(user, movieIds);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{idMovie}")
    public ResponseEntity likeOrUnlikeMovie(@PathVariable Long idMovie) throws GenericNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser uE = (AppUser) authentication.getPrincipal();
        lovedMovieService.likeMovie(uE, idMovie);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<MovieDetailsWithReviewsDto>> getCurentUserLovedMovies() throws GenericNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser uE = (AppUser) authentication.getPrincipal();
        return ResponseEntity.ok(lovedMovieService.getCurentUserLovedMovies(uE));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<MovieDetailsWithReviewsDto>> getLovedMoviesByUsername(@PathVariable String username)
            throws GenericNotFoundException {
        return ResponseEntity.ok(lovedMovieService.getLovedMoviesByUsername(username));
    }
}
