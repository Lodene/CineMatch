package fr.cpe.cinematch_backend.dtos.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {

    private Long idMovie;

    @Min(0)
    @Max(5)
    private float note;

    private String description;
}
