package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.dtos.MovieDto;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.services.LovedMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loved-movies")
public class LovedMovieController {

    @Autowired
    private LovedMovieService lovedMovieService;

    @PostMapping("/{idMovie}")
    public ResponseEntity likeOrUnlikeMovie(@PathVariable Long idMovie) throws GenericNotFoundException {
        lovedMovieService.likeMovie(idMovie);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<MovieDto>> getCurentUserLovedMovies() throws GenericNotFoundException {
        return ResponseEntity.ok(lovedMovieService.getCurentUserLovedMovies()) ;
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<MovieDto>> getLovedMoviesByUserId(@PathVariable String username) throws GenericNotFoundException {
        return ResponseEntity.ok(lovedMovieService.getLovedMoviesByUsername(username));
    }

}
