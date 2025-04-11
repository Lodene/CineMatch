package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.dtos.MovieDto;
import fr.cpe.cinematch_backend.dtos.WatchedMovieRequestDto;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.services.WatchedMovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/watched-movie")
public class WatchedMovieController {

    @Autowired
    private WatchedMovieService watchedMovieService;

    @PostMapping("/toggle")
    public void toggleWatchedMovie(@RequestBody @Valid WatchedMovieRequestDto dto) throws GenericNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) auth.getPrincipal();
        watchedMovieService.toggleWatchedMovie(dto, user.getUsername());
    }

    @GetMapping
    public List<MovieDto> getMyWatchedMovies() throws GenericNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) auth.getPrincipal();
        return watchedMovieService.getWatchedMoviesByUsername(user.getUsername());
    }

    @GetMapping("/user/{idUser}")
    public List<MovieDto> getWatchedMoviesByUserId(@PathVariable Long idUser) throws GenericNotFoundException {
        return watchedMovieService.getWatchedMoviesByUserId(idUser);
    }
}
