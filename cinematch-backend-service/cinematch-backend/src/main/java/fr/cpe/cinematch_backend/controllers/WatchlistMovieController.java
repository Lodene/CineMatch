package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.dtos.MovieDto;
import fr.cpe.cinematch_backend.dtos.WatchlistMovieRequestDto;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.services.WatchlistMovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/watchlist")
public class WatchlistMovieController {

    @Autowired
    private WatchlistMovieService watchlistMovieService;

    @PostMapping("/toggle")
    public void toggleWatchlistMovie(@RequestBody @Valid WatchlistMovieRequestDto dto) throws GenericNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) authentication.getPrincipal();
        watchlistMovieService.toggleWatchlistMovie(dto, user.getUsername());
    }

    @GetMapping
    public List<MovieDto> getMyWatchlist() throws GenericNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) authentication.getPrincipal();
        return watchlistMovieService.getWatchlistByUsername(user.getUsername());
    }

    @GetMapping("/user/{idUser}")
    public List<MovieDto> getWatchlistByUserId(@PathVariable Long idUser) throws GenericNotFoundException {
        return watchlistMovieService.getWatchlistByUserId(idUser);
    }
}
