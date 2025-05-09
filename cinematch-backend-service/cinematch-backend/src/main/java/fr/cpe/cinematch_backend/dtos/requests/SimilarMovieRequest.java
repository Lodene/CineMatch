package fr.cpe.cinematch_backend.dtos.requests;

import fr.cpe.cinematch_backend.dtos.MovieDto;
import fr.cpe.cinematch_backend.entities.MovieEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class SimilarMovieRequest {
    String fromUsername;
    List<MovieEntity> recentlyLikedMovies;
}
