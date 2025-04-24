package fr.cpe.cinematch_backend.dtos.requests;

import fr.cpe.cinematch_backend.dtos.MovieDto;
import lombok.Data;

import java.util.List;

@Data
public class SocketRequest {
    private String requestId;
    private String fromUsername;
    private List<MovieDto> recommendedMovies;
}
