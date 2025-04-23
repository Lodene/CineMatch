package cpe.cinematch_backend.orchestrator.requests;

import cpe.cinematch_backend.orchestrator.dto.MovieDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieRecommendationRequest {
    String fromUsername;
    List<MovieDto> recentlyLikedMovies;
}
