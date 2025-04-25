package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.requests.SimilarMovieRequest;
import fr.cpe.cinematch_backend.entities.MovieEntity;
import fr.cpe.cinematch_backend.exceptions.BadEndpointException;
import fr.cpe.cinematch_backend.exceptions.ConfigErrorException;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class RecommendationMovieService {

    @Autowired
    private Environment env;


    @Autowired
    LovedMovieService lovedMovieService;

    RestTemplate restTemplate = new RestTemplate();
    /*
    * Get similar movies
    * returns UUID of request
    * */
    public String getRecommendedMovie(String username) throws GenericNotFoundException, ConfigErrorException, BadEndpointException {
        // retrieve 10 last liked movies
        List<MovieEntity> recentlyLikedMovies = lovedMovieService.getRecentlyLikedMovies(username, 10);
        String orchestratorUrl = env.getProperty("orchestrator.url");
        if (orchestratorUrl == null) {
            throw new ConfigErrorException(500, "Orchestrator url missing","The orchestrator server is not configured");
        }
        if (recentlyLikedMovies.isEmpty()) {
            throw new BadEndpointException(400, "Not enough user info", "You have to have at least one favorite movie");
        }
        /*
        * User MUST have at lease one favorite movie.
        * */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        SimilarMovieRequest request = new SimilarMovieRequest();
        request.setRecentlyLikedMovies(recentlyLikedMovies);
        request.setFromUsername(username);
        HttpEntity<SimilarMovieRequest> requestEntity = new HttpEntity<>(request, headers);
        return restTemplate.postForObject(orchestratorUrl.concat("/orchestrator/recommendation-film"), requestEntity, String.class);
    }
}
