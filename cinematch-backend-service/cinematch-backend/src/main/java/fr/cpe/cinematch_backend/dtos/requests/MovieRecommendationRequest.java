package fr.cpe.cinematch_backend.dtos.requests;

import lombok.Data;

import java.util.List;

@Data
public class MovieRecommendationRequest {
    private String requestId;
    private String fromUsername;
    private List<Long> recommendationsId;
}
