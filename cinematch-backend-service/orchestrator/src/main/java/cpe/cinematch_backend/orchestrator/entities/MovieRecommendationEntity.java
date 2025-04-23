package cpe.cinematch_backend.orchestrator.entities;

import cpe.cinematch_backend.orchestrator.dto.MovieDto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

/**
 * Used for storage purpose
 */
@Document
@Data
public class MovieRecommendationEntity {
    @Id
    private UUID id;

    List<MovieDto> recentlyLikedMovies;

    String fromUsername;

    List<Long> recommendationsId;
}
