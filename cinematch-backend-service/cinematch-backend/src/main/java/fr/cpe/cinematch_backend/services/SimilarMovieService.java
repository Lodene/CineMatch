package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.MovieDto;
import fr.cpe.cinematch_backend.dtos.apiResponse.TmdbResponse;
import fr.cpe.cinematch_backend.dtos.requests.SimilarMovieRequest;
import fr.cpe.cinematch_backend.entities.MovieEntity;
import fr.cpe.cinematch_backend.exceptions.ConfigErrorException;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class SimilarMovieService {

    @Autowired
    private Environment env;

    @Autowired
    MovieService movieService;

    RestTemplate restTemplate = new RestTemplate();
    /*
    * Get similar movies
    * */
    public String getSimilarMovie(Long id) throws GenericNotFoundException, ConfigErrorException {
        Optional<MovieEntity> movieDto = movieService.getMovieEntityById(id);
        String orchestratorUrl = env.getProperty("orchestrator.url");
        if (orchestratorUrl == null) {
            throw new ConfigErrorException(500, "Orchestrator url missing","The orchestrator server is not configured");
        }
        if (movieDto.isEmpty()) {
            throw new GenericNotFoundException(404, "Movie not found", "Movie with id: '" + id + "' was not found");
        }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            SimilarMovieRequest request = new SimilarMovieRequest();
            request.setMovie(movieDto.get());
            HttpEntity<SimilarMovieRequest> requestEntity = new HttpEntity<>(request, headers);
            return restTemplate.postForObject(orchestratorUrl.concat("/orchestrator/similar-film"), requestEntity, String.class);

    }
}
