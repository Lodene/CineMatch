package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.dtos.LovedMovieRequestDto;
import fr.cpe.cinematch_backend.dtos.MovieDto;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.services.LovedMovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loved-movies")
public class LovedMovieController {

    @Autowired
    private LovedMovieService lovedMovieService;

    @PostMapping
    public void likeMovie(@RequestBody @Valid LovedMovieRequestDto dto) throws GenericNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) authentication.getPrincipal();
        lovedMovieService.likeMovie(dto, user.getUsername());
    }

    @DeleteMapping("/{idMovie}")
    public void unlikeMovie(@PathVariable Long idMovie) throws GenericNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) authentication.getPrincipal();
        lovedMovieService.unlikeMovie(idMovie, user.getUsername());
    }

    @GetMapping
    public List<MovieDto> getAllLovedMovies() throws GenericNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) authentication.getPrincipal();
        return lovedMovieService.getLovedMovies(user.getUsername());
    }

    @GetMapping("/user/{idUser}")
    public List<MovieDto> getLovedMoviesByUserId(@PathVariable Long idUser) throws GenericNotFoundException {
        return lovedMovieService.getLovedMoviesByUserId(idUser);
    }

}
