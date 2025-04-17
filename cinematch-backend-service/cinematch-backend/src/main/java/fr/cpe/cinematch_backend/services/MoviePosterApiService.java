package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.apiResponse.TmdbMovie;
import fr.cpe.cinematch_backend.dtos.apiResponse.TmdbResponse;
import fr.cpe.cinematch_backend.exceptions.BadEndpointException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class MoviePosterApiService {

    @Autowired
    private Environment env;

    private static final String BASE_URL = "https://api.themoviedb.org/3";

    RestTemplate restTemplate = new RestTemplate();
    public String getMoviePosterUrl(long movieId) throws BadEndpointException {
        String apiKey = env.getProperty("tmdb.api.key");
        if (apiKey == null || apiKey.isEmpty()) {
            return null;
        }
        try {
            String searchUrl = BASE_URL + "/movie/" + movieId + "/images?api_key=" + apiKey + "&query=" + URLEncoder.encode(String.valueOf(movieId), StandardCharsets.UTF_8);
            ResponseEntity<TmdbResponse> response = restTemplate.getForEntity(searchUrl, TmdbResponse.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<TmdbMovie> posters = response.getBody().getPosters();
                if (!posters.isEmpty()) {

                    for (TmdbMovie poster : posters) {
                        String posterPath = poster.getFilePath();
                        if (posterPath != null && !posterPath.isEmpty()) {
                            return posterPath;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new BadEndpointException(500, "API error response", "The api did not return a result");
        }
        return null;
    }

    public String getMovieBackdropUrl(long movieId) throws BadEndpointException {
        String apiKey = env.getProperty("tmdb.api.key");
        if (apiKey == null || apiKey.isEmpty()) {
            return null;
        }
        try {
            String searchUrl = BASE_URL + "/movie/" + movieId + "/images?api_key=" + apiKey + "&query=" + URLEncoder.encode(String.valueOf(movieId), StandardCharsets.UTF_8);
            ResponseEntity<TmdbResponse> response = restTemplate.getForEntity(searchUrl, TmdbResponse.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<TmdbMovie> backdrops = response.getBody().getBackdrops();
                if (!backdrops.isEmpty()) {
                    for (TmdbMovie backdrop : backdrops) {
                        String backdropPath = backdrop.getFilePath();
                        if (backdropPath != null && !backdropPath.isEmpty()) {
                            return backdropPath;
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Log the exception
            throw new BadEndpointException(500, "API error response", "The api did not return a result");
        }
        return null;
    }
}
