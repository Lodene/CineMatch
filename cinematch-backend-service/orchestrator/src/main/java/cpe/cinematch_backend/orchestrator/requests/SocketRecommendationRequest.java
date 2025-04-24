package cpe.cinematch_backend.orchestrator.requests;

import lombok.Data;

import java.util.List;

@Data
public class SocketRecommendationRequest {
    private String requestId;
    private String fromUsername;
    private List<Long> recommendationsId;
}
