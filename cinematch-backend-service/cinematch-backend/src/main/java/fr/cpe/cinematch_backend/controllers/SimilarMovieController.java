package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.dtos.MovieDto;
import fr.cpe.cinematch_backend.exceptions.ConfigErrorException;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.services.SimilarMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/similar-movie")
public class SimilarMovieController {


    @Autowired
    SimilarMovieService similarMovieService;

    @GetMapping("/{idMovie}")
    public ResponseEntity<String> getSimilarMovie(@PathVariable Long idMovie) throws GenericNotFoundException, ConfigErrorException {
        return ResponseEntity.ok(this.similarMovieService.getSimilarMovie(idMovie));
    }
}
