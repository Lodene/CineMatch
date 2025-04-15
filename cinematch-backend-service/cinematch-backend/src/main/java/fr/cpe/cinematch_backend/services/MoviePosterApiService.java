package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.apiResponse.TmdbMovie;
import fr.cpe.cinematch_backend.dtos.apiResponse.TmdbResponse;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class MoviePosterApiService {

    private static final String API_KEY = "";
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";



    RestTemplate restTemplate = new RestTemplate();
    public String getMoviePosterUrl(long movieId) throws GenericNotFoundException {
        try {
            String searchUrl = BASE_URL + "/movie/" + movieId + "/images?api_key=" + API_KEY + "&query=" + URLEncoder.encode(String.valueOf(movieId), StandardCharsets.UTF_8.toString());
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
            throw new GenericNotFoundException(404, "Not found", "No posterPath were found with the given movie id");
        }
        return null;
    }

    public String getMovieBackdropUrl(long movieId) throws GenericNotFoundException {
        try {
            String searchUrl = BASE_URL + "/movie/" + movieId + "/images?api_key=" + API_KEY + "&query=" + URLEncoder.encode(String.valueOf(movieId), StandardCharsets.UTF_8.toString());
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
            throw new GenericNotFoundException(404, "Not found", "No backdropPath were found with the given movie id");
        }
        return null;
    }
}
