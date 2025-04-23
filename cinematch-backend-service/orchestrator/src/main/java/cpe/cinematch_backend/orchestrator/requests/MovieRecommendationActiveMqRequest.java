package cpe.cinematch_backend.orchestrator.requests;

import lombok.Data;

@Data
public class MovieRecommendationActiveMqRequest extends MovieRecommendationRequest{
    private String requestId;
}
