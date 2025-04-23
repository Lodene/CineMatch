package cpe.cinematch_backend.orchestrator.responses;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class MovieRecommendationResponse {
    private UUID requestId;
    private List<Long> recommendationsId;
}
