package cpe.cinematch_backend.orchestrator.entities;

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
public class SimilarMovieRequestEntity {
    @Id
    private UUID id;

    List<Long> similarMoviesId;

    Long originalMovieId;
}
