package cpe.cinematch_backend.orchestrator.responses;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.util.List;

@Data
// @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "_type")
public class MovieRecommendationResponse {
    private String requestId;
    private List<Long> recommendationsId;
}
