package fr.cpe.cinematch_backend.controllers;

import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.exceptions.BadEndpointException;
import fr.cpe.cinematch_backend.exceptions.ConfigErrorException;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.services.RecommendationMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/similar-movie")
public class RecommendationMovieController {


    @Autowired
    RecommendationMovieService recommendationMovieService;

    @GetMapping()
    public ResponseEntity<String> getRecommendedMovie() throws GenericNotFoundException, ConfigErrorException, BadEndpointException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) auth.getPrincipal();
        return ResponseEntity.ok(this.recommendationMovieService.getRecommendedMovie(user.getUsername()));
    }
}
